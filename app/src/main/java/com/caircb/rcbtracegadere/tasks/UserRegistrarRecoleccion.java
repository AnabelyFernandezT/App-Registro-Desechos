package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoFotografiaEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemFirmasManifiesto;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
        ManifiestoEntity m = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        if(m!=null){
            RequestManifiesto rq = new RequestManifiesto();
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

            //obtner detalles

            //Subir Fotos
            pathEvidencias = pathEvidencias + "/" + getPath() + "/" + m.getNumeroManifiesto();
            uploadImagen();
        }

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
