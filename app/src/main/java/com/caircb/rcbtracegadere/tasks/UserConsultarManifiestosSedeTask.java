package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFindRutas;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarManifiestosSedeTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarManifiestosSedeTask(Context context) {
        super(context);
    }

    public interface OnPlacaListener {
        public void onSuccessful(List<DtoManifiestoSede> catalogos);
    }

    private OnPlacaListener mOnVehiculoListener;

    @Override
    public void execute() {
        progressShow("Cargando datos...");
        Integer idDestinatario = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());

        WebService.api().traerManifiestos(new RequestManifiestoSede(idVehiculo,idDestinatario)).enqueue(new Callback<List<DtoManifiestoSede>>() {
            @Override
            public void onResponse(Call<List<DtoManifiestoSede>> call, Response<List<DtoManifiestoSede>> response) {
                if (response.isSuccessful()){

                    //MyApp.getDBO().manifiestoSedeDao().eliminarManifiestos();
                    //MyApp.getDBO().manifiestoDetalleSede().eliminarDetalle();
                    //MyApp.getDBO().manifiestoDetalleValorSede().eliminarDetalle();

                    for(DtoManifiestoSede reg:response.body()){
                        MyApp.getDBO().manifiestoSedeDao().saveOrUpdate(reg);
                        for (DtoManifiestoDetalleSede mdet:reg.getHojaRutaDetalle()){
                            MyApp.getDBO().manifiestoDetalleSede().saveOrUpdate(mdet);
                            for(DtoManifiestoDetalleValorSede sedeVa : mdet.getHojaRutaDetalleValor()){
                                MyApp.getDBO().manifiestoDetalleValorSede().saveOrUpdate(sedeVa);
                            }
                        }
                    }

                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoManifiestoSede>> call, Throwable t) {
                    progressHide();

            }
        });

    }

    public void setmOnVehiculoListener(@Nullable OnPlacaListener l) {  mOnVehiculoListener = l;}
}
