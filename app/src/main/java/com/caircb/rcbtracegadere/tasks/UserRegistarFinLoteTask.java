package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.LoteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestFinLote;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistarFinLoteTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer loteContenedor;
    Integer idDestino;
    Integer idTrasposteVehiLote;
    public UserRegistarFinLoteTask(Context context, Integer idTrasposteVehiLote) {
        super(context);
        this.idTrasposteVehiLote = idTrasposteVehiLote;
    }
    public interface OnRegisterListener {
        public void onSuccessful(String numeroLoteFin);
        public void onFail();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        final RequestFinLote requestFinLote = requestFinLote();

        if (requestFinLote!=null){
            WebService.api().registrarFinLote(requestFinLote).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                        if (response.body().getExito()){
                            progressHide();
                            MyApp.getDBO().parametroDao().saveOrUpdate("current_fin_lote",""+response.body().getMensaje());
                            if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful(response.body().getMensaje().toString());
                        }
                    }else {
                        progressHide();
                        if(mOnRegisterListener!=null)mOnRegisterListener.onFail();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {

                }
            });
        }
    }

    private RequestFinLote requestFinLote(){
        ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
        ParametroEntity destino = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico");
        LoteEntity  en = MyApp.getDBO().loteDao().fetchLotesCompletos();
        if(finLotes!=null){
            loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());
        }else {
            loteContenedor = en.getIdLoteContenedor();
        }

        if(destino!=null){
            idDestino = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());

        }



        RequestFinLote rq= new RequestFinLote();

        rq.setFecha(new Date());
        rq.setIdDestinatarioFinRutaCat(idDestino);//donde estoy
        rq.setIdLoteContenedor(loteContenedor);
        rq.setIdTransporteVehiculoLote(idTrasposteVehiLote);
        rq.setTipo(1);
        return rq;
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }
}
