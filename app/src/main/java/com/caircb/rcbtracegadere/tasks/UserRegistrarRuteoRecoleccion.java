package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.request.RequestRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarRuteoRecoleccion extends MyRetrofitApi implements RetrofitCallbacks {

    private RuteoRecoleccionEntity dtoRuteo;

    public interface OnRegisterRuteroRecoleecionListener {
        public void onSuccessful();
        public void onFail();
    }
    private UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener mOnRegisterRuteroRecoleecionListener;

    public UserRegistrarRuteoRecoleccion(Context context, RuteoRecoleccionEntity dtoRuteo){
        super(context);
        this.dtoRuteo = dtoRuteo;
    }

    @Override
    public void execute() {
        progressShow("Registrando...");
        RequestRuteoRecoleccion sendDto = null;
        if(dtoRuteo != null){
            sendDto = new RequestRuteoRecoleccion();
            sendDto.setIdSubRuta(dtoRuteo.getIdSubRuta());
            sendDto.setFechaInicioRuta(dtoRuteo.getFechaInicioRuta());
            sendDto.setPuntoPartida(dtoRuteo.getPuntoPartida());
            sendDto.setPuntoLlegada(dtoRuteo.getPuntoLlegada());
            sendDto.setFechaLlegadaRuta(dtoRuteo.getFechaLlegadaRuta());
            sendDto.setTipo(0);

            Gson g = new Gson();
            String f = g.toJson(sendDto);
            //if(mOnRegisterRuteroRecoleecionListener!=null)mOnRegisterRuteroRecoleecionListener.onSuccessful();
            //progressHide();

            WebService.api().registrarRuteoRecollecion(sendDto).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        if(response.body().getExito()) {
                            if(mOnRegisterRuteroRecoleecionListener!=null)mOnRegisterRuteroRecoleecionListener.onSuccessful();
                            progressHide();
                        }
                    }else{
                        progressHide();
                        //message(response.body().getMensaje());
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    message("No existe acceso al servidor");

                }
            });

        }
    }

    public void setOnRegisterRuteoRecollecionListenner (@NonNull OnRegisterRuteroRecoleecionListener l) {
        mOnRegisterRuteroRecoleecionListener = l;
    }
}
