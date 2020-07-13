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
        WebService.api().obtenerRutainicioFin(new RequestObtenerInicioFin(MySession.getIdUsuario(),new Date())).enqueue(new Callback<List<DtoInicioRuta>>() {

            @Override
            public void onResponse(Call<List<DtoInicioRuta>> call, Response<List<DtoInicioRuta>> response) {
                System.out.println("PLACAAAA "+response.body().get(0).getPlaca());
                if (response.isSuccessful()){
                    if (!verificarInicioRuta()){
                        if(response.body().get(0).getIdRutaInicioFin()>0 ){
                            MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(response.body().get(0).getIdRutaInicioFin(),
                                    MySession.getIdUsuario(),
                                    response.body().get(0).getIdSubRuta(),
                                    new Date(),
                                    null,
                                    response.body().get(0).getKilometrajeInicio(),
                                    response.body().get(0).getKilometrajeFin(),
                                    1);
                        }
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<List<DtoInicioRuta>> call, Throwable t) {

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
