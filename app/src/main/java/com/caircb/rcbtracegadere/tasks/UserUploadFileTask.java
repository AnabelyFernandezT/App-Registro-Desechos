package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.models.DtoFile;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserUploadFileTask {

    private Context mContext;
    private String path;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private StorageReference uploadeRef;
    private List<DtoFile> listaFileDefauld;
    private List<Integer> listaFotoNovedadFrecuente;
    private List<Integer> listaFotoMotivoNoRecoleccion;

    public UserUploadFileTask(Context context,String path){
            this.mContext=context;
            this.path=path;

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

    }

    public void uploadRecoleccion(
            List<DtoFile> listaFileDefauld
    ){
        this.listaFileDefauld = listaFileDefauld;
        //fotos novedades frecuente...

        //fotos motivos no recoleccion...
        sendFileDefauld(0);
    }

    private void sendFileDefauld(Integer position){

    }



}
