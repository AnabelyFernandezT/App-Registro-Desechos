package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemLoteHoteles;
import com.caircb.rcbtracegadere.models.request.RequestLotesHoteles;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoLotesHoteles;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarLotesHotelesTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarLotesHotelesTask(Context context) {
        super(context);
    }

    public interface OnPlacaListener {
        public void onSuccessful(List<DtoLotesHoteles> catalogos);
    }

    private OnPlacaListener mOnVehiculoListener;

    @Override
    public void execute() {

        WebService.api().traerLotesHoteles(new RequestLotesHoteles(MySession.getIdUsuario(),new Date())).enqueue(new Callback<List<DtoLotesHoteles>>() {
            @Override
            public void onResponse(Call<List<DtoLotesHoteles>> call, Response<List<DtoLotesHoteles>> response) {
                if (response.isSuccessful()){
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                    MyApp.getDBO().loteHotelesDao().eliminarLotes();
                    MyApp.getDBO().loteHotelesDao().saveOrUpdate(response.body());
                   // for(DtoLotesHoteles reg:response.body()){
                    //}
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoLotesHoteles>> call, Throwable t) {
                    progressHide();

            }
        });

    }

}
