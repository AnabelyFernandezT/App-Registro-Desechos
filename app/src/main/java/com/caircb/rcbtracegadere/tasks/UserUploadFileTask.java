package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class UserUploadFileTask {

    private Context mContext;
    private String path;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private StorageReference uploadeRef;
    private List<DtoFile> listaFileDefauld;
    private List<Long> listaFotoNovedadFrecuente;
    private List<Long> listaFotoMotivoNoRecoleccion;

    public UserUploadFileTask(Context context,String path){
            this.mContext=context;
            this.path=path;

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

    }

    public void uploadRecoleccion(
            List<DtoFile> listaFileDefauld,
            Integer idAppManifiesto
    ){
        this.listaFileDefauld = listaFileDefauld;
        //fotos novedades frecuente...
        listaFotoNovedadFrecuente =  MyApp.getDBO().manifiestoFotografiasDao().consultarFotografiasUpload(idAppManifiesto,1);
        //fotos motivos no recoleccion...
        listaFotoMotivoNoRecoleccion = MyApp.getDBO().manifiestoFotografiasDao().consultarFotografiasUpload(idAppManifiesto,2);

        sendFileDefauld(0);
    }

    private void sendFileDefauld(Integer position){
        if(position<listaFileDefauld.size()) {
            uploadeRef = storageRef.child(listaFileDefauld.get(position).getUrl());
            sendToStorage(Utils.decodeBase64toByte(listaFileDefauld.get(position).getFile()), position);
        }else{

        }
    }

    private void sendToStorage(byte[] imagen, final Integer index){
        if(uploadeRef != null){
            uploadeRef.putBytes(imagen).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //notificate to send imagen... database local

                    //sen next file...
                    sendFileDefauld(index + 1);
                }
            });
        }
    }



}
