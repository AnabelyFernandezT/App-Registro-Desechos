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
    public interface onCountListaAsigandasListenner {
        public void onSuccesfull(Integer total);
    }

    private onCountListaAsigandasListenner mOnCountListaAsignaadasListeneer;


    @Override
    public void execute() {
        progressShow("Buscando información de hoteles...");
        WebService.api().traerLotesHoteles(new RequestLotesHoteles(MySession.getIdUsuario(),new Date())).enqueue(new Callback<List<DtoLotesHoteles>>() {
            @Override
            public void onResponse(Call<List<DtoLotesHoteles>> call, Response<List<DtoLotesHoteles>> response) {
                if (response.isSuccessful()){
                    //MyApp.getDBO().loteHotelesDao().eliminarLotes();
                    if(mOnCountListaAsignaadasListeneer!=null) mOnCountListaAsignaadasListeneer.onSuccesfull(response.body().size());
                    MyApp.getDBO().loteHotelesDao().saveOrUpdate(response.body());

                   // for(DtoLotesHoteles reg:response.body()){
                    //}
                    progressHide();
                }else {
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<List<DtoLotesHoteles>> call, Throwable t) {
                    progressHide();

            }
        });

    }

    public void setmOnCountListaAsignaadasListeneer(onCountListaAsigandasListenner l) {  mOnCountListaAsignaadasListeneer = l; }
}
