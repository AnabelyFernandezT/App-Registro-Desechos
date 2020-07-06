package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarPlacasInicioLoteTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarPlacasInicioLoteTask(Context context) {
        super(context);
    }

    public interface OnVehiculoListener {
        public void onSuccessful(List<DtoCatalogo> catalogos);
    }

    private OnVehiculoListener mOnVehiculoListener;

    @Override
    public void execute() {

        progressShow("Consultando vihiculos disponibles...");

        WebService.api().getCatalogos(new RequestCatalogo(13, new Date())).enqueue(new Callback<List<DtoCatalogo>>() { //13 catalogo placas trailer
            @Override
            public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                if(response.isSuccessful()) {
                    progressHide();
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                }else{
                    progressHide();
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(4));
                }
            }

            @Override
            public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                progressHide();
                if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(4));
            }
        });


    }
    public void setOnVehiculoListener(@NonNull OnVehiculoListener l){
        mOnVehiculoListener = l;
    }

}
