package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestImpresora;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.response.DtoImpresora;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaImpresoraTask extends MyRetrofitApi implements RetrofitCallbacks{

    String fecha;

    public interface TaskListener {
        public void onSuccessful();

    }

    private TaskListener mOnRegisterListener;
    public UserConsultaImpresoraTask(Context context) {
        super(context);
    }

    @Override
    public void execute() {
        //progressShow("Consultado datos...");
        //obtener fecha de sincronizacion...
        fecha = MyApp.getDBO().parametroDao().fecthParametroValor("fechaSincImpresoras");

        WebService.api().traerImpresoras(new RequestImpresora(fecha!=null?fecha:"")).enqueue(new Callback<List<DtoImpresora>>() {
            @Override
            public void onResponse(Call<List<DtoImpresora>> call, Response<List<DtoImpresora>> response) {
                if (response.isSuccessful()){
                    //if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                    for(DtoImpresora reg:response.body()){
                        MyApp.getDBO().impresoraDao().saveOrUpdateImpresora(reg);
                        fecha = reg.getFechaModificacion();
                    }
                    //actualizar fecha ultima sincronizacion...
                    MyApp.getDBO().parametroDao().saveOrUpdate("fechaSincImpresoras",fecha);
                    //progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoImpresora>> call, Throwable t) {
                //progressHide();
            }
        });
    }

    public void setOnRegisterListener(@NonNull TaskListener l){
        mOnRegisterListener =l;
    }

}
