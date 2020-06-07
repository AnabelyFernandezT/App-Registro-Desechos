package com.caircb.rcbtracegadere.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;

import com.caircb.rcbtracegadere.MyApp;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestRutaIniciFin;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.models.response.DtoRutaInicioFin;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserGuardarInicioRutaTask extends MyRetrofitApi implements RetrofitCallbacks {
    ProgressDialog dialog;
    Cursor cursor;
    DtoRutaInicioFin DtoInicioFin = new DtoRutaInicioFin();

    Integer idInicioFin,idTransporteVehiculo,tipo;
    String kilometrajeInicio,kilometrajeFin;

    RutaInicioFinEntity model;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");

    public UserGuardarInicioRutaTask(Context context, Integer tipo) {
        super(context);
        this.tipo = tipo;
    }

    @Override
    public void execute() {

        register();

    }

    private void  register(){
        RequestRutaIniciFin requestRutaIniciFin = createRequestInicio();
        if(requestRutaIniciFin!=null){
            Gson g = new Gson();
            String f = g.toJson(requestRutaIniciFin);

            WebService.api().putInicioFin(requestRutaIniciFin).enqueue(new Callback<DtoInfo>() {
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

                }
            });

        }


    }

    private RequestRutaIniciFin createRequestInicio (){
        RequestRutaIniciFin rq=null;
         model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(model.getIdTransporteRecolector());
        if (model!=null){
            rq =new RequestRutaIniciFin();
            rq.setId("1");
            rq.setIdTransporteRecolector(model.getIdTransporteRecolector());
            rq.setIdTransporteVehiculo(model.getIdTransporteVehiculo());
            rq.setFechaDispositivo(model.getFechaInicio());
            rq.setKilometraje(model.getKilometrajeInicio());
            rq.setTipo(tipo);
        }

        return rq;
    }
    private String getPath() {
        return simpleDate.format(new Date());
    }


}
