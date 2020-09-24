package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestCambioImpresora;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarCambioImpresoraIniciaRuta extends MyRetrofitApi implements RetrofitCallbacks {

    private Integer idImpresora;
    private Integer idInicioFinRuta;


    public UserRegistrarCambioImpresoraIniciaRuta(Context context,Integer idImpresora) {
        super(context);
        this.idImpresora=idImpresora;
    }

    public interface OnRegisterListener{
        public void onSuccessful();
    }
    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute(){
        RutaInicioFinEntity entity = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutas(MySession.getIdUsuario());
        idInicioFinRuta = entity.get_id();
        RequestCambioImpresora model = new RequestCambioImpresora(idInicioFinRuta,idImpresora);

        progressShow("Registrando..");
        WebService.api().registrarCambioImpresoraInicioRuta(model).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if(response.isSuccessful()){
                    if(response.body().getExito()){
                        progressHide();
                        if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
                    }
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

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}
