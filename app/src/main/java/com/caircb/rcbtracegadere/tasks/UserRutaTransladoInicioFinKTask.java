package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestNuevoKilometraje;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRutaTransladoInicioFinKTask extends MyRetrofitApi implements RetrofitCallbacks {

    String nuevoKilometraje;

    public UserRutaTransladoInicioFinKTask(Context context, String nuevoKilometraje) {
        super(context);
        this.nuevoKilometraje = nuevoKilometraje;
    }

    public interface OnRegisterListener{
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute()  {

        final RequestNuevoKilometraje request = requestNuevoKilometraje();

        if(request!=null){
            WebService.api().registroReasignacionVehiculo(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                        if (response.body().getExito()){
                            if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                        }

                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {

                }
            });
        }


    }

    private RequestNuevoKilometraje requestNuevoKilometraje (){
        RequestNuevoKilometraje rq = null;

        RutaInicioFinEntity model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("kilometraje_final_ant");

        if(model!=null && parametro!=null){
            rq = new RequestNuevoKilometraje();
            rq.setIdSubRuta(model.getIdSubRuta());
            rq.setNuevoKilometraje(nuevoKilometraje);
            rq.setKilometrajeFinal(parametro.getValor());
        }

        return rq;
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}
