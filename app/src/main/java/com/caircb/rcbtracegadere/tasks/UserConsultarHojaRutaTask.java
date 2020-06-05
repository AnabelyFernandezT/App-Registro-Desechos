package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoObservacionFrecuente;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserConsultarHojaRutaTask extends MyRetrofitApi implements RetrofitCallbacks {


    public interface TaskListener {
        public void onSuccessful();
    }
    private final TaskListener taskListener;


    public UserConsultarHojaRutaTask(Context context, TaskListener listener) {
        super(context);
        this.taskListener=listener;
        progressShow("Consultando...");
    }

    @Override
    public void execute() {

        WebService.api().getHojaRuta(new RequestHojaRuta("",12)).enqueue(new Callback<List<DtoManifiesto>>() {
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
                            List<DtoCatalogo> listaCatalogo =  MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(1);
                            for (DtoManifiesto reg:respuesta){
                                MyApp.getDBO().manifiestoDao().saveOrUpdate(reg);
                                for(DtoManifiestoDetalle dt:reg.getHojaRutaDetalle()) {
                                    MyApp.getDBO().manifiestoDetalleDao().saveOrUpdate(dt);
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
            public void onFailure(Call<List<DtoManifiesto>> call, Throwable t) {
                progressHide();
            }
        });
    }
}
