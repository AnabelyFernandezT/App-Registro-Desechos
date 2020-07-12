package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestMovilizarLoteSede;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarLoteSedeTask extends MyRetrofitApi implements RetrofitCallbacks {
    private Integer destino,conductor,operador,operadorAuxiliar;

    public UserRegistrarLoteSedeTask(Context context, Integer destino,Integer conductor,Integer operador,Integer operadorAuxiliar) {
        super(context);
        this.destino = destino;
        this.conductor = conductor;
        this.operador = operador;
        this.operadorAuxiliar = operadorAuxiliar;
    }

    public interface OnRegisterListener {
        public void onSuccessful();
        public void onFail();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        RequestMovilizarLoteSede request = requestMovilizarLoteSede();
        if(request!=null){
            WebService.api().registrarmovilizacionLoteSede(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {

                    if(response.isSuccessful()){
                        progressHide();
                        if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
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

    private RequestMovilizarLoteSede requestMovilizarLoteSede(){
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idContenedor = Integer.parseInt(valor.equals("null") ? "-1":valor);

        ParametroEntity pa = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo_inicio_lote");
        String val = pa == null ? "-1" : pa.getValor();
        Integer vehiculo = Integer.parseInt(val.equals("null") ? "-1":val);

      //  Integer loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());

      //  Integer vehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo_inicio_lote").getValor());

        RequestMovilizarLoteSede rq = new RequestMovilizarLoteSede();
        rq.setIdLoteContenedor(idContenedor);//
        rq.setIdTransportistaVehiculo(vehiculo);
        rq.setIdTransportistaRecolector(conductor);
        rq.setIdOperador1(operador);
        rq.setIdOperador2(operadorAuxiliar);
        rq.setFecha(new Date());
        rq.setIdDestinatarioFinRutaCatalogo(destino);
        rq.setTipo(0);

        return rq;

    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }
}
