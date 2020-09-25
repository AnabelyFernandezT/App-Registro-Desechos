package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestDetalleSede;
import com.caircb.rcbtracegadere.models.request.RequestRegistrarDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistarDetalleSedeTask extends MyRetrofitApi implements RetrofitCallbacks {
    public UserRegistarDetalleSedeTask(Context context) {
        super(context);
    }

    public interface OnRegisterListener {
        public void onSuccessful();
        public void onFail();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        final RequestRegistrarDetalleSede requestDetalle = requestRegistrarDetalleSede();
        if (requestDetalle!=null){
            Gson g = new Gson();
            String f = g.toJson(requestDetalle);
            progressShow("Registrando...");
            WebService.api().registrarDetalleRecolectado(requestDetalle).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.body().getExito()){
                        if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                        progressHide();
                    }else {
                        if(mOnRegisterListener!=null)mOnRegisterListener.onFail();
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    if(mOnRegisterListener!=null)mOnRegisterListener.onFail();
                    progressHide();

                }
            });
        }

    }

    private RequestRegistrarDetalleSede requestRegistrarDetalleSede (){
        Integer loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());

        RequestRegistrarDetalleSede rq = new RequestRegistrarDetalleSede();

        rq.setIdLoteContenedor(loteContenedor);
        //rq.setDetalles(registrarDetalleSede());
        rq.setIdManifiestoDetalleValor(registrarDetalleSede());


        return rq;
    }

    private List<Integer> registrarDetalleSede() {
        //List<RequestDetalleSede> detalle = new ArrayList<>();
        List<Integer> detallesede = MyApp.getDBO().manifiestoDetalleValorSede().fetchDetallesRecolectados();
        List<Integer> detalle = new ArrayList<>();
        if(detallesede.size()>0){
            for(Integer id:detallesede){
                detalle.add(id);
            }

        }
        return detalle;
    }
    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;

    }
}
