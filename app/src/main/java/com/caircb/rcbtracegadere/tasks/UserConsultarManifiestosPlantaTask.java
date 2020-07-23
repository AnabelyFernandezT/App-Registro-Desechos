package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarManifiestosPlantaTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarManifiestosPlantaTask(Context context) {
        super(context);
    }

    public interface OnPlacaListener {
        public void onSuccessful(List<DtoManifiestoPlanta> catalogos);
    }

    private OnPlacaListener mOnVehiculoListener;

    @Override
    public void execute() {

        Integer idDestinatario = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());

        WebService.api().traerManifiestosPlanta(new RequestManifiestoSede(idVehiculo,idDestinatario)).enqueue(new Callback<List<DtoManifiestoPlanta>>() {
            @Override
            public void onResponse(Call<List<DtoManifiestoPlanta>> call, Response<List<DtoManifiestoPlanta>> response) {
                if (response.isSuccessful()){
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                    //MyApp.getDBO().manifiestoPlantaDao().eliminarManifiestos();
                    //MyApp.getDBO().manifiestoPlantaDetalleDao().eliminarDetalle();
                    //MyApp.getDBO().manifiestoPlantaDetalleValorDao().eliminarDetalle();

                    for(DtoManifiestoPlanta reg:response.body()){
                        MyApp.getDBO().manifiestoPlantaDao().saveOrUpdate(reg);
                        for (DtoManifiestoDetalleSede mdet:reg.getHojaRutaDetallePlanta()){
                            MyApp.getDBO().manifiestoPlantaDetalleDao().saveOrUpdate(mdet);
                            for(DtoManifiestoDetalleValorSede sedeVa : mdet.getHojaRutaDetalleValor()){
                                MyApp.getDBO().manifiestoPlantaDetalleValorDao().saveOrUpdate(reg.getIdManifiesto(),sedeVa);
                            }
                        }
                    }
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoManifiestoPlanta>> call, Throwable t) {
                    progressHide();

            }
        });

    }

}
