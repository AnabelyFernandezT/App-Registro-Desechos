package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;
import com.caircb.rcbtracegadere.services.WebService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserConsultarHojaRutaPlacaTask extends MyRetrofitApi implements RetrofitCallbacks {

    String fechaSincronizacion;
    String obfechaActualizacion = "fecha_actualizacion_"+ MySession.getIdUsuario().toString()+"_"+MySession.getLugarNombre();

    public interface TaskListener {
        public void onSuccessful();
    }
    private final TaskListener taskListener;


    public UserConsultarHojaRutaPlacaTask(Context context, TaskListener listener) {
        super(context);
        this.taskListener=listener;
        progressShow("Consultando...");
    }

    @Override
    public void execute() throws ParseException {
        ParametroEntity fechaActualiza = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion);
        if(fechaActualiza!=null){fechaSincronizacion = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion).getValor();
        }else fechaSincronizacion = null;

        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());

        WebService.api().getHojaRuta(new RequestHojaRuta(new Date(),fechaActutalizacion(fechaSincronizacion),idVehiculo,0)).enqueue(new Callback<List<DtoManifiesto>>() {
            @Override
            public void onResponse(Call<List<DtoManifiesto>> call, final Response<List<DtoManifiesto>> response) {
                if(response.isSuccessful()){
                    final List<DtoManifiesto> respuesta = response.body();
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
                            for (DtoManifiesto reg:respuesta){
                                MyApp.getDBO().manifiestoDao().saveOrUpdate(reg);
                                /*for(DtoManifiestoDetalle dt:reg.getHojaRutaDetalle()) {
                                    MyApp.getDBO().manifiestoDetalleDao().saveOrUpdate(dt);
                                }*/
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
            public void onFailure(Call<List<DtoManifiesto>> call, Throwable t) {
                progressHide();
            }
        });
    }

    private Date fechaActutalizacion (String fechaActualizacion) throws ParseException {
        if(fechaActualizacion!=null){
            final DateFormat fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            return fecha.parse(fechaActualizacion);
        }else {
            return null;
        }
    }
}
