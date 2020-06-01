package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestPaquetes;
import com.caircb.rcbtracegadere.models.response.DtoPaquetes;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaquetesTask extends MyRetrofitApi implements RetrofitCallbacks {

    public PaquetesTask(Context context) {
        super(context);
    }

    @Override
    public void execute() {

        WebService.api().getPaquetes().enqueue(new Callback<List<DtoPaquetes>>() {
            @Override
            public void onResponse(Call<List<DtoPaquetes>> call, Response<List<DtoPaquetes>> response) {
                if (response.isSuccessful()){
                    MyApp.getDBO().paqueteDao().saveOrUpdate(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<DtoPaquetes>> call, Throwable t) {

            }
        });
    }
}
