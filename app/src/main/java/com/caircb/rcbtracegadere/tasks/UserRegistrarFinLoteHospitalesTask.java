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
import com.google.gson.Gson;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegistrarFinLoteHospitalesTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface OnFinLoteListener {
        public void onSuccessful();

        public void onFailure();
    }

    private OnFinLoteListener mOnFinLoteListener;
    private Integer idSubtura;
    private Integer idDestinatarioFinLote;
    private Integer tipoFinLote;

    public UserRegistrarFinLoteHospitalesTask(Context context, Integer idSubruta, Integer idDestinatarioFinLote, Integer tipoFinLote) {
        super(context);
        this.idSubtura = idSubruta;
        this.idDestinatarioFinLote = idDestinatarioFinLote;
        this.tipoFinLote = tipoFinLote;
    }

    @Override
    public void execute() {
        progressShow("Sincronizando con servidor el final de ruta");
        RequestFinRuta requestFinRuta = createRequestFin();
        Gson g = new Gson();
        String f = g.toJson(requestFinRuta);
        System.out.println(f);
        if (requestFinRuta != null) {
            if (mOnFinLoteListener != null) mOnFinLoteListener.onSuccessful();
            WebService.api().putFinLoteHospitales(requestFinRuta).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()) {
                        progressHide();
                        if (mOnFinLoteListener != null) mOnFinLoteListener.onSuccessful();
                    } else {
                        progressHide();
                        if (mOnFinLoteListener != null) mOnFinLoteListener.onFailure();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                    if (mOnFinLoteListener != null) mOnFinLoteListener.onFailure();
                }
            });
            progressHide();
        } else {
            message("Request no encontrado");
            progressHide();
        }
    }


    private RequestFinRuta createRequestFin() {
        RequestFinRuta rq = null;
        rq = new RequestFinRuta();
        rq.setId(0);
        rq.setIdSubRuta(idSubtura);
        rq.setFechaDispositivo(new Date());
        rq.setKilometraje("");
        rq.setTipo(tipoFinLote);
        rq.setIdDestinatarioFinRutaCatalogo(idDestinatarioFinLote);
        return rq;
    }

    public void setOnFinLoteListener(@NonNull OnFinLoteListener l) {
        mOnFinLoteListener = l;
    }
}
