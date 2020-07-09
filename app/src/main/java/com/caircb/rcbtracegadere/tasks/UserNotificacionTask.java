package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestNotificacion;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNotificacionTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer idAppManifiesto;
    String mensaje;

    public UserNotificacionTask(Context context,
                                Integer idAppManifiesto,
                                String mensaje) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.mensaje = mensaje;
    }

    @Override
    public void execute() {

        register();
    }

    private void register() {
        progressShow("Enviado Mensaje");
        RequestNotificacion request = createRequestNotificador();
        if(request!= null){
            Gson g = new Gson();
            String f = g.toJson(request);
            WebService.api().registrarNotificacion(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        if(response.body().getExito()){
                            message("Enviado");
                            progressHide();
                        }else{
                            message(response.body().getMensaje());
                            progressHide();
                        }
                        progressHide();
                    }else {
                        progressHide();
                        message(response.body().getMensaje());

                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });
        }

    }

    private RequestNotificacion createRequestNotificador() {
        RequestNotificacion rq = new RequestNotificacion();

        rq.setIdReceptor(518);
        rq.setMensaje(mensaje);
        rq.setIdEmisor(MySession.getIdUsuario());
        rq.setToken(MySession.getId());
        //rq.setToken(MySession.getId());
        rq.setIdManifiesto(1);
        rq.setIdHojaRuta("1");
        rq.setIdCatNotificacion(1);


        return rq;
    }


}
