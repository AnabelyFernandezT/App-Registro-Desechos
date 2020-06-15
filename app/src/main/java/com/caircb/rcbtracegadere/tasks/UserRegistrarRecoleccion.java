package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.generics.MyPrint;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDet;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDetBultos;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadFrecuente;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadNoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPaquete;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarRecoleccion extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;

    ManifiestoEntity model;
    String path ="recoleccion";
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    private List<DtoFile> listaFileDefauld;
    UserUploadFileTask userUploadFileTask;
    DtoFile firmaTransportista;
    DtoFile firmaTecnicoGenerador;
    DtoFile audioNovedadCliente;
    Location location;
    MyPrint print;

    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    public UserRegistrarRecoleccion(Context context,
                                    Integer idAppManifiesto,
                                    Location location) {
        super(context);
        this.idAppManifiesto=idAppManifiesto;
        this.location=location;
    }

    @Override
    public void execute() {
        //Subir Fotos
        progressShow("registrando recoleccion...");
        model =  MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);

        //images por defecto
        listaFileDefauld = new ArrayList<>();

        firmaTransportista = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, MyConstant.STATUS_RECOLECCION);
        if(firmaTransportista!=null && !firmaTransportista.isSincronizado())listaFileDefauld.add(firmaTransportista);

        firmaTecnicoGenerador = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR, MyConstant.STATUS_RECOLECCION);
        if(firmaTecnicoGenerador!=null && !firmaTecnicoGenerador.isSincronizado())listaFileDefauld.add(firmaTecnicoGenerador);

        audioNovedadCliente = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.AUDIO_RECOLECCION, MyConstant.STATUS_RECOLECCION);
        if(audioNovedadCliente!=null && !audioNovedadCliente.isSincronizado())listaFileDefauld.add(audioNovedadCliente);

        path = path + "/" + getPath() + "/" + model.getNumeroManifiesto();
        userUploadFileTask= new UserUploadFileTask(getActivity(),path);
        userUploadFileTask.setOnUploadFileListener(new UserUploadFileTask.OnUploadFileListener() {
            @Override
            public void onSuccessful() {
                register();
            }

            @Override
            public void onFailure(String message) {
                progressHide();
                message(message);
            }
        });
        userUploadFileTask.uploadRecoleccion(listaFileDefauld,idAppManifiesto);
    }

    private void register(){
        RequestManifiesto request = createRequestManifiesto();
        if(request!=null){
            Gson g = new Gson();
            String f = g.toJson(request);

            WebService.api().registrarRecoleccion(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        //actualizar el estado a recibido del manifiesto...
                        if(response.body().getExito()) {
                            MyApp.getDBO().manifiestoDao().updateManifiestoToRecolectado(idAppManifiesto);
                            progressHide();
                            //ejecutar el proceso de imprecion..
                            imprimirEtiquetas();
                        }else{
                            progressHide();
                            message(response.body().getMensaje());
                        }

                    }else{
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });
        }
    }

    private RequestManifiesto createRequestManifiesto(){
        RequestManifiesto rq = null;
        if(model!=null) {
            rq = new RequestManifiesto();
            rq.setIdAppManifiesto(idAppManifiesto);
            rq.setNumeroManifiesto(model.getNumeroManifiesto());
            rq.setNumeroManifiestoCliente(model.getNumManifiestoCliente());
            rq.setUrlFirmaTransportista(firmaTransportista!=null? (path+"/"+firmaTransportista.getUrl()):"");
            //rq.setResponsableEntregaIdentificacion(m.getTecnicoIdentificacion());
            rq.setUrlFirmaResponsableEntrega(firmaTecnicoGenerador!=null?(path+"/"+firmaTecnicoGenerador.getUrl()):"");
            rq.setUsuarioResponsable(MySession.getIdUsuario());
            rq.setNovedadReportadaCliente(model.getNovedadEncontrada());
            rq.setUrlAudioNovedadCliente(audioNovedadCliente!=null?( path+"/"+audioNovedadCliente.getUrl()):"");
            rq.setFechaRecoleccion(model.getFechaRecoleccion());
            rq.setLatitude(location!=null?location.getLatitude():null);
            rq.setLongitude(location!=null?location.getLongitude():null);
            rq.setPaquete(createRequestPaquete(model.getTipoPaquete()!=null?(model.getTipoPaquete()>0?model.getTipoPaquete():null):null));
            rq.setDetalles(createRequestDet());
            rq.setNovedadFrecuente(createRequestNovedadFrecuente());
            rq.setNovedadNoRecoleccion(createRequestNoRecoleccion());
        }
        return  rq;
    }

    private List<RequestManifiestoDet> createRequestDet(){
        List<RequestManifiestoDet> resp= new ArrayList<>();
        List<ManifiestoDetalleEntity> det = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleSeleccionados(idAppManifiesto);
        if(det.size()>0){
            for (ManifiestoDetalleEntity d:det){
                resp.add( new RequestManifiestoDet(
                        d.getIdAppManifiestoDetalle(),
                        d.getPesoUnidad(),
                        d.getCantidadBulto(),
                        createRequestBultos(d.getIdAppManifiestoDetalle())
                ));
            }
        }
        return resp;
    }

    private List<RequestManifiestoDetBultos> createRequestBultos(Integer idAppManifiestoDetalle){
        List<RequestManifiestoDetBultos> resp = new ArrayList<>();
        List<ManifiestoDetallePesosEntity> bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idAppManifiestoDetalle);
        if(bultos.size()>0){
            int i=1;
            for (ManifiestoDetallePesosEntity p:bultos){
                resp.add(new RequestManifiestoDetBultos(
                        i,
                        p.getValor(),
                        p.getDescripcion(),
                        p.getCodeQr()
                ));
                i++;
            }
        }
        return resp;
    }

    private List<RequestManifiestoNovedadFrecuente> createRequestNovedadFrecuente(){
        List<RequestManifiestoNovedadFrecuente> resp = new ArrayList<>();
        List<Integer> novedad = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchConsultarNovedadFrecuente(idAppManifiesto);
        if(novedad.size()>0){
            for (Integer id:novedad) {
                resp.add(new RequestManifiestoNovedadFrecuente(
                        id,
                        createFotografia(id,1)
                ));
            }
        }
        return resp;
    }

    private List<RequestManifiestoNovedadNoRecoleccion> createRequestNoRecoleccion(){
        List<RequestManifiestoNovedadNoRecoleccion> resp = new ArrayList<>();
        List<Integer> novedad = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchConsultarMotivoNoRecoleccion(idAppManifiesto);
        if(novedad.size()>0){
            for (Integer id:novedad) {
                resp.add(new RequestManifiestoNovedadNoRecoleccion(
                        id,
                        createFotografia(id,2)
                ));
            }
        }
        return resp;
    }

    private List<RequestNovedadFoto> createFotografia(Integer idCatalogo,Integer tipo) {
        return MyApp.getDBO().manifiestoFileDao().consultarFotografias(idAppManifiesto,idCatalogo,tipo);
    }

    private RequestManifiestoPaquete createRequestPaquete(Integer idPaquete){
        RequestManifiestoPaquete resp=null;
        if(idPaquete!=null) {
            ManifiestoPaquetesEntity p = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(idAppManifiesto, idPaquete);
            if(p!=null){
                resp = new RequestManifiestoPaquete();
                resp.setAdicionalFunda(p.getAdFundas());
                resp.setAdicionalGuardian(p.getAdGuardianes());
                resp.setCantidadFunda(p.getDatosFundas());
                resp.setCantidadGuardian(p.getDatosGuardianes());
                resp.setCantidadPendienteFunda(p.getDatosFundasPendientes());
                resp.setCantidadPendienteGuardian(p.getDatosGuardianesPendientes());
                resp.setPqh(p.getPqh());
                resp.setIdPaquete(idPaquete);
            }
        }
        return resp;
    }

    private String getPath() { return simpleDate.format(new Date());}

    private void imprimirEtiquetas(){
        try {
            print = new MyPrint(getActivity());
            print.setOnPrinterListener(new MyPrint.OnPrinterListener() {
                @Override
                public void onSuccessful() {
                    if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                }

                @Override
                public void onFailure(String message) {
                    message(message);
                    if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                }
            });
            print.pinter(idAppManifiesto);
        }catch (Exception e){
            if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
        }
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}
