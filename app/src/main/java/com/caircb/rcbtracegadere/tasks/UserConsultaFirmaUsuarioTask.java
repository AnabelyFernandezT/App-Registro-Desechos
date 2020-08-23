package com.caircb.rcbtracegadere.tasks;

import android.content.Context;


import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestFirmaUsuario;
import com.caircb.rcbtracegadere.models.response.DtoFirmaUsuario;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaFirmaUsuarioTask extends MyRetrofitApi implements RetrofitCallbacks {

    int idTransportistaRecolector;

    public UserConsultaFirmaUsuarioTask(Context context,Integer idTransportistaRecolector) {
        super(context);
        this.idTransportistaRecolector=idTransportistaRecolector;
    }

    @Override
    public void execute()  {
        WebService.api().obtenerFirmaUsuario(new RequestFirmaUsuario(idTransportistaRecolector)).enqueue(new Callback<DtoFirmaUsuario>() {
            @Override
            public void onResponse(Call<DtoFirmaUsuario> call, Response<DtoFirmaUsuario> response) {
                progressShow("Cargando datos...");
                if (response.isSuccessful()) {
                    MyApp.getDBO().consultarFirmaUsuarioDao().saveOrUpdate(response.body());
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoFirmaUsuario> call, Throwable t) {
                progressHide();
            }
        });
    }
}
