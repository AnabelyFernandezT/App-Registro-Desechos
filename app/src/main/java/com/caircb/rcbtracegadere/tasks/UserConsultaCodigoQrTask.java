package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.dialogs.DialogQrLoteTransportista;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.request.RequestLote;
import com.caircb.rcbtracegadere.models.response.DtoCodigoQrTransportista;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaCodigoQrTask extends MyRetrofitApi implements RetrofitCallbacks {

    DialogQrLoteTransportista dialogQrLoteTransportista;

    public UserConsultaCodigoQrTask(Context context, DialogQrLoteTransportista dialogQrLoteTransportista) {
        super(context);
        this.dialogQrLoteTransportista = dialogQrLoteTransportista;
    }

    @Override
    public void execute() {

        String idSubRuta = MySession.getIdSubRuta() + "";
        System.out.println(idSubRuta);
        if (!idSubRuta.equals("-1")) {
            WebService.api().traerCodigoQrTransportista(new RequestCodigoQrTransportista(Integer.parseInt(idSubRuta))).enqueue(new Callback<DtoCodigoQrTransportista>() {
                @Override
                public void onResponse(Call<DtoCodigoQrTransportista> call, Response<DtoCodigoQrTransportista> response) {
                    progressShow("Cargando datos...");
                    if (response.isSuccessful()) {
                        if (response.body().getCogigoQr() != "") {
                            MyApp.getDBO().codigoQrTransportistaDao().saveOrUpdate(response.body());
                            dialogQrLoteTransportista.show();
                            progressHide();
                        } else {
                            progressHide();
                            message("No existen lotes cerrados...");
                        }
                    } else {
                        progressHide();
                        message("No existen lotes cerrados...");
                    }
                }

                @Override
                public void onFailure(Call<DtoCodigoQrTransportista> call, Throwable t) {
                    progressHide();

                }
            });
        } else {
            message("No ha iniciado una ruta, o si ya ha iniciado ruta por favor sincronice...");
        }

    }

}
