package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.dialogs.DialogQrLoteTransportista;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaCodigoQrValidadorTask  extends MyRetrofitApi implements RetrofitCallbacks {


    public interface OnCodigoQrListener {
        public void onSuccessful();

        public void onFailure();
    }

    private UserConsultaCodigoQrValidadorTask.OnCodigoQrListener mOnCodigoQrListener;

    public UserConsultaCodigoQrValidadorTask(Context context) {
        super(context);
    }

    @Override
    public void execute() {

        String idSubRuta = MySession.getIdSubRuta() + "";
        Integer idTransportistaRecolector = MySession.getIdUsuario();
        if (idSubRuta.equals("-1")){
            idSubRuta="0";
        }
       /* if (!idSubRuta.equals("-1")) {*/
            progressShow("Cargando datos...");
        WebService.api().traerCodigoQrTransportista(new RequestCodigoQrTransportista(Integer.parseInt(idSubRuta),idTransportistaRecolector,new Date())).enqueue(new Callback<List<DtoCodigoQrTransportista>>() {
            @Override
            public void onResponse(Call<List<DtoCodigoQrTransportista>> call, Response<List<DtoCodigoQrTransportista>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size()>0) {
                        final List<DtoCodigoQrTransportista> respuesta = response.body();
                        for(DtoCodigoQrTransportista reg:respuesta){
                            MyApp.getDBO().codigoQrTransportistaDao().saveOrUpdate(reg);
                        }
                        progressHide();
                        if (mOnCodigoQrListener != null) mOnCodigoQrListener.onSuccessful();
                    } else {
                        progressHide();
                        if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
                    }
                } else {
                    progressHide();
                    if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<DtoCodigoQrTransportista>> call, Throwable t) {
                progressHide();
                if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
            }
        });
      /*  } else {
            //message("No ha iniciado una Ruta...");
            if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
        }*/

    }
    public void setOnCodigoQrListener(@NonNull UserConsultaCodigoQrValidadorTask.OnCodigoQrListener l) {
        mOnCodigoQrListener = l;
    }

}
