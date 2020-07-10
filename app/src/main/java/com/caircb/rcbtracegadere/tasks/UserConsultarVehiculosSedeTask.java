package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestDataCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarVehiculosSedeTask extends MyRetrofitApi implements RetrofitCallbacks {
    public UserConsultarVehiculosSedeTask(Context context) {
        super(context);
    }

    public interface OnVehiculoListener {
        public void onSuccessful(List<DtoCatalogo> catalogos);
    }

    private OnVehiculoListener mOnVehiculoListener;
    private Integer tipoPlaca;

    @Override
    public void execute() {
        progressShow("Consultando vihiculos disponibles...");
        RequestDataCatalogo requestDataCatalogo = requestDataCatalogo();

        if (requestDataCatalogo!= null){

            WebService.api().obtenerCatalogoPlacasSede(requestDataCatalogo).enqueue(new Callback<List<DtoCatalogo>>() {
                @Override
                public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                    if(response.isSuccessful()){
                        MyApp.getDBO().catalogoDao().saveOrUpdate((List<DtoCatalogo>) response.body(),3);
                        List<DtoCatalogo> data = response.body();
                        for(DtoCatalogo c : response.body()){
                            MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+c.getId(),""+c.getDatoAdicional());
                        }

                        if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful((List<DtoCatalogo>) response.body());
                    }
                    progressHide();

                }

                @Override
                public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                    progressHide();
                }
            });

        }

    }

    RequestDataCatalogo requestDataCatalogo(){
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idDestinatario = Integer.parseInt(valor.equals("null") ? "-1":valor);

        RequestDataCatalogo rq = new RequestDataCatalogo();
        rq.setFecha(new Date());
        rq.setData(String.valueOf(idDestinatario));//lugar logeado
        rq.setDataAuxi("");
        rq.setTipo(3);

        return rq;
    }

    public void setOnVehiculoListener(@NonNull OnVehiculoListener l){
        mOnVehiculoListener = l;
    }
}
