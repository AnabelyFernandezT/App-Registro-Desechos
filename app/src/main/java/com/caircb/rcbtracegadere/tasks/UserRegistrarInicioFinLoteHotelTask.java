package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestInicioLoteHotel;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarInicioFinLoteHotelTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer destinatarioInicioFinRutaCatalogo;

    public UserRegistrarInicioFinLoteHotelTask(Context context, Integer destinatarioInicioFinRutaCatalogo) {
        super(context);
        this.destinatarioInicioFinRutaCatalogo = destinatarioInicioFinRutaCatalogo;
    }

    @Override
    public void execute() {
        final RequestInicioLoteHotel request = requestInicioLoteHotel();
        if(request!=null){
            WebService.api().inicioFinLoteHotel(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {

                }
            });

        }

    }


   private RequestInicioLoteHotel requestInicioLoteHotel(){
       RequestInicioLoteHotel rq = new RequestInicioLoteHotel();
       HotelLotePadreEntity lotePadre = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());
       RutaInicioFinEntity inicioFinRuta = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
       ManifiestoEntity entity = MyApp.getDBO().manifiestoDao().fetchHojaRuta();

       Integer idDestino = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());

       rq.setIdLoteContenedorHotel(lotePadre.getIdLoteContenedorHotel());
       rq.setIdSubRuta(inicioFinRuta.getIdSubRuta());
       rq.setFecha(new Date());
       rq.setIdDestinatarioFinRutaCatalogo(idDestino);
       rq.setIdTransportistaVehiculo(entity.getIdTransporteVehiculo());

        return rq;
   }
}
