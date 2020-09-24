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
    Double pesoExtra;
    Integer idManifiesto;
    Integer idManifiestoDetalle;
    public UserEnviarCorreoComercialTask(Context context,
                                         Integer idManifiesto,
                                         Integer idManifiestoDetalle,
                                         Double pesoExtra) {
        super(context);
        this.pesoExtra = pesoExtra;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    @Override
    public void execute(){
        progressShow("Enviando correo...");
        final RequestNotificacionComercial request = requestNotificacionComercial();

        WebService.api().enviarCorreoComercial(request).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if (response.isSuccessful()){
                    progressHide();
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

    private RequestNotificacionComercial requestNotificacionComercial (){

        RequestNotificacionComercial rq =  new RequestNotificacionComercial();
        ManifiestoDetalleEntity detalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idManifiesto,idManifiestoDetalle);
        ManifiestoEntity entity = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);

        rq.setIdentificacion(entity.getIdentificacionCliente());
        rq.setNombreCliente(entity.getNombreCliente());
        rq.setManifiesto(entity.getNumeroManifiesto());
        rq.setPesoReferencial(String.valueOf(detalle.getPesoReferencial()));
        rq.setSucursal(entity.getSucursal());
        rq.setCodigoMae(detalle.getCodigoMAE() + ": " + detalle.getNombreDesecho());

        try {
            rq.setPesoSolicitado(""+ Math.round (((pesoExtra - detalle.getPesoReferencial()) * 100.00)) / 100.00);
        }
        catch (Exception e) {
            rq.setPesoSolicitado("" + pesoExtra);
        }

        Integer tpk = entity.getTipoPaquete();
        if(tpk==null|| tpk.toString().equals("")){
            rq.setFlagTipoPKG(0);
        }else {
            rq.setFlagTipoPKG(1);
        }

        return rq;
    }
}