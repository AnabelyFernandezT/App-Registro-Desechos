package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPendienteSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPendienteSede;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarManifiestoSedeLoteTask extends MyRetrofitApi implements RetrofitCallbacks {

    Integer idLote;
    Integer idManifiesto;

    public UserConsultarManifiestoSedeLoteTask(Context context, Integer idLote, Integer idManifiesto) {
        super(context);
        this.idLote = idLote;
        this.idManifiesto = idManifiesto;
    }
    public interface OnRegistro{
        public void onSuccessful();
    }

    private OnRegistro mOnRegistro;

    @Override
    public void execute(){

        RequestManifiestoPendienteSede request = requestManifiestoPendienteSede();

        if(request!=null) {
            progressShow("Consultando manifiestos faltantes");

            WebService.api().obtenerManifiestosPendientesSede(request).enqueue(new Callback<List<DtoManifiestoPendienteSede>>() {
                @Override
                public void onResponse(Call<List<DtoManifiestoPendienteSede>> call, Response<List<DtoManifiestoPendienteSede>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().size()>0) {
                            for (DtoManifiestoPendienteSede reg : response.body()) {
                                progressHide();
                                MyApp.getDBO().manifiestoSedePlantaDao().saveOrUpdate(idManifiesto, reg);
                            }

                            if (mOnRegistro != null) mOnRegistro.onSuccessful();
                        }else {
                            progressHide();
                            //message("No se encontraron datos");
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<DtoManifiestoPendienteSede>> call, Throwable t) {
                    progressHide();
                }
            });
        }
    }

    private RequestManifiestoPendienteSede requestManifiestoPendienteSede (){
        RequestManifiestoPendienteSede rq = new RequestManifiestoPendienteSede();
        rq.setIdLote(idLote);
        rq.setIdManifiesto(idManifiesto);
        rq.setIdsManifiestosCierre("");
        return rq;
    }

    public void setmOnRegistro (@NonNull OnRegistro l){
        mOnRegistro =l;
    }
}
