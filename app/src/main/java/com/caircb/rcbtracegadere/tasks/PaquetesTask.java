package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestPaquetes;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoPaquetes;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaquetesTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface TaskListener {
        public void onSuccessful();
    }
    private final TaskListener taskListener;

    public PaquetesTask(Context context, TaskListener listener) {
        super(context);
        this.taskListener = listener;

    }

    @Override
    public void execute() {

        WebService.api().getPaquetes().enqueue(new Callback<List<DtoPaquetes>>() {
            @Override
            public void onResponse(Call<List<DtoPaquetes>> call, Response<List<DtoPaquetes>> response) {
                if(response.isSuccessful()){

                    for (DtoPaquetes reg:response.body()){
                        MyApp.getDBO().paqueteDao().saveOrUpdate(reg);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DtoPaquetes>> call, Throwable t) {

            }
        });

    }
}