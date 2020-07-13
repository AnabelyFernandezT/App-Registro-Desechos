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
import com.caircb.rcbtracegadere.models.response.DtoLotesHoteles;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

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
        System.out.println("Usuariioooo "+MySession.getIdUsuario());
        System.out.println("FEchaaa "+new Date());
        WebService.api().obtenerRutainicioFin(new RequestObtenerInicioFin(MySession.getIdUsuario(),new Date())).enqueue(new Callback<DtoInicioRuta>() {

            @Override
            public void onResponse(Call<DtoInicioRuta> call, Response<DtoInicioRuta> response) {
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+response.body().getPlaca());
                MyApp.getDBO().parametroDao().saveOrUpdate("estado_transporte",""+response.body().getEstado());
                System.out.println("DATOS: "+response.body().getPlaca()+"---"+response.body().getEstado());
                if (response.isSuccessful()){
                    if (!verificarInicioRuta()){
                        if(response.body().getIdRutaInicioFin()>0 ){
                            MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(response.body().getIdRutaInicioFin(),
                                    MySession.getIdUsuario(),
                                    response.body().getIdSubRuta(),
                                    new Date(),
                                    null,
                                    response.body().getKilometrajeInicio(),
                                    response.body().getKilometrajeFin(),
                                    1);
                        }
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

        if(model!=null){
            return true;
        }else {
            return false;
        }

    }
}
