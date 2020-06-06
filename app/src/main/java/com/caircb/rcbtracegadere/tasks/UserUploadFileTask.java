package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
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
    private ItemFile file;

    public interface OnUploadFileListener {
        public void onSuccessful();
        public void onFailure();
    }

    private OnUploadFileListener mOnUploadFileListener;

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
        listaFotoNovedadFrecuente =  MyApp.getDBO().manifiestoFileDao().consultarFotografiasUpload(idAppManifiesto, ManifiestoFileDao.FOTO_NOVEDAD_FRECUENTE);
        //fotos motivos no recoleccion...
        listaFotoMotivoNoRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFotografiasUpload(idAppManifiesto,ManifiestoFileDao.FOTO_NO_RECOLECCION);

        sendFileDefauld(0);
    }

    private void sendFileDefauld(Integer position){
        if(position<listaFileDefauld.size()) {
            uploadeRef = storageRef.child(this.path+"/"+listaFileDefauld.get(position).getUrl());
            sendToStorage(Utils.decodeBase64toByte(listaFileDefauld.get(position).getFile()), position,1l,listaFileDefauld.get(position).getId());
        }else{
            sendFileNovedadFrecuente(0);
        }
    }

    private void sendFileNovedadFrecuente(Integer position){
        if(position < listaFotoNovedadFrecuente.size()){
            file = MyApp.getDBO().manifiestoFileDao().obtenerFotosById(listaFotoNovedadFrecuente.get(position));
            uploadeRef = storageRef.child(this.path+"/novedadfrecuente/"+file.getUrl());
            sendToStorage(Utils.decodeBase64toByte(file.getFile()), position,2l,listaFotoNovedadFrecuente.get(position));
        }else{
            file = null;
            sendFileNovedadFrecuente(0);
        }
    }

    private void sendFileMotivoNoRecoleccion(Integer position){
        if(position < listaFotoMotivoNoRecoleccion.size()){
            file = MyApp.getDBO().manifiestoFileDao().obtenerFotosById(listaFotoMotivoNoRecoleccion.get(position));
            uploadeRef = storageRef.child(this.path+"/notivonorecoleccion/"+file.getUrl());
            sendToStorage(Utils.decodeBase64toByte(file.getFile()), position,3l,listaFotoMotivoNoRecoleccion.get(position));
        }else{
            finalizar();
        }
    }

    private void finalizar(){
        if(mOnUploadFileListener!=null){
            mOnUploadFileListener.onSuccessful();
        }
    }

    private void sendToStorage(byte[] imagen, final Integer index,final long tipo, final long id){
        if(uploadeRef != null){
            uploadeRef.putBytes(imagen).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    String d="";
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //send next file...
                    if(tipo==1l){
                        MyApp.getDBO().manifiestoFileDao().actualizarToSincronizado(id,true);
                        sendFileDefauld(index + 1);
                    }
                    else if(tipo==2l){
                        MyApp.getDBO().manifiestoFileDao().actualizarToSincronizado(id,true);
                        sendFileNovedadFrecuente(index + 1);
                    }
                    else if (tipo==3l){
                        MyApp.getDBO().manifiestoFileDao().actualizarToSincronizado(id,true);
                        sendFileMotivoNoRecoleccion(index + 1);
                    }
                }
            });
        }
    }

    public void setOnUploadFileListener(@NonNull OnUploadFileListener l){
        mOnUploadFileListener = l;
    }



}
