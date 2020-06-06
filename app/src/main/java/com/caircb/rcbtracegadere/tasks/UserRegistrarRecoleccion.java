package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoFotografiaEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoObservacionFrecuenteEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.ItemFirmasManifiesto;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDet;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDetBultos;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadFrecuente;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadNoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPaquete;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRegistrarRecoleccion extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;

    ManifiestoEntity model;
    String path ="recoleccion";
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    private List<DtoFile> listaFileDefauld;
    UserUploadFileTask userUploadFileTask;


    public UserRegistrarRecoleccion(Context context,
                                    Integer idAppManifiesto) {
        super(context);
        this.idAppManifiesto=idAppManifiesto;
    }

    @Override
    public void execute() {
        //Subir Fotos
        model =  MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);

        //images por defecto
        listaFileDefauld = new ArrayList<>();
        listaFileDefauld.add(new DtoFile(model.getTransportistaFirmaUrl(),model.getTransportistaFirmaImg()));
        listaFileDefauld.add(new DtoFile(model.getTecnicoFirmaUrl(),model.getTecnicoFirmaImg()));
        if(model.getNovedadAudio()!=null && model.getNovedadAudio().length()>0)listaFileDefauld.add(new DtoFile(model.getNombreNovedadAudio(),model.getNovedadAudio()));

        path = path + "/" + getPath() + "/" + model.getNumeroManifiesto();
        userUploadFileTask= new UserUploadFileTask(getActivity(),path);
        userUploadFileTask.uploadRecoleccion(listaFileDefauld,idAppManifiesto);

    }

    private RequestManifiesto createRequestManifiesto(){
        RequestManifiesto rq = null;
        if(model!=null) {
            rq = new RequestManifiesto();
            rq.setIdAppManifiesto(idAppManifiesto);
            rq.setNumeroManifiesto(model.getNumeroManifiesto());
            rq.setNumeroManifiestoCliente(model.getNumManifiestoCliente());
            rq.setUrlFirmaTransportista(path+"/"+model.getTransportistaFirmaUrl());
            //rq.setResponsableEntregaIdentificacion(m.getTecnicoIdentificacion());
            rq.setUrlFirmaResponsableEntrega(path+"/"+model.getTecnicoFirmaUrl());
            rq.setUsuarioResponsable(MySession.getIdUsuario());
            rq.setNovedadReportadaCliente(model.getNovedadEncontrada());
            rq.setUrlAudioNovedadCliente(path+"/"+model.getNovedadAudio());
            rq.setFechaRecoleccion(model.getFechaManifiesto());

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
                        new BigDecimal(d.getPesoUnidad()),
                        new BigDecimal(d.getCantidadBulto()),
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
                        new BigDecimal(p.getValor()),
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
        return MyApp.getDBO().manifiestoFotografiasDao().consultarFotografias(idAppManifiesto,idCatalogo,tipo);
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


    private String getPath() {
        return simpleDate.format(new Date());
    }
}
