package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestInformacionTransportista;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.models.response.DtoInformacionTransportista;
import com.caircb.rcbtracegadere.services.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarInformacionTransportista extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarInformacionTransportista(Context context) {
        super(context);
    }

    @Override
    public void execute() {
        progressShow("Consultando...");
        WebService.api().informacionTransportista(new RequestInformacionTransportista(MySession.getIdUsuario())).enqueue(new Callback<DtoInformacionTransportista>() {
            @Override
            public void onResponse(Call<DtoInformacionTransportista> call, Response<DtoInformacionTransportista> response) {
                if(response.isSuccessful()){
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+response.body().getIdFinRutaCatalogo());
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info",""+response.body().getIdFinRutaCatalogo());
                    MySession.setDestinoEspecifico(response.body().getNombreCorto());
                    progressHide();
                }else{
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoInformacionTransportista> call, Throwable t) {
                progressHide();
            }
        });

    }
}
