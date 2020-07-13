package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.request.RequestObtenerInicioFin;
import com.caircb.rcbtracegadere.models.response.DtoInicioRuta;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarInicioRutaTask extends MyRetrofitApi implements RetrofitCallbacks {
    private RutaInicioFinEntity model;

    public UserConsultarInicioRutaTask(Context context) {
        super(context);
    }

    @Override
    public void execute() {

        WebService.api().obtenerRutainicioFin(new RequestObtenerInicioFin(MySession.getIdUsuario(),new Date())).enqueue(new Callback<DtoInicioRuta>() {
            @Override
            public void onResponse(Call<DtoInicioRuta> call, Response<DtoInicioRuta> response) {
                System.out.println("Ruta inicio fin placa: " + response.body().getPlaca()+"--estado--"+response.body().getEstado()+"--Kilm--" + response.body().getKilometrajeInicio());
                if (response.isSuccessful()){
                    if (!verificarInicioRuta()){
                        MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(response.body().getIdRutaInicioFin(),
                                MySession.getIdUsuario(),
                                response.body().getIdSubRuta(),
                                new Date(),
                                null,
                                response.body().getKilometrajeInicio(),
                                response.body().getKilometrajeFin(),
                                response.body().getEstado(),
                                response.body().getPlaca());
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<DtoInicioRuta> call, Throwable t) {

            }
        });

    }

    private Boolean verificarInicioRuta (){
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());

        if(model!=null && model.getIdRutaInicioFin()>0){
            return true;
        }else {
            return false;
        }

    }
}
