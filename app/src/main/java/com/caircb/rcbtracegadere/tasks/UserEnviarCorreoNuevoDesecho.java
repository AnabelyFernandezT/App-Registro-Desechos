package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestEnviarCorreoNuevoDesecho;
import com.caircb.rcbtracegadere.models.request.RequestFinRuta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEnviarCorreoNuevoDesecho extends MyRetrofitApi implements RetrofitCallbacks {

    String identificacion;
    String nombreCliente;
    String sucursal;
    String numeroManifiesto;
    String mensajeDesecho;

    public UserEnviarCorreoNuevoDesecho(Context context,String identificacion,String nombreCliente,String sucursal,String numeroManifiesto,String mensajeDesecho) {
        super(context);
        this.identificacion=identificacion;
        this.nombreCliente=nombreCliente;
        this.sucursal=sucursal;
        this.numeroManifiesto=numeroManifiesto;
        this.mensajeDesecho=mensajeDesecho;
    }

    @Override
    public void execute()  {
        RequestEnviarCorreoNuevoDesecho requestEnviarCorreoNuevoDesecho = createRequestEnviarCorreoNuevoDesecho();
        if (requestEnviarCorreoNuevoDesecho != null) {
            progressShow("Enviando correo...");
            WebService.api().enviarCorreo(requestEnviarCorreoNuevoDesecho).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        progressHide();
                    }else{
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });
        }
    };

    private RequestEnviarCorreoNuevoDesecho createRequestEnviarCorreoNuevoDesecho() {
        RequestEnviarCorreoNuevoDesecho rq = null;
        rq = new RequestEnviarCorreoNuevoDesecho();
        rq.setIdentificacion(identificacion);
        rq.setNombreCliente(nombreCliente);
        rq.setSucursal(sucursal);
        rq.setManifiesto(numeroManifiesto);
        rq.setNombreDesecho(mensajeDesecho);
        rq.setFlagTipoPKG(0);
        return rq;
    }
}

