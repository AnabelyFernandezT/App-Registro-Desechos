package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestCambioImpresora;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarCambioImpresoraIniciaRuta extends MyRetrofitApi implements RetrofitCallbacks {

    private Integer idImpresora;


    public UserRegistrarCambioImpresoraIniciaRuta(Context context,Integer idImpresora) {
        super(context);
        this.idImpresora=idImpresora;
    }

    @Override
    public void execute() throws ParseException {
        RequestCambioImpresora model = new RequestCambioImpresora();

        WebService.api().registrarCambioImpresoraInicioRuta(model).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if(response.isSuccessful()){

                }else{

                }
            }

            @Override
            public void onFailure(Call<DtoInfo> call, Throwable t) {

            }
        });


    }
}
