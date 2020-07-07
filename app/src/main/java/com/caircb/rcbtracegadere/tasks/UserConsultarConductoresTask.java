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

public class UserConsultarConductoresTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarConductoresTask(Context context) {
        super(context);
    }

    public interface OnConductorListener {
        public void onSuccessful(List<DtoCatalogo> catalogos);
    }

    private OnConductorListener onConductorListener;

    @Override
    public void execute() {
        progressShow("Consultando conductores disponibles...");

        WebService.api().getCatalogos(new RequestCatalogo(14,new Date())).enqueue(new Callback<List<DtoCatalogo>>() {
            @Override
            public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                if(response.isSuccessful()){
                    progressHide();
                    if(onConductorListener!=null)onConductorListener.onSuccessful(response.body());
                    MyApp.getDBO().catalogoDao().saveOrUpdate(response.body(), 14);
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

    public void setOnConductorListener(@NonNull OnConductorListener l){
        onConductorListener = l;
    }
}
