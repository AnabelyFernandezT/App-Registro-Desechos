package com.caircb.rcbtracegadere.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.request.RequestHojaRuta;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalle;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoObservacionFrecuente;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserConsultarHojaRutaTask extends MyRetrofitApi implements RetrofitCallbacks {

    String fechaSincronizacion;
    String obfechaActualizacion = "fecha_actualizacion_"+MySession.getIdUsuario().toString()+"_"+MySession.getLugarNombre();

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
    public void execute() throws ParseException {

        ParametroEntity entity = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta");
        ParametroEntity fechaActualiza = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion);
        RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        String valor = entity == null ?(rut.getIdSubRuta()!=null?String.valueOf(rut.getIdSubRuta()):null) : entity.getValor();
        final Integer idRuta = valor==null?-1:Integer.parseInt(valor);
        if(fechaActualiza!=null){fechaSincronizacion = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion).getValor();
        }else fechaSincronizacion = null;
        Date fecha = deserialize(fechaSincronizacion);

        //Integer idRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        WebService.api().getHojaRuta(new RequestHojaRuta(new Date(),fecha,0,idRuta)).enqueue(new Callback<List<DtoManifiesto>>() {

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

                            if(respuesta != null && respuesta.size() > 0){
                                List<ItemManifiesto> checkItems = MyApp.getDBO().manifiestoDao().fetchManifiestosNoProcesados(idRuta, MySession.getIdUsuario());

                                for (ItemManifiesto it: checkItems){
                                    int flag = 0;
                                    for (DtoManifiesto reg:respuesta){
                                        if(it.getIdAppManifiesto() == reg.getIdAppManifiesto()){
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 0)
                                    {
                                        MyApp.getDBO().manifiestoDao().deleteNonSyncronizedManifiestosDet(it.getIdAppManifiesto());
                                        MyApp.getDBO().manifiestoDao().deleteNonSyncronizedManifiestos(it.getIdAppManifiesto());
                                    }
                                }
                            }

                            for (DtoManifiesto reg:respuesta){
                                MyApp.getDBO().manifiestoDao().saveOrUpdate(reg);
                                MyApp.getDBO().parametroDao().saveOrUpdate(obfechaActualizacion,reg.getFechaModificacion());
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

    final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Date deserialize(String json) throws JsonParseException {
        if(json!=null){
            try {
                String fecha = json;

                if(fecha.length()==19)
                    return df2.parse(fecha);
                else
                    return df1.parse(fecha);

            } catch (final java.text.ParseException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }

    }


}
