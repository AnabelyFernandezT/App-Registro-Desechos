package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoPendiente;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoPendienteSede;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarManifiestosPendientesSedeTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idManifiestoPadre, idLoteSede, idDestino;

    public UserRegistrarManifiestosPendientesSedeTask(Context context, Integer idManifiestoPadre, Integer idLoteSede, Integer idDestino) {
        super(context);
        this.idManifiestoPadre = idManifiestoPadre;
        this.idLoteSede = idLoteSede;
        this.idDestino = idDestino;
    }

    public interface OnRegistro{
        public void onSuccessful();
    }

    private OnRegistro mOnRegistro;

    @Override
    public void execute(){
        final RequestManifiestoPendienteSede request = requestManifiestoPendienteSede();
        /*Gson gson = new Gson();
        String json = gson.toJson(request);
        System.out.println(json);*/

        if (request!=null){
            progressShow("Enviando documento...");
            WebService.api().registrarManifiestoSedePlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        progressHide();
                        if (mOnRegistro != null) mOnRegistro.onSuccessful();
                    }
                    progressHide();
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });

        }


    }

    private RequestManifiestoPendienteSede requestManifiestoPendienteSede (){
        RequestManifiestoPendienteSede rq = new RequestManifiestoPendienteSede();

        rq.setIdLote(idLoteSede);
        rq.setIdManifiesto(idManifiestoPadre);
        rq.setIdsManifiestosCierre(manifiestos());
        rq.setIdAutorizacion(idDestino);

        return rq;
    }

    private String manifiestos (){
        String manifiestos = "";
        List<ItemManifiestoPendiente> idManifiesto = MyApp.getDBO().manifiestoSedePlantaDao().fetchManifiestoPendienteCheck(idManifiestoPadre);

        if(idManifiesto.size()>0){
            for(ItemManifiestoPendiente it:idManifiesto){
                manifiestos = it.getIdManifiesto() + ","+manifiestos;
            }
            manifiestos = manifiestos.substring(0,manifiestos.length()-1);
        }

        return manifiestos;
    }

    public void setmOnRegistro (@NonNull OnRegistro l){
        mOnRegistro =l;
    }
}
