package com.caircb.rcbtracegadere.tasks;

import android.content.Context;


import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarPlacasInicioRutaDisponible extends MyRetrofitApi implements RetrofitCallbacks {

    public interface TaskListener {
        public void onFinished(List<DtoCatalogo> catalogos);
    }

    private final TaskListener taskListener;

    public UserConsultarPlacasInicioRutaDisponible(Context context, TaskListener listener) {
        super(context);
        this.taskListener=listener;
    }

    @Override
    public void execute() {
        WebService.api().getCatalogos(new RequestCatalogo(5, "")).enqueue(new Callback<List<DtoCatalogo>>() {
            @Override
            public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                if(response.isSuccessful()) {
                    if(taskListener!=null)taskListener.onFinished(response.body());
                }else{
                    if(taskListener!=null)taskListener.onFinished(MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(4));
                }
            }

            @Override
            public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                if(taskListener!=null)taskListener.onFinished(MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(4));
            }
        });
    }
}
