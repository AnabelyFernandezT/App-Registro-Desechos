package com.caircb.rcbtracegadere.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionModulos;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionTransportista;
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
    DialogInformacionTransportista dialogInformacionTransportista;
    int numero=0;

    public UserInformacionModulosTask(Context context, DialogInformacionModulos dialogInformacionModulos) {
        super(context);
        this.dialogInformacionModulos=dialogInformacionModulos;
        this.numero=1;
    }
    public UserInformacionModulosTask(Context context, DialogInformacionTransportista dialogInformacionTransportista) {
        super(context);
        this.dialogInformacionTransportista=dialogInformacionTransportista;
        this.numero=2;
    }


    @Override
    public void execute() {

        String tipoProcesoInfo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_info");
        String placaInfo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista");
        int idTransportistaInfo = MySession.getIdUsuario();
        //System.out.println(idTransportistaInfo + "---"+tipoProcesoInfo+"---"+placaInfo);
        AlertDialog.Builder builder;

        if (tipoProcesoInfo!=null && placaInfo!=null){
           // System.out.println(tipoProcesoInfo);
           // System.out.println(placaInfo);
            progressShow("Cargando datos...");

            WebService.api().traerInformacionModulos(new RequestInformacionModulos(new Date(), placaInfo, idTransportistaInfo, Integer.parseInt(tipoProcesoInfo))).enqueue(new Callback<List<DtoInformacionModulos>>() {
                @Override
                public void onResponse(Call<List<DtoInformacionModulos>> call,final Response<List<DtoInformacionModulos>> response) {
                    if(response.isSuccessful()){
                        if(response.body().size()!=0){
                            MyApp.getDBO().informacionModulosDao().saveOrUpdate(response.body());
                            if (numero==2){
                                dialogInformacionTransportista.show();
                            }else {
                                dialogInformacionModulos.show();
                            }
                            progressHide();
                        }else {
                            message("No hay datos para mostrar...");
                            progressHide();
                        }
                    }else {
                        progressHide();
                    }
                }

                @Override
                public void onFailure(Call<List<DtoInformacionModulos>> call, Throwable t) {
                    progressHide();
                }
            });
        }else {
            progressShow("Cargando datos...");
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
            progressHide();
        }
    }
}
