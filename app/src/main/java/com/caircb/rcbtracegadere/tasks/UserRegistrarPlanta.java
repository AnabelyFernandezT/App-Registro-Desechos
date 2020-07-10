package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadFrecuente;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPlanta;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarPlanta extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;
    Double peso;

    ManifiestoEntity model;
    UserUploadFileTask userUploadFileTask;
    DtoFile firmaPlanta;
    DtoFile novedadFrecuente;
    String path ="planta";
    private List<DtoFile> listaFileDefauld;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");


    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    public UserRegistrarPlanta(Context context,
                                Integer idAppManifiesto,
                               Double pesoPlanta) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.peso = pesoPlanta;
    }

    @Override
    public void execute() {
        listaFileDefauld = new ArrayList<>();

        progressShow("Registrando recoleccion...");
        model =  MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        firmaPlanta =MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA,MyConstant.STATUS_RECEPCION_PLANTA);
        if(firmaPlanta!=null && !firmaPlanta.isSincronizado())listaFileDefauld.add(firmaPlanta);

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
        userUploadFileTask.uploadRecoleccionPlanta(listaFileDefauld,idAppManifiesto);

    }

    private void register(){

       final RequestManifiestoPlanta request = createRequestManifiestoPlanta();
        Gson g = new Gson();
        String f = g.toJson(request);
        System.out.println(f);

       /*
        if(request!=null){
            Gson g = new Gson();
            String f = g.toJson(request);
            WebService.api().registrarPlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                        if(response.body().getExito()){
                            MyApp.getDBO().manifiestoDao().updateManifiestoToRecolectadoPlanta(idAppManifiesto);
                            finalizar();
                            progressHide();
                        }else {
                            message(response.body().getMensaje());
                        }
                    }else{
                        message(response.body().getMensaje());
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                }
            });
        }
        */
    }
    private String getPath() { return simpleDate.format(new Date());}

    private RequestManifiestoPlanta createRequestManifiestoPlanta(){
        RequestManifiestoPlanta rq = null;
        if(model!=null) {
            rq = new RequestManifiestoPlanta();
            rq.setIdAppManifiesto(idAppManifiesto);
            rq.setNumeroManifiesto(model.getNumeroManifiesto());
            rq.setPeso(peso);
            rq.setUrlFirmaPlanta(firmaPlanta!=null?(path+"/"+firmaPlanta.getUrl()):"");
            rq.setFechaRecepcionPlanta(model.getFechaRecepcionPlanta());
            rq.setNovedadFrecuentePlanta(createRequestNovedadFrecuente());

        }

        return rq;
    }

    private List<RequestManifiestoNovedadFrecuente> createRequestNovedadFrecuente(){
        List<RequestManifiestoNovedadFrecuente> resp = new ArrayList<>();
        List<Integer> novedad = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchConsultarNovedadFrecuentePlanta(idAppManifiesto);
        if(novedad.size()>0){
            for (Integer id:novedad) {
                resp.add(new RequestManifiestoNovedadFrecuente(
                        id,
                        createFotografia(id,3),
                        path+"/"+"novedadfrecuente"
                ));
            }
        }
        return resp;
    }

    private List<RequestNovedadFoto> createFotografia(Integer idCatalogo, Integer tipo) {
        return MyApp.getDBO().manifiestoFileDao().consultarFotografias(idAppManifiesto,idCatalogo,tipo);
    }

    private void finalizar(){
        if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }


}
