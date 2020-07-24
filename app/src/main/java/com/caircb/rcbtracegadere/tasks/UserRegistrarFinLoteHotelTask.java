package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestFinLotePadreHotelTask;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarFinLoteHotelTask extends MyRetrofitApi implements RetrofitCallbacks {
    Integer loteContenedorPadre;
    HotelLotePadreEntity lotePadre;

    public UserRegistrarFinLoteHotelTask(Context context) {
        super(context);
        this.loteContenedorPadre = loteContenedorPadre;
    }

    @Override
    public void execute() {
        final RequestFinLotePadreHotelTask request = finLotePadreHotelTask();
        if(request!= null){
            WebService.api().registrarFinLotePadreHotel(request).enqueue(new Callback<DtoInfo>() {
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

    RequestFinLotePadreHotelTask finLotePadreHotelTask(){
        RequestFinLotePadreHotelTask rq = null;
        lotePadre = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());

        if(lotePadre!=null){
            rq = new RequestFinLotePadreHotelTask();
            rq.setIdLoteContenedorHotel(lotePadre.getIdLoteContenedorHotel());
            rq.setFechaRegistro(new Date());
            rq.setIdTransportistaRecolector(MySession.getIdUsuario());
            rq.setTipo(1);
        }


        return rq;
    }

}
