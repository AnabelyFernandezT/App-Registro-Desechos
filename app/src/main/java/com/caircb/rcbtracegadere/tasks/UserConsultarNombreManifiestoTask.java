package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestVisorManifiestoPdf;
import com.caircb.rcbtracegadere.models.response.DtoInfo;
import com.caircb.rcbtracegadere.services.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarNombreManifiestoTask extends MyRetrofitApi implements RetrofitCallbacks {

    Integer idAppManifiesto, estado;
    public interface OnNombreManifiestoListenner{
        public void onSuccessful(String nombrePdfManifiesto_url);
    }
    public OnNombreManifiestoListenner mOnNombreManifiestoListenner;

    public UserConsultarNombreManifiestoTask(Context context, Integer idAppManifiesto, Integer estado){
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.estado = estado;
    }

    @Override
    public void execute() {
        progressShow("Consultando...");

        WebService.api().obtenerUrlNombrePdfByIdManifiesto(new RequestVisorManifiestoPdf(idAppManifiesto, estado)).enqueue(new Callback<DtoInfo>() {
            @Override
            public void onResponse(Call<DtoInfo> call, Response<DtoInfo> response) {
                if(response.isSuccessful()){
                    if(mOnNombreManifiestoListenner!=null){mOnNombreManifiestoListenner.onSuccessful(response.body().getMensaje());}
                    progressHide();
                }else {
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoInfo> call, Throwable t) {
                progressHide();
            }
        });

    }

    public void setmOnNombreManifiestoListenner (@NonNull OnNombreManifiestoListenner l){
        mOnNombreManifiestoListenner = l;
    }
}
