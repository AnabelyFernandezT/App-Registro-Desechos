package com.caircb.rcbtracegadere.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionModulos;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.request.RequestInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;
import com.caircb.rcbtracegadere.models.response.DtoLote;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.android.gms.common.util.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformacionModulosTask extends MyRetrofitApi implements RetrofitCallbacks {

    public interface TaskListener {
        public void onSuccessful();

    }
    DialogInformacionModulos dialogInformacionModulos;

    public UserInformacionModulosTask(Context context, DialogInformacionModulos dialogInformacionModulos) {
        super(context);
        this.dialogInformacionModulos=dialogInformacionModulos;
    }



    @Override
    public void execute() {

        String tipoProcesoInfo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_info");
        System.out.println(tipoProcesoInfo);

        String placaInfo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista");
        System.out.println(placaInfo);

        int idTransportistaInfo = MySession.getIdUsuario();
        System.out.println(idTransportistaInfo);
        AlertDialog.Builder builder;

        if (tipoProcesoInfo!=null && placaInfo!=null){
            WebService.api().traerInformacionModulos(new RequestInformacionModulos(new Date(), placaInfo, idTransportistaInfo, Integer.parseInt(tipoProcesoInfo))).enqueue(new Callback<List<DtoInformacionModulos>>() {

                AlertDialog.Builder builder2;
                @Override
                public void onResponse(Call<List<DtoInformacionModulos>> call,final Response<List<DtoInformacionModulos>> response) {
                    if(response.isSuccessful()){
                        if (response.body().size()!=0){
                            MyApp.getDBO().informacionModulosDao().saveOrUpdate(response.body());
                            dialogInformacionModulos.show();
                        }else {
                            builder2 = new AlertDialog.Builder(getContext());
                            builder2.setMessage("No hay datos para mostrar...");
                            builder2.setCancelable(false);
                            builder2.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder2.create();
                            dialog.show();
                        }
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<List<DtoInformacionModulos>> call, Throwable t) {
                    progressHide();

                }
            });
        }else {
            builder = new AlertDialog.Builder(getContext());
            builder.setMessage("No hay datos para mostrar...");
            builder.setCancelable(false);
            builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
