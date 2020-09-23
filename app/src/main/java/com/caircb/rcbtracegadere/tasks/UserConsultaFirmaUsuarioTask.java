package com.caircb.rcbtracegadere.tasks;

import android.content.Context;


import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestFirmaUsuario;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFirmaUsuario;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaFirmaUsuarioTask extends MyRetrofitApi implements RetrofitCallbacks {

    int idTransportistaRecolector;
    public interface OnFirmaListener {
        public void onSuccessful();
    }

    private UserConsultaFirmaUsuarioTask.OnFirmaListener onFirmaListener;

    public UserConsultaFirmaUsuarioTask(Context context,Integer idTransportistaRecolector) {
        super(context);
        this.idTransportistaRecolector=idTransportistaRecolector;
    }

    @Override
    public void execute()  {
        progressShow("Cargando datos...");
        WebService.api().obtenerFirmaUsuario(new RequestFirmaUsuario(idTransportistaRecolector)).enqueue(new Callback<DtoFirmaUsuario>() {
            @Override
            public void onResponse(Call<DtoFirmaUsuario> call, Response<DtoFirmaUsuario> response) {
                if (response.isSuccessful()) {
                    MyApp.getDBO().consultarFirmaUsuarioDao().saveOrUpdate(response.body());
                    if(onFirmaListener!=null)onFirmaListener.onSuccessful();
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoFirmaUsuario> call, Throwable t) {
                progressHide();
            }
        });
    }
    public void setOnFirmaListener(@NonNull UserConsultaFirmaUsuarioTask.OnFirmaListener l){
        onFirmaListener = l;
    }
}
