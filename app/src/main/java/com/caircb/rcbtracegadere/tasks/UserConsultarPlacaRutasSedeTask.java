package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestDataCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarPlacaRutasSedeTask  extends MyRetrofitApi implements RetrofitCallbacks {
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");

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

        RequestDataCatalogo requestDataCatalogo = requestDataCatalogo();

        if(requestDataCatalogo!=null){
            WebService.api().obtenerCatalogoPlacasSede(requestDataCatalogo).enqueue(new Callback<List<DtoCatalogo>>() {
                @Override
                public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                    if(response.isSuccessful()){
                        MyApp.getDBO().catalogoDao().saveOrUpdate((List<DtoCatalogo>) response.body(),3);
                        if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful((List<DtoCatalogo>) response.body());

                    }else{
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                    progressHide();
                }
            });

        }


    }

    RequestDataCatalogo requestDataCatalogo(){
        RequestDataCatalogo rq = new RequestDataCatalogo();
        rq.setFecha(new Date());
        rq.setData("4");
        rq.setDataAuxi("");
        rq.setTipo(3);

        return rq;
    }
    public void setOnVehiculoListener(@NonNull OnVehiculoListener l){
        mOnVehiculoListener = l;
    }

}
