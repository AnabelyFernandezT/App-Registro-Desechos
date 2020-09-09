package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestExcepcion;
import com.caircb.rcbtracegadere.models.request.RequestFinLote;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarExcepcionTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer loteContenedor;
    Integer idDestino;
    Integer idTrasposteVehiLote;

    public UserRegistrarExcepcionTask(Context context, Integer idTrasposteVehiLote) {
        super(context);
        this.idTrasposteVehiLote = idTrasposteVehiLote;
    }
    public interface OnRegisterListener {
        public void onSuccessful(String numeroLoteFin);
        public void onFail();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        final RequestExcepcion requestRegistrar = RequestExcepcion();

        if (requestRegistrar!=null){
            WebService.api().registrarExcepcion(requestRegistrar).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                        if (response.body().getExito()){
                            progressHide();
                            if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful(response.body().getMensaje().toString());
                        }
                    }else {
                        progressHide();
                        if(mOnRegisterListener!=null)mOnRegisterListener.onFail();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {

                }
            });
        }
    }

    private RequestExcepcion RequestExcepcion(){


        RequestExcepcion rq= new RequestExcepcion();

        return rq;
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }
}
