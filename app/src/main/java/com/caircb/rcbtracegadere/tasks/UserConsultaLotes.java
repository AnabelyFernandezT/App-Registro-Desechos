package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFindRutas;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaLotes extends MyRetrofitApi implements RetrofitCallbacks{

    public interface TaskListener {
        public void onSuccessful();

    }


    public UserConsultaLotes(Context context) {
        super(context);

    }


    @Override
    public void execute() {
        WebService.api().traerLotes(new RequestLote(1, new Date())).enqueue(new Callback<List<DtoLote>>() {
            @Override
            public void onResponse(Call<List<DtoLote>> call, Response<List<DtoLote>> response) {
                if (response.isSuccessful()){
                    for(DtoLote reg:response.body()){
                        MyApp.getDBO().loteDao().saveOrUpdate(response.body());
                    }
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoLote>> call, Throwable t) {
                progressHide();

            }
        });
    }

}
