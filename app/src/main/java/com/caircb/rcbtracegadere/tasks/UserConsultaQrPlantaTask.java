package com.caircb.rcbtracegadere.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestRecepcionQrPlanta;
import com.caircb.rcbtracegadere.models.response.DtoHojaRutaDetallePlantaLote;
import com.caircb.rcbtracegadere.models.response.DtoRecepcionQrPlanta;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultaQrPlantaTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface OnQrPlantaListener {
        public void onSuccessful();
    }

    private OnQrPlantaListener onQrPlantaListener;

    public UserConsultaQrPlantaTask(Context context) {
        super(context);
        progressShow("Consultando...");
    }

    @Override
    public void execute() throws ParseException {
        Integer idDestinatario = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());

        WebService.api().getHojaRutaQrPlanta(new RequestRecepcionQrPlanta(idDestinatario, idVehiculo)).enqueue(new Callback<DtoRecepcionQrPlanta>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<DtoRecepcionQrPlanta> call, Response<DtoRecepcionQrPlanta> response) {
                if (response.isSuccessful()) {
                    final DtoRecepcionQrPlanta respuesta = response.body();
                    new AsyncTask<Void, Integer, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            Integer pos = 0;
                            MyApp.getDBO().recepcionQrPlantaDao().saveOrUpdate(respuesta);
                            int contDetalle = 0;
                            for (DtoHojaRutaDetallePlantaLote mdet : respuesta.getHojaRutaDetallePlantaLote()) {
                                MyApp.getDBO().recepcionQrPlantaDetalleDao().saveOrUpdate(mdet, contDetalle);
                                contDetalle++;
                            }
                            pos++;

                            if (onQrPlantaListener != null) onQrPlantaListener.onSuccessful();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            //super.onPostExecute(aBoolean);
                            progressHide();
                        }
                    }.execute();

                } else {
                    progressHide();
                }
            }

            @Override
            public void onFailure(Call<DtoRecepcionQrPlanta> call, Throwable t) {
                progressHide();
            }
        });
    }

    public void setOnQrPlantaListener(@NonNull OnQrPlantaListener l) {
        onQrPlantaListener = l;
    }

}
