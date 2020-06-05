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

    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    StorageReference uploadeRef;

    String pathEvidencias = "evidencias";
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    List<ItemFoto> listaEvidenciasFotos;

    public UserRegistrarRecoleccion(Context context,
                                    Integer idAppManifiesto) {
        super(context);
        this.idAppManifiesto=idAppManifiesto;
    }

    @Override
    public void execute() {
        //obtener datos para realizar el registro...

            //obtner detalles

            //Subir Fotos
            //pathEvidencias = pathEvidencias + "/" + getPath() + "/" + m.getNumeroManifiesto();
            //uploadImagen();


    }

    private RequestManifiesto createRequestManifiesto(){
        RequestManifiesto rq = null;
        ManifiestoEntity m = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        if(m!=null) {
            rq = new RequestManifiesto();
            rq.setIdAppManifiesto(idAppManifiesto);
            rq.setNumeroManifiesto(m.getNumeroManifiesto());
            rq.setNumeroManifiestoCliente(m.getNumManifiestoCliente());
            rq.setUrlFirmaTransportista(m.getTransportistaFirmaUrl());
            //rq.setResponsableEntregaIdentificacion(m.getTecnicoIdentificacion());
            rq.setUrlFirmaResponsableEntrega(m.getTecnicoFirmaUrl());
            rq.setUsuarioResponsable(MySession.getIdUsuario());
            rq.setNovedadReportadaCliente(m.getNovedadEncontrada());
            rq.setUrlAudioNovedadCliente(m.getNovedadAudio());
            rq.setFechaRecoleccion(m.getFechaManifiesto());

            rq.setPaquete(createRequestPaquete(m.getTipoPaquete()!=null?(m.getTipoPaquete()>0?m.getTipoPaquete():null):null));
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



    private void registrar(){
        Toast.makeText(getActivity(),"IMAGENES EN FIREBASE!!", Toast.LENGTH_SHORT).show();
    }

    private void sendToStorage(byte[] imagen, final Integer index){
        if(uploadeRef != null){
            uploadeRef.putBytes(imagen).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    ProgressHide();
                    message(e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sendImagen(index + 1);
                }
            });
        }
    }

    private List<ItemFoto> listaFotos (Integer idAppManifiesto){

        List<ItemFoto>listaFoto = MyApp.getDBO().manifiestoFotografiasDao().obtenerAllFotosByIdManifiesto(idAppManifiesto);
        List<ItemFoto> listaFirmaTecnico= MyApp.getDBO().manifiestoDao().obtenerFotoFirmaTecnicoByIdManifiesto(idAppManifiesto);
        List<ItemFoto> listaFirmaTranspor = MyApp.getDBO().manifiestoDao().obtenerFotoFirmaTransportistaByIdManifiesto(idAppManifiesto);

        listaFoto.addAll(listaFirmaTecnico);
        listaFoto.addAll(listaFirmaTranspor);

        return listaFoto == null ? new ArrayList<ItemFoto>() :  listaFoto;
    }

    private void uploadImagen(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        listaEvidenciasFotos = listaFotos(idAppManifiesto);

        if(listaEvidenciasFotos != null && listaEvidenciasFotos.size() >0){
            //enviamos evidencias
            sendImagen(0);
        }

    }

    private void sendImagen(Integer index){
        /*Evidencias*/
        if(index < listaEvidenciasFotos.size()){
            if(listaEvidenciasFotos.get(index).getTipo() == 1 ){
                uploadeRef = storageRef.child(pathEvidencias + "/noRecoleccion/" + listaEvidenciasFotos.get(index).getCode()+"_IMG");
                sendToStorage(Utils.decodeBase64toByte(listaEvidenciasFotos.get(index).getFoto()), index);

            }else if(listaEvidenciasFotos.get(index).getTipo() == 2 ){
                uploadeRef = storageRef.child(pathEvidencias + "/novedades/" + listaEvidenciasFotos.get(index).getCode()+"_IMG");
                sendToStorage(Utils.decodeBase64toByte(listaEvidenciasFotos.get(index).getFoto()), index);

            }else if(listaEvidenciasFotos.get(index).getTipo() == 10 ){
                uploadeRef = storageRef.child(pathEvidencias + "/" + listaEvidenciasFotos.get(index).getFotoUrl());
                sendToStorage(Utils.decodeBase64toByte(listaEvidenciasFotos.get(index).getFoto()), index);
            }
        }

        if(index == listaEvidenciasFotos.size()){ // registro todas las fotos al firebase
            registrar();
        }

    }


    private String getPath() {
        return simpleDate.format(new Date());
    }
}
