package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarRutasTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarRutasTask(Context context) {
        super(context);
    }

    public interface OnPlacaListener {
        public void onSuccessful(List<DtoFindRutas> catalogos);

    }

    private OnPlacaListener mOnVehiculoListener;

    @Override
    public void execute() {
        System.out.println(MySession.getIdUsuario());
        System.out.println(new Date());
        WebService.api().traerRutas(new RequestFindRutas(MySession.getIdUsuario(), new Date())).enqueue(new Callback<List<DtoFindRutas>>() {
            @Override
            public void onResponse(Call<List<DtoFindRutas>> call, Response<List<DtoFindRutas>> response) {
                if (response.isSuccessful()){
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                    for(DtoFindRutas reg:response.body()){
                        MyApp.getDBO().rutasDao().saveOrUpdate(reg);

                    }

                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoFindRutas>> call, Throwable t) {
                progressHide();

            }
        });

    }

    public void setOnVehiculoListener(@NonNull OnPlacaListener l){
        mOnVehiculoListener = l;
    }
}
