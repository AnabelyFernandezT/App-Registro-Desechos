package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarManifiestosPendientesPesarTask  extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarManifiestosPendientesPesarTask (Context context) {
        super(context);
    }

    public interface OnListasManifiestosPendientesistener {
        public void onSuccessful(List<DtoManifiestoPlanta> listaManifiestos);
    }

    private OnListasManifiestosPendientesistener mOnListasManifiestosPendientesistener;

    @Override
    public void execute() {
        progressShow("Sincronizando pendientes...");
        WebService.api().obtenerListaHojaRutaPlantaPendientesPeso().enqueue(new Callback<List<DtoManifiestoPlanta>>() {
            @Override
            public void onResponse(Call<List<DtoManifiestoPlanta>> call, Response<List<DtoManifiestoPlanta>> response) {
                if(response.isSuccessful()){

                    if(mOnListasManifiestosPendientesistener!=null)mOnListasManifiestosPendientesistener.onSuccessful(response.body());

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
    public void setmOnListasManifiestosPendientesistener(@Nullable OnListasManifiestosPendientesistener l) {
        mOnListasManifiestosPendientesistener = l;
    }
}
