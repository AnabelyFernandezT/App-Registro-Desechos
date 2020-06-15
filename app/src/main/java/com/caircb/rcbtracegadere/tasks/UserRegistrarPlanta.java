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

import java.util.ArrayList;
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

        progressShow("registrando recoleccion...");
        model =  MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        firmaPlanta =MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA,MyConstant.STATUS_RECEPCION_PLANTA);
        if(firmaPlanta!=null && !firmaPlanta.isSincronizado())listaFileDefauld.add(firmaPlanta);

       /* userUploadFileTask= new UserUploadFileTask(getActivity(),path);
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
        });*/
       register();


    }

    private void register(){
       RequestManifiestoPlanta request = createRequestManifiestoPlanta();
        if(request!=null){
            WebService.api().registrarPlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                            MyApp.getDBO().manifiestoDao().updateManifiestoToRecolectadoPlanta(idAppManifiesto);
                            finalizar();
                            progressHide();


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
    }

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
        List<Integer> novedad = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchConsultarNovedadFrecuente(idAppManifiesto);
        if(novedad.size()>0){
            for (Integer id:novedad) {
                resp.add(new RequestManifiestoNovedadFrecuente(
                        id,
                        createFotografia(id,1),
                        path+"/"+"notivonorecoleccion"
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

    public void setOnRegisterListener(@NonNull UserRegistrarPlanta l){

    }


}
