package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.gson.JsonParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConsultarCatalogosTask extends MyRetrofitApi implements RetrofitCallbacks {

    String fechaSincronizacion;
    String obfechaActualizacion = "fecha_actualizacion_catalogo"+ MySession.getIdUsuario().toString()+"_"+MySession.getLugarNombre();

    List<Integer> ids;
    public UserConsultarCatalogosTask(Context context, List<Integer> ids) {
        super(context);
        this.ids=ids;
        progressShow("Descargando catalogo...");
    }

    @Override
    public void execute() {

        ParametroEntity fechaActualiza = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion);
        if(fechaActualiza!=null){fechaSincronizacion = MyApp.getDBO().parametroDao().fetchParametroEspecifico(obfechaActualizacion).getValor();
        }else fechaSincronizacion = null;
        Date fecha = deserialize(fechaSincronizacion);


        for (final Integer catalogoID:ids) {
            WebService.api().getCatalogos(new RequestCatalogo(catalogoID, new Date())).enqueue(new Callback<List<DtoCatalogo>>() {
                @Override
                public void onResponse(Call<List<DtoCatalogo>> call, Response<List<DtoCatalogo>> response) {
                    if(response.isSuccessful()) {
                        //verifica si retorna catalogos..

                        //insertar catalogos obtenidos...
                        MyApp.getDBO().catalogoDao().saveOrUpdate(response.body(), catalogoID);
                        progressHide();
                    }
                    else {
                        message("No hay catalogos  " + response);
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<List<DtoCatalogo>> call, Throwable t) {
                    progressHide();
                    message(t +" No hay catalogos");

                }
            });
        }
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
