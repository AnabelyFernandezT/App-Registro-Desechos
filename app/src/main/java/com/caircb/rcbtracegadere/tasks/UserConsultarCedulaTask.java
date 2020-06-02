package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.services.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarCedulaTask extends MyRetrofitApi implements RetrofitCallbacks {

    String identificacion;

    public interface OnResponseListener {
        public void onSuccessful(DtoIdentificacion identificacion);
        public void onFailure();
    }
    private OnResponseListener mOnResponseListener;

    public UserConsultarCedulaTask(Context context, String identificacion) {
        super(context);
        this.identificacion=identificacion;
    }

    @Override
    public void execute() {
        WebService.api().getIdentificacion(identificacion).enqueue(new Callback<DtoIdentificacion>() {
            @Override
            public void onResponse(Call<DtoIdentificacion> call, Response<DtoIdentificacion> response) {
                if(response.isSuccessful()){
                    if(mOnResponseListener!=null)mOnResponseListener.onSuccessful(response.body());
                }else{
                    if(mOnResponseListener!=null)mOnResponseListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<DtoIdentificacion> call, Throwable t) {
                if(mOnResponseListener!=null)mOnResponseListener.onFailure();
            }
        });
    }

    public void setOnResponseListener(@NonNull OnResponseListener l){
        mOnResponseListener=l;
    }
}
