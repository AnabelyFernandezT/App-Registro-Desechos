package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoQrPlanta;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarSedeQrTask extends MyRetrofitApi implements RetrofitCallbacks {

    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;

    public UserRegistrarSedeQrTask(Context context, RecepcionQrPlantaEntity recepcionQrPlantaEntity) {
        super(context);
        this.recepcionQrPlantaEntity = recepcionQrPlantaEntity;
    }

    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        final RequestManifiestoQrPlanta request = createRequestManifiestoQrPlanta();
        Gson g = new Gson();
        String f = g.toJson(request);
        System.out.println(f);

        if (request != null) {
            WebService.api().registroManifiestoQrPlanta(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getExito()) {
                            finalizar();
                            progressHide();
                        } else {
                            message(response.body().getMensaje());
                        }
                    } else {
                        message(response.body().getMensaje());
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {
                    progressHide();
                }
            });
        }
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l) {
        mOnRegisterListener = l;
    }

    private void finalizar() {
        if (mOnRegisterListener != null) mOnRegisterListener.onSuccessful();
    }

    private RequestManifiestoQrPlanta createRequestManifiestoQrPlanta() {
        Integer loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());
        RequestManifiestoQrPlanta rq = null;
        rq = new RequestManifiestoQrPlanta();
        rq.setNumeroManifiesto(recepcionQrPlantaEntity.getNumerosManifiesto());
        rq.setUrlFirmaPlanta("");
        rq.setFechaRecepcionPlanta(AppDatabase.getDateTime().toString());
        rq.setIdPlantaRecolector(MySession.getIdUsuario());
        rq.setObservacion("");
        rq.setTipoRecoleccion(3);
        rq.setNovedadFrecuentePlanta(null);
        rq.setFlagPlantaSede(2);
        rq.setIdLoteContenedor(loteContenedor);
        rq.setIdDestinatarioFinRutaCatalogo(0);
        return rq;
    }

}
