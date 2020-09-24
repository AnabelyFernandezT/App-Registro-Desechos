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
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarVehiculosSedeTask extends MyRetrofitApi implements RetrofitCallbacks {
    public Integer banderaSede;

    public UserConsultarVehiculosSedeTask(Context context, Integer banderaSede) {
        super(context);
        this.banderaSede=banderaSede;
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
        /*Gson gson = new Gson();
        String json = gson.toJson(requestDataCatalogo);*/

        if (requestDataCatalogo!= null){

            WebService.api().obtenerCatalogoPlacasSede(requestDataCatalogo).enqueue(new Callback<List<DtoCatalogo>>() {
                @Override
                public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                    if(response.isSuccessful()){
                        MyApp.getDBO().catalogoDao().saveOrUpdate((List<DtoCatalogo>) response.body(),3);
                        List<DtoCatalogo> data = response.body();
                        for(DtoCatalogo c : response.body()){
                            MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+c.getId(),""+c.getDatoAdicional());
                            //MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info",""+c.getNombre());
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
        if (banderaSede==1){//Si recibe 1 es para validacion de consulta placas para sede
            rq.setTipo(5);
        }else {
            rq.setTipo(3);
        }


        return rq;
    }

    public void setOnVehiculoListener(@NonNull OnVehiculoListener l){
        mOnVehiculoListener = l;
    }
}
