package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestNotificacionComercial;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEnviarCorreoComercialTask extends MyRetrofitApi implements RetrofitCallbacks {
    String pesoExtra;
    Integer idManifiesto;
    Integer idManifiestoDetalle;
    public UserEnviarCorreoComercialTask(Context context,
                                         Integer idManifiesto,
                                         Integer idManifiestoDetalle,
                                         String pesoExtra) {
        super(context);
        this.pesoExtra = pesoExtra;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    @Override
    public void execute(){
        final RequestNotificacionComercial request = requestNotificacionComercial();

        WebService.api().enviarCorreoComercial(request).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {

            }

            @Override
            public void onFailure(Call<DtoInfo> call, Throwable t) {

            }
        });


    }

    private RequestNotificacionComercial requestNotificacionComercial (){

        RequestNotificacionComercial rq =  new RequestNotificacionComercial();
        ManifiestoDetalleEntity detalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idManifiesto,idManifiestoDetalle);
        ManifiestoEntity entity = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);

        rq.setManifiesto(entity.getNumeroManifiesto());
        rq.setPesoReferencial(String.valueOf(detalle.getPesoReferencial()));
        rq.setPesoReferencial(pesoExtra);
        Integer tpk = entity.getTipoPaquete();
        if(tpk==null|| tpk.toString().equals("")){
            rq.setFlagTipoPKG(0);
        }else {
            rq.setFlagTipoPKG(1);
        }

        return rq;
    }


}
