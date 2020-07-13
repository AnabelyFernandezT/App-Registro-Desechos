package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.LoteHotelesEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestRegistarLotePadreHotel;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrarLoteHotelTask extends MyRetrofitApi implements RetrofitCallbacks {

    HotelLotePadreEntity lotePadreEntity;
    Integer idDestinoFinRutaCatalogo;


    public interface  onRegisterSuccesfullListener {
        public void onSucessfull ();
    }
    private onRegisterSuccesfullListener mOnRegisterSesscesullListener;

    public UserRegistrarLoteHotelTask(Context context, Integer idDestinoFinRutaCatalogo) {
        super(context);
        this.idDestinoFinRutaCatalogo = idDestinoFinRutaCatalogo;
    }

    @Override
    public void execute() {
        RequestRegistarLotePadreHotel request = requestRegistarLotePadreHotel();
        Gson gson = new Gson();
        String json = gson.toJson(request);
        System.out.println(json);

        if(request!=null){
            WebService.api().registrarHotelLote(request).enqueue(new Callback<DtoInfo>() {
                @Override
                public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                    if (response.isSuccessful()){
                        MyApp.getDBO().loteHotelesDao().updataMovilizado(Integer.parseInt(response.message()));
                        if(mOnRegisterSesscesullListener!=null)mOnRegisterSesscesullListener.onSucessfull();
                    }
                }

                @Override
                public void onFailure(Call<DtoInfo> call, Throwable t) {

                }
            });
        }

    }

    private RequestRegistarLotePadreHotel requestRegistarLotePadreHotel(){
        RequestRegistarLotePadreHotel rq = new RequestRegistarLotePadreHotel();
        lotePadreEntity = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());

        if(lotePadreEntity!=null){
            rq.setIdLoteContenedorHotel(lotePadreEntity.getIdLoteContenedorHotel());
            rq.setIdTransportistaRecolector(MySession.getIdUsuario());
            rq.setFecha(new Date());
            rq.setTipo(0);
            rq.setIdDestinatarioFinRutaCatalogo(idDestinoFinRutaCatalogo);

        }

        return rq;
    }

    public void setmOnRegisterSesscesullListener(onRegisterSuccesfullListener l){ mOnRegisterSesscesullListener= l;}

}
