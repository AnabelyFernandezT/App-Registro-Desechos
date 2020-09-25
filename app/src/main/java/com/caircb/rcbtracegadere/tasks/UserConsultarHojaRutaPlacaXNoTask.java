package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.request.RequestManifiestoSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserConsultarHojaRutaPlacaXNoTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface TaskListener {
        public void onSuccessful();
    }
    private final TaskListener taskListener;


    public UserConsultarHojaRutaPlacaXNoTask(Context context, TaskListener listener) {
        super(context);
        this.taskListener=listener;
        progressShow("Consultando...");
    }

    @Override
    public void execute() {

        Integer idDestinatario = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());
        WebService.api().traerManifiestosPlanta(new RequestManifiestoSede(idVehiculo,idDestinatario)).enqueue(new Callback<List<DtoManifiestoPlanta>>() {
            @Override
            public void onResponse(Call<List<DtoManifiestoPlanta>> call, final Response<List<DtoManifiestoPlanta>> response) {
                if(response.isSuccessful()){
                    final List<DtoManifiestoPlanta> respuesta = response.body();
                    final Integer cont =respuesta.size();
                     new AsyncTask<Void, Integer, Boolean>() {
                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            progressUpdateText("Sincronizando  [ "+values[0]+" de "+cont+" ]");
                        }

                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            Integer pos=0;
                            //List<DtoCatalogo> listaCatalogo =  MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(1);
                            if (respuesta.size()>0){
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_bandera_validacion", "0");
                            }
                            for (DtoManifiestoPlanta reg:respuesta){
                                MyApp.getDBO().manifiestoPlantaDao().saveOrUpdate(reg);
                                for (DtoManifiestoDetalleSede mdet:reg.getHojaRutaDetallePlanta()){
                                    MyApp.getDBO().manifiestoPlantaDetalleDao().saveOrUpdate(mdet);
                                    for(DtoManifiestoDetalleValorSede sedeVa : mdet.getHojaRutaDetalleValor()){
                                        MyApp.getDBO().manifiestoPlantaDetalleValorDao().saveOrUpdate(reg.getIdManifiesto(),sedeVa);
                                    }
                                }
                                //inicalizar los catalogos de recoleccion...
                                //for (DtoCatalogo c:listaCatalogo){
                                //    MyApp.getDBO().manifiestoObservacionFrecuenteDao().createRecoleccion(c,reg.getIdAppManifiesto());
                                //}
                                pos++;
                                if(cont>1)publishProgress(pos);
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            //super.onPostExecute(aBoolean);
                            progressHide();
                            if(taskListener!=null)taskListener.onSuccessful();
                        }
                    }.execute();
                    //ProgressHide();

                }else{
                    progressHide();
                }

            }

            @Override
            public void onFailure(Call<List<DtoManifiestoPlanta>> call, Throwable t) {
                progressHide();
            }
        });
    }
}
