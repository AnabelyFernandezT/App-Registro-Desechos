package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
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
    HotelLotePadreEntity lotePadre = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());

    public UserConsultaCodigoQrTask(Context context, DialogQrLoteTransportista dialogQrLoteTransportista) {
        super(context);
        this.dialogQrLoteTransportista = dialogQrLoteTransportista;
    }

    @Override
    public void execute() {

        String idSubRuta = MySession.getIdSubRuta() + "";
        Integer idTransportistaRecolector = MySession.getIdUsuario();
        if (idSubRuta.equals("-1")|| lotePadre!=null){
            idSubRuta="0";
        }
        /*if (!idSubRuta.equals("-1")) {*/
            progressShow("Cargando datos...");
            WebService.api().traerCodigoQrTransportista(new RequestCodigoQrTransportista(Integer.parseInt(idSubRuta),idTransportistaRecolector,new Date())).enqueue(new Callback<List<DtoCodigoQrTransportista>>() {
                @Override
                public void onResponse(Call<List<DtoCodigoQrTransportista>> call, Response<List<DtoCodigoQrTransportista>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().size()>0) {
                            final List<DtoCodigoQrTransportista> respuesta = response.body();
                            for(DtoCodigoQrTransportista reg:respuesta){
                               // MyApp.getDBO().codigoQrTransportistaDao().deleteTable();
                                MyApp.getDBO().codigoQrTransportistaDao().saveOrUpdate(reg);
                            }
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
                public void onFailure(Call<List<DtoCodigoQrTransportista>> call, Throwable t) {
                    progressHide();
                }
            });

        /*} else {
            message("No ha iniciado una ruta, o si ya ha iniciado ruta por favor sincronice...");
        }*/

    }

}
