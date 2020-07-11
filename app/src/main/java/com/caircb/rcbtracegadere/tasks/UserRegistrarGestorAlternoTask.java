package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.LotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoNovedadNoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.models.request.RequestNovedadesGestores;
import com.caircb.rcbtracegadere.models.request.RequestRegistroGenerador;
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

public class UserRegistrarGestorAlternoTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;
    String path ="gestor";
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    LotePadreEntity model;
    DtoFile firmaRecoleccion;
    List<DtoFile> fotosGestores;
    private List<DtoFile> listaFileDefauld;
    UserUploadFileTask userUploadFileTask;
    String novedad;
    Double pesoGestor;
    String correG;

    public interface onRegisterAlternoListener{
        public void onSussfull();
    }

    private onRegisterAlternoListener mOnRegisterAlternoListener;

    public UserRegistrarGestorAlternoTask(Context context,
                                          Integer idAppManifiesto,
                                          String novedad,
                                          Double pesoGestor,
                                          String correG) {
        super(context);
        this.idAppManifiesto=idAppManifiesto;
        this.novedad = novedad;
        this.pesoGestor= pesoGestor;
        this.correG = correG;
    }

    @Override
    public void execute() {
        progressShow("Registrando...");
        listaFileDefauld = new ArrayList<>();
        model = MyApp.getDBO().lotePadreDao().fetchConsultarCatalogoEspecifico(idAppManifiesto);

        firmaRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_GESTORES, MyConstant.STATUS_GESTORES);
        //fotosGestores = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauldAllFotos(idAppManifiesto, ManifiestoFileDao.FOTO_NOVEDAD_GESTOR, MyConstant.STATUS_GESTORES);
        if(firmaRecoleccion!=null && !firmaRecoleccion.isSincronizado())listaFileDefauld.add(firmaRecoleccion);

        path = path + "/" + getPath() + "/" + model.getNumeroManifiestoPadre();
        System.out.println(path);

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
        userUploadFileTask.uploadGestoresAlternos(listaFileDefauld,idAppManifiesto);
    }

    private void register(){

        final RequestRegistroGenerador request = requestRegistroGenerador();
        if(request!=null) {
            Gson g = new Gson();
            String f = g.toJson(request);
            WebService.api().registrarRecoleccionGestores(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        //message(response.body().getMensaje());
                        progressHide();
                        if(mOnRegisterAlternoListener!=null){mOnRegisterAlternoListener.onSussfull();}
                    }else {
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

    RequestRegistroGenerador requestRegistroGenerador(){
        RequestRegistroGenerador rq = new RequestRegistroGenerador();

        if(model!=null) {
            rq.setNovedadEncontrada(novedad);
            rq.setPesoRecolectado(model.getTotal());
            rq.setPesoGestorAlterno(pesoGestor);
            rq.setFotoMPImagen("");
            rq.setFotoMPUrl(firmaRecoleccion != null ? (path + "/" + firmaRecoleccion.getUrl()) : "");
            rq.setFotoMPEstado(1);
            rq.setIdManifiestoPadre(idAppManifiesto);
            rq.setFotosNovedades(requestNovedadesGestores());
            rq.setCorreoGestorAlterno(correG);
        }
        return rq;
    }

    private List<RequestNovedadesGestores> requestNovedadesGestores(){
        List<RequestNovedadesGestores> resp = new ArrayList<>();
        List<RequestNovedadFoto> novedad = MyApp.getDBO().manifiestoFileDao().consultarFotografiasGestores(idAppManifiesto,4);
        if(novedad.size()>0){
            for(RequestNovedadFoto id:novedad){
                resp.add(new RequestNovedadesGestores(novedad,path+"/"+"ObservacionesRecoleccion"));
            }
        }
        return resp;

    }

    private List<RequestNovedadFoto> createFotografia(Integer tipo) {
        return MyApp.getDBO().manifiestoFileDao().consultarFotografiasGestores(idAppManifiesto,tipo);
    }

    public void setmOnRegisterAlternoListener(@Nullable onRegisterAlternoListener l){ mOnRegisterAlternoListener =l;}

    private String getPath() { return simpleDate.format(new Date());}
}
