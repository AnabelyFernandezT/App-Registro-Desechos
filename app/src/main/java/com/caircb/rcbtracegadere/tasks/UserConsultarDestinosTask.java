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

public class UserConsultarDestinosTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarDestinosTask(Context context) {
        super(context);
    }

    public interface OnDestinoListener {
        public void onSuccessful(List<DtoCatalogo> catalogos);
    }

    private OnDestinoListener onDestinoListener;

    @Override
    public void execute() {
        progressShow("Consultando destino disponibles...");

        WebService.api().getCatalogos(new RequestCatalogo(9,new Date())).enqueue(new Callback<List<DtoCatalogo>>() {
            @Override
            public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                if(response.isSuccessful()){
                    progressHide();
                    if(onDestinoListener!=null)onDestinoListener.onSuccessful(response.body());
                    MyApp.getDBO().catalogoDao().saveOrUpdate(response.body(), 9);
                }else {
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                progressHide();
            }
        });

    }

    public void setOnDestinoListener(@NonNull OnDestinoListener l){
        onDestinoListener = l;
    }
}
