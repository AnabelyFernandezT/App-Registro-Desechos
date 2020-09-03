package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.response.DtoImpresora;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaImpresora extends MyRetrofitApi implements RetrofitCallbacks{

    public interface TaskListener {
        public void onSuccessful();

    }

    private TaskListener mOnRegisterListener;
    public UserConsultaImpresora(Context context) {
        super(context);

    }


    @Override
    public void execute() {
        progressShow("Consultado datos...");

        WebService.api().traerImpresoras().enqueue(new Callback<List<DtoImpresora>>() {
            @Override
            public void onResponse(Call<List<DtoImpresora>> call, Response<List<DtoImpresora>> response) {
                if (response.isSuccessful()){
                    if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                    for(DtoImpresora reg:response.body()){
                        //MyApp.getDBO().loteDao().saveOrUpdate(reg);
                    }
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoImpresora>> call, Throwable t) {
                progressHide();

            }
        });
    }

    public void setOnRegisterListener(@NonNull TaskListener l){
        mOnRegisterListener =l;

    }

}
