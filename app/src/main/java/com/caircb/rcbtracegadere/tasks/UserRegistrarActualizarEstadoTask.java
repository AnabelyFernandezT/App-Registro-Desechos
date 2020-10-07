package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestActualizacionEstado;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarActualizarEstadoTask extends MyRetrofitApi implements RetrofitCallbacks {

     Integer idManifiesto;
     Integer idSubruta;


    public UserRegistrarActualizarEstadoTask(Context context, Integer idManifiesto, Integer idSubruta ) {
        super(context);
        this.idManifiesto = idManifiesto;
        this.idSubruta = idSubruta;

    }

    @Override
    public void execute(){
        final RequestActualizacionEstado request = requestActualizacionEstado();

        WebService.api().cambiarEstado(request).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if(response.isSuccessful()){

                }

            }

            @Override
            public void onFailure(Call<DtoInfo> call, Throwable t) {

            }
        });

    }

    private RequestActualizacionEstado requestActualizacionEstado(){
        RequestActualizacionEstado rq = new RequestActualizacionEstado();
        rq.setIdManifiesto(idManifiesto);
        rq.setIdSubruta(idSubruta);
        rq.setIdTransportistaRecolector(0);

        return rq;
    }
}
