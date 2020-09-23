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
        System.out.println(idSubRuta);
        if (!idSubRuta.equals("-1")) {
            WebService.api().traerCodigoQrTransportista(new RequestCodigoQrTransportista(Integer.parseInt(idSubRuta),idTransportistaRecolector,new Date())).enqueue(new Callback<DtoCodigoQrTransportista>() {
                @Override
                public void onResponse(Call<DtoCodigoQrTransportista> call, Response<DtoCodigoQrTransportista> response) {
                    progressShow("Cargando datos...");
                    if (response.isSuccessful()) {
                        if (!response.body().getCogigoQr().equals("")) {
                            MyApp.getDBO().codigoQrTransportistaDao().saveOrUpdate(response.body());
                            progressHide();
                            if (mOnCodigoQrListener != null) mOnCodigoQrListener.onSuccessful();
                        } else {
                            progressHide();
                            if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
                            //message("No existen lotes cerrados...");
                        }
                    } else {
                        progressHide();
                        if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
                        //message("No existen lotes cerrados...");
                    }
                }

                @Override
                public void onFailure(Call<DtoCodigoQrTransportista> call, Throwable t) {
                    progressHide();
                    if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
                }
            });
        } else {
            //message("No ha iniciado una Ruta...");
            if (mOnCodigoQrListener != null) mOnCodigoQrListener.onFailure();
        }

    }
    public void setOnCodigoQrListener(@NonNull UserConsultaCodigoQrValidadorTask.OnCodigoQrListener l) {
        mOnCodigoQrListener = l;
    }

}
