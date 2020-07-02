package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarCatalogosTask extends MyRetrofitApi implements RetrofitCallbacks {

    List<Integer> ids;
    public UserConsultarCatalogosTask(Context context, List<Integer> ids) {
        super(context);
        this.ids=ids;
    }

    @Override
    public void execute() {

        for (final Integer catalogoID:ids) {
            WebService.api().getCatalogos(new RequestCatalogo(catalogoID, new Date())).enqueue(new Callback<List<DtoCatalogo>>() {
                @Override
                public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                    if(response.isSuccessful()) {
                        MyApp.getDBO().catalogoDao().saveOrUpdate(response.body(), catalogoID);
                    }
                    else {
                        message("No hay catalogos  " + response);
                    }
                }

                @Override
                public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                    message(t +" No hay catalogos");

                }
            });
        }
    }

}
