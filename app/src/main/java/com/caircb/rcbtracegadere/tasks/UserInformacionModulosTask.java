package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformacionModulosTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface TaskListener {
        public void onSuccessful();

    }


    public UserInformacionModulosTask(Context context) {
        super(context);

    }

    @Override
    public void execute() {
        WebService.api().traerInformacionModulos(new RequestInformacionModulos(new Date(), "GST-3632", 2, 1)).enqueue(new Callback<List<DtoInformacionModulos>>() {

            @Override
            public void onResponse(Call<List<DtoInformacionModulos>> call, Response<List<DtoInformacionModulos>> response) {
                if (response.isSuccessful()){
                    for(DtoInformacionModulos reg:response.body()){
                        MyApp.getDBO().informacionModulosDao().saveOrUpdate(response.body());
                    }
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoInformacionModulos>> call, Throwable t) {
                progressHide();
            }
        });
    }
}
