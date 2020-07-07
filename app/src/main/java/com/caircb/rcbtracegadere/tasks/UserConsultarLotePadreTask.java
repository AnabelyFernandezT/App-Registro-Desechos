package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestLotePadre;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreGestor;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarLotePadreTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarLotePadreTask(Context context) {
        super(context);
    }

    public interface OnPlacaListener {
        public void onSuccessful(List<DtoLotePadreGestor> catalogos);
    }

    private OnPlacaListener mOnVehiculoListener;

    @Override
    public void execute() {
            /***CAMBIAR PARAMETRO TRES DEL REQUEST ESTA UN DATO QUEMADO ***/
        WebService.api().traerLotesPadre(new RequestLotePadre(MySession.getIdUsuario(),new Date(),1)).enqueue(new Callback<List<DtoLotePadreGestor>>() {
            @Override
            public void onResponse(Call<List<DtoLotePadreGestor>> call, Response<List<DtoLotePadreGestor>> response) {
                if (response.isSuccessful()){
                    if(mOnVehiculoListener!=null)mOnVehiculoListener.onSuccessful(response.body());
                    MyApp.getDBO().lotePadreDao().eliminarLotes();
                    for(DtoLotePadreGestor reg:response.body()){
                        MyApp.getDBO().lotePadreDao().saveOrUpdate(reg);
                    }
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoLotePadreGestor>> call, Throwable t) {
                    progressHide();

            }
        });

    }

}
