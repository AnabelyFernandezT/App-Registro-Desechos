package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestDataCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarPlacaRutasSedeTask  extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarPlacaRutasSedeTask(Context context) {
        super(context);
    }

    public interface OnVehiculoListener {
        public void onSuccessful(List<DtoCatalogo> catalogos);
    }

    private OnVehiculoListener mOnVehiculoListener;

    @Override
    public void execute() {
        progressShow("Consultando vihiculos disponibles...");

        WebService.api().obtenerCatalogoPlacasSede(new RequestDataCatalogo(3,"4",new Date(),"")).enqueue(new Callback<DtoCatalogo>() {
            @Override
            public void onResponse(Call<DtoCatalogo> call, Response<DtoCatalogo> response) {
                if(response.isSuccessful()){
                    MyApp.getDBO().catalogoDao().saveOrUpdate((List<DtoCatalogo>) response.body(),3);
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful((List<DtoCatalogo>) response.body());

                }else{
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoCatalogo> call, Throwable t) {
                progressHide();
            }
        });
    }
    public void setOnVehiculoListener(@NonNull OnVehiculoListener l){
        mOnVehiculoListener = l;
    }

}
