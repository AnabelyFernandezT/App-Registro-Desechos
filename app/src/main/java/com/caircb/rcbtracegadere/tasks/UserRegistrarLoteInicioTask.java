package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestInicioLoteSede;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarLoteInicioTask extends MyRetrofitApi implements RetrofitCallbacks {
    public UserRegistrarLoteInicioTask(Context context) {
        super(context);
    }

    public interface OnRegisterListener {
        public void onSuccessful();
        public void onFail();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {

        WebService.api().registrarLoteinicio(new RequestInicioLoteSede(3,0,new Date())).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if (response.isSuccessful()){
                    progressHide();
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_inicio_lote",""+response.body().getMensaje());
                    if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
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
    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }
}
