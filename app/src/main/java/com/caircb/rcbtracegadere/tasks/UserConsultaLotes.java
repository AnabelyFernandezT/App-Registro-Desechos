package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
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
    Integer loteContenedor;

    public interface TaskListener {
        public void onSuccessful();

    }

    private TaskListener mOnRegisterListener;
    public UserConsultaLotes(Context context) {
        super(context);

    }


    //idDestinatarioFinRuta sede, hotel

    @Override
    public void execute() {
        progressShow("Consultado datos...");
        ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote");

        if (finLotes!=null){
            loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote").getValor());
        }else{
            loteContenedor=0;
        }


        WebService.api().traerLotes(new RequestLote(MySession.getIdUsuario(),new Date())).enqueue(new Callback<List<DtoLote>>() {
            @Override
            public void onResponse(Call<List<DtoLote>> call, Response<List<DtoLote>> response) {
                if (response.isSuccessful()){
                    if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                    MyApp.getDBO().loteDao().deleteTableLote();
                    for(DtoLote reg:response.body()){
                        MyApp.getDBO().loteDao().saveOrUpdate(reg);

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

    public void setOnRegisterListener(@NonNull TaskListener l){
        mOnRegisterListener =l;

    }

}
