package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFinRuta;
import com.caircb.rcbtracegadere.models.request.RequestIniciaRuta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegistrarFinRutaTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface OnIniciaRutaListener {
        public void onSuccessful();
        public void onFailure();
    }

    private OnIniciaRutaListener mOnIniciaRutaListener;
    private Long idRegistro;
    private RutaInicioFinEntity model;


    public UserRegistrarFinRutaTask(Context context, long idRegistro) {
        super(context);
        this.idRegistro = idRegistro;
    }

    @Override
    public void execute() {
        register();
    }

    private void  register(){
        progressShow("Sincronizando con servidor el final de ruta");
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(idRegistro.intValue());
        RequestFinRuta requestFinRuta = createRequestFin();
        if(requestFinRuta!=null){
            WebService.api().putFin(requestFinRuta).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        MyApp.getDBO().rutaInicioFinDao().actualizarFinRutaToSincronizado(
                                idRegistro.intValue());
                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onSuccessful();
                    }else{
                        progressHide();
                        if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    if(mOnIniciaRutaListener!=null)mOnIniciaRutaListener.onFailure();
                }
            });
        }else {
            message("Usuario no asociado con Seguridad");
            progressHide();
        }
    }

    private RequestFinRuta createRequestFin(){
        RequestFinRuta rq=null;
        model = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if (model!=null){
            rq =new RequestFinRuta();
            rq.setId(model.getIdRutaInicioFin());
            rq.setIdTransporteRecolector(model.getIdTransporteRecolector());
            rq.setIdSubRuta(model.getIdSubRuta());
            rq.setFechaDispositivo(model.getFechaFin());
            rq.setKilometraje(model.getKilometrajeFin());
            rq.setTipo(2);
        }
        return rq;
    }

    public void setOnIniciaRutaListener(@NonNull OnIniciaRutaListener l){
        mOnIniciaRutaListener =l;
    }


}
