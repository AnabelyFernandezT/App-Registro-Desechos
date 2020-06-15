package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.services.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarRecolectadosTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface TaskListener {
        public void onSuccessful();

    }

    private final TaskListener taskListener;


    public UserConsultarRecolectadosTask(Context context, TaskListener taskListener){
        super(context);
        this.taskListener = taskListener;
        progressShow("Consultando...");
    }

    @Override
    public void execute() {

        WebService.api().getHojaRutaPlanta().enqueue(new Callback<List<DtoManifiesto>>() {
            @Override
            public void onResponse(Call<List<DtoManifiesto>> call, Response<List<DtoManifiesto>> response) {
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
                            for (DtoManifiesto reg:respuesta){
                                MyApp.getDBO().manifiestoDao().saveOrUpdate(reg);

                                pos++;
                                if(cont>1)publishProgress(pos);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            progressHide();
                            if(taskListener!=null)taskListener.onSuccessful();
                        }
                    }.execute();
                }else {
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
