package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.DtoDetallesBultoPlanta;
import com.caircb.rcbtracegadere.models.DtoDetallesPlanta;
import com.caircb.rcbtracegadere.models.DtoFile;
import com.caircb.rcbtracegadere.models.DtoFotoPlanta;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDet;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoDetBultos;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPlanta;
import com.caircb.rcbtracegadere.models.request.RequestNovedadFoto;
import com.caircb.rcbtracegadere.models.request.RequestRegisterPlantaDetalle;
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

public class UserRegisterPlantaDetalleTask extends MyRetrofitApi implements RetrofitCallbacks {

    Integer idManifiesto;
    DtoFile firmaRecoleccion;
    String path ="planta";
    private List<DtoFile> listaFileDefauld;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    UserUploadFileTask userUploadFileTask;
    String observacion, numeroManifiesto;

    public interface onRegisterPlantaDetalleListenner{
        public void OnSucessfull();
    }

    private UserRegisterPlantaDetalleTask.onRegisterPlantaDetalleListenner mOnRegisterPlantaDetalleListener;

    public UserRegisterPlantaDetalleTask(Context context, Integer idManifiesto, String observacion, String numeroManifiesto){
        super(context);
        this.idManifiesto = idManifiesto;
        this.observacion = observacion;
        this.numeroManifiesto = numeroManifiesto;
    }

    @Override
    public void execute() {
        progressShow("Registrando...");
        listaFileDefauld = new ArrayList<>();
        firmaRecoleccion = MyApp.getDBO().manifiestoFileDao().consultarFiletoSendDefauld(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_ADICIONAL_PLANTA, MyConstant.STATUS_RECEPCION_PLANTA);
        if(firmaRecoleccion!=null && !firmaRecoleccion.isSincronizado())listaFileDefauld.add(firmaRecoleccion);

        path = path + "/" + getPath() + "/" + numeroManifiesto + "/Adicionales";
        userUploadFileTask= new UserUploadFileTask(getActivity(),path);
        userUploadFileTask.setOnUploadFileListener(new UserUploadFileTask.OnUploadFileListener() {
            @Override
            public void onSuccessful() { register(); }

            @Override
            public void onFailure(String message) {
                progressHide();
                message(message);
            }
        });
        userUploadFileTask.uploadPlantaAdicionales(listaFileDefauld, idManifiesto);
        System.out.println(path);

    }

    private void register(){
        final  RequestRegisterPlantaDetalle request = createRequestPlantaDetalle();
        Gson g = new Gson();
        String f = g.toJson(request);
        System.out.println(f);

        if(request !=null){
            WebService.api().registroManifiestoDetallePlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        if(response.body().getExito()){
                            if(mOnRegisterPlantaDetalleListener!=null){ mOnRegisterPlantaDetalleListener.OnSucessfull(); }
                        }
                    }
                    progressHide();
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });
        }
    }

    private String getPath() { return simpleDate.format(new Date());}

    private RequestRegisterPlantaDetalle createRequestPlantaDetalle(){
        RequestRegisterPlantaDetalle rq = null;
        rq = new RequestRegisterPlantaDetalle();
        rq.setIdManifiesto(idManifiesto);
        rq.setObservacion(observacion);
        rq.setDetalles(obtenerDetalles());
        rq.setFotos(obtenerFotos());
        rq.setUrlFima(firmaRecoleccion!=null?(path+"/"+firmaRecoleccion.getUrl()):"");

        return rq;
    }

    private List<DtoFotoPlanta> obtenerFotos(){
        List<DtoFotoPlanta> resp = new ArrayList<>();
        List<String> urlFotos = new ArrayList<>();
        List<RequestNovedadFoto> lista = MyApp.getDBO().manifiestoFileDao().consultarFotografias(idManifiesto,-1,ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA);

        if(lista.size()>0){
            for(RequestNovedadFoto reg: lista){
                urlFotos.add(reg.getUrlImagen());
            }
            resp.add(new DtoFotoPlanta(
                urlFotos,
                path+"/"+"novedadfrecuente"
            ));
        }

        return resp;
    }
    private List<DtoDetallesPlanta> obtenerDetalles (){
        /*List<DtoDetallesPlanta> detalles;
        DtoDetallesPlanta res = null;
        detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigDetalle(idManifiesto);
        List<DtoDetallesPlanta> listaDetalles = new ArrayList<>();
        if(detalles.size()>0){
            for(DtoDetallesPlanta reg: detalles){
                listaDetalles.add(new DtoDetallesPlanta(reg.getIdDetalle(), obtenerDetallesBultos(reg.getIdDetalle())));
                //listaDetalles.add(obtenerDetallesBultos(reg.getIdDetalle()));:
            }
        }
        return listaDetalles;*/
        List<DtoDetallesPlanta> resp= new ArrayList<>();
        List<Integer> det = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigDetalle(idManifiesto);
        if(det.size()>0){
            for (Integer d:det){
                resp.add( new DtoDetallesPlanta(
                        d,
                        obtenerDetallesBultos(d)
                ));
            }
        }
        return resp;
    }

    private List<DtoDetallesBultoPlanta> obtenerDetallesBultos (Integer idManifiestoDetalle){
        /*List<DtoDetallesBultoPlanta> detalles;
        detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigDetalleBultos(idManifiestoDetalle);
        List<DtoDetallesBultoPlanta> listaDetalles = new ArrayList<>();
        if(detalles.size()>0){
            for(DtoDetallesBultoPlanta reg: detalles){
                listaDetalles.add(new DtoDetallesBultoPlanta(reg.getIdDetalleValor(),
                        reg.getIdDetalleValor()));
                //listaDetalles.add(obtenerDetallesBultos(reg.getIdDetalle()));:
            }
        }
        return detalles;*/
        List<DtoDetallesBultoPlanta> resp = new ArrayList<>();
        List<DtoDetallesBultoPlanta> bultos = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigDetalleBultos(idManifiestoDetalle);
        if(bultos.size()>0){
            for (DtoDetallesBultoPlanta p:bultos){
                resp.add(new DtoDetallesBultoPlanta(
                        p.getIdDetalleValor(),
                        p.getPeso()
                ));
            }
        }
        return resp;
    }

    public void setmOnRegisterPlantaDetalleListener (@Nullable onRegisterPlantaDetalleListenner l){mOnRegisterPlantaDetalleListener = l;}
}
