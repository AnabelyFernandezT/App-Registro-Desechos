package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;

import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegistrarInicioRutaTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface OnIniciaRutaListener {
        public void onSuccessful();
        public void onFailure();
    }

    private OnIniciaRutaListener mOnIniciaRutaListener;
    private Long idRegistro;
    private RutaInicioFinEntity model;


    public UserRegistrarInicioRutaTask(Context context,long idRegistro) {
        super(context);
        this.idRegistro = idRegistro;
    }

    @Override
    public void execute() {
        register();
    }

    private void  register(){
        progressShow("Sincronizando con el servidor el inicio de ruta");
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(idRegistro.intValue());
        RequestIniciaRuta requestRutaIniciFin = createRequestInicio();
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
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                }
            });
        }
    }

    private RequestIniciaRuta createRequestInicio (){
        RequestIniciaRuta rq=null;
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(idRegistro.intValue());
        if (model!=null){
            rq =new RequestIniciaRuta();
            rq.setIdTransporteRecolector(model.getIdTransporteRecolector());
            rq.setIdTransporteVehiculo(model.getIdTransporteVehiculo());
            rq.setFechaDispositivo(model.getFechaInicio());
            rq.setKilometraje(model.getKilometrajeInicio());
            rq.setTipo(1);
        }
        return rq;
    }

    public void setOnIniciaRutaListener(@NonNull OnIniciaRutaListener l){
        mOnIniciaRutaListener =l;
    }


}
