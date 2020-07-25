package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestInicioLoteHotel;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

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

    public interface OnRegisterListener {
        public void onSuccessful();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void execute() {
        final RequestInicioLoteHotel request = requestInicioLoteHotel();
        if(request!=null){
            Gson g = new Gson();
            String f = g.toJson(request);

           WebService.api().inicioFinLoteHotel(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if(response.isSuccessful()){
                        if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
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
       //RutaInicioFinEntity inicioFinRuta = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
       Integer inicioFinRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
       ManifiestoEntity entity = MyApp.getDBO().manifiestoDao().fetchHojaRutaEn(MySession.getIdUsuario());

       Integer idDestino =-1;
       String id = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor();
       if(!id.equals("")){
            idDestino = Integer.valueOf(id);
       }

       //Integer idDestino = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());

       if(lotePadre!=null){
           rq.setIdLoteContenedorHotel(lotePadre.getIdLoteContenedorHotel());
           rq.setIdSubRuta(inicioFinRuta);
           rq.setFecha(new Date());
           rq.setIdDestinatarioFinRutaCatalogo(idDestino);
           rq.setIdTransportistaVehiculo(entity.getIdTransporteVehiculo());
       }


        return rq;
   }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}
