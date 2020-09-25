package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestHotelPadre;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreGestor;
import com.caircb.rcbtracegadere.models.response.DtoLotePadreHotel;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserObtenerLotePadreHotelTask extends MyRetrofitApi implements RetrofitCallbacks {
    public UserObtenerLotePadreHotelTask(Context context) {
        super(context);
    }

    public interface OnLoteHotelPadreListener {
        public void onSuccessful();
    }
    private OnLoteHotelPadreListener mOnLoteHotelPadreListener;

    @Override
    public void execute() {
        progressShow("Consultando...");
        WebService.api().getLotePadreHotel(new RequestHotelPadre(MySession.getIdUsuario(),new Date())).enqueue(new Callback<DtoLotePadreHotel>() {
            @Override
            public void onResponse(Call<DtoLotePadreHotel> call, Response<DtoLotePadreHotel> response) {
                if(response.isSuccessful()){
                    MyApp.getDBO().hotelLotePadreDao().saveOrUpdare(response.body(),MySession.getIdUsuario());
                    if(mOnLoteHotelPadreListener!=null)mOnLoteHotelPadreListener.onSuccessful();
                    progressHide();
                }else{
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoLotePadreHotel> call, Throwable t) {
                progressHide();
            }
        });
    }

    public void setmOnLoteHotelPadreListener(@NonNull OnLoteHotelPadreListener l){
        mOnLoteHotelPadreListener =l;

    }
}
