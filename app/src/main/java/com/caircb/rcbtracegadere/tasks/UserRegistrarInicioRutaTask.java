package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;

import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegistrarInicioRutaTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface OnIniciaRutaListener {
        public void onSuccessful();
        public void onFailure(int error);
    }

    private OnIniciaRutaListener mOnIniciaRutaListener;
    private Long idRegistro;
    private RutaInicioFinEntity model;
    private Integer idImpresora;
    Integer idVehiculo;
    Date fechaInicio;
    String kilometrajeInicio;
    Integer idTipoSubruta;

    public UserRegistrarInicioRutaTask(Context context, /*long idRegistro,*/ Integer idImpresora, Integer idVehiculo, Date fechaInicio, String kilometrajeInicio,Integer idTipoSubruta) {
        super(context);
        this.idImpresora = idImpresora;
        this.idVehiculo=idVehiculo;
        this.fechaInicio=fechaInicio;
        this.kilometrajeInicio=kilometrajeInicio;
        this.idTipoSubruta=idTipoSubruta;
        progressShow("Sincronizando con el servidor el inicio de ruta");
    }

    @Override
    public void execute() {
        register();
    }

    private void  register(){

        RequestIniciaRuta requestRutaIniciFin = createRequestInicio();
        /*Gson g = new Gson();
        String f = g.toJson(requestRutaIniciFin);*/

        if(requestRutaIniciFin!=null){
            WebService.api().putInicioFin(requestRutaIniciFin).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){

                        MyApp.getDBO().rutaInicioFinDao().actualizarInicioFinRutaToSincronizado(
                                idRegistro.intValue(),
                                Integer.parseInt(response.body().getMensaje()));

                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onSuccessful();
                    }else{
                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure(response.code());
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure(1);
                }
            });
        }else {
            message("Usuario no asociado con Seguridad");
            progressHide();
        }
    }

    private RequestIniciaRuta createRequestInicio (){
        Integer idUsuario=MySession.getIdUsuario();
        idRegistro =  MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(1, idUsuario,idVehiculo,fechaInicio,null,kilometrajeInicio,null,1,idTipoSubruta);
        RequestIniciaRuta rq=null;
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(idUsuario);

        if (model!=null){
            rq =new RequestIniciaRuta();
            rq.setIdTransporteRecolector(model.getIdTransporteRecolector());
            rq.setIdSubRuta(model.getIdSubRuta());
            rq.setFechaDispositivo(model.getFechaInicio());
            rq.setKilometraje(model.getKilometrajeInicio());
            rq.setTipo(1);
            rq.setIdTransportistaRecolector(model.getIdTransporteRecolector());
            rq.setIdImpresora(idImpresora);
        }
        return rq;
    }

    public void setOnIniciaRutaListener(@NonNull OnIniciaRutaListener l){
        mOnIniciaRutaListener =l;
    }


}
