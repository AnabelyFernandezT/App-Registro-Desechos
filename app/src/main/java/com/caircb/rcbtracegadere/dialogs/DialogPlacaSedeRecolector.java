package com.caircb.rcbtracegadere.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosSedeTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacaRutasSedeTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;
import com.caircb.rcbtracegadere.tasks.UserConsultarVehiculosSedeTask;

import java.util.ArrayList;
import java.util.List;

public class DialogPlacaSedeRecolector extends MyDialog {
    Activity _activity;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    UserConsultaLotes consultarLotes;
    String placa;
    //UserConsultarPlacaRutasSedeTask consultarPlacasInicioRutaDisponible;
    UserConsultarVehiculosSedeTask consultarPlacas;
    LinearLayout btnIngresarApp, btnCancelarApp;
    UserConsultarManifiestosSedeTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;

    public interface onSincronizarListener{
        public void onSuccessfull();
    }
    public onSincronizarListener mOnSincronizarListener;

    public DialogPlacaSedeRecolector(@NonNull Context context) {
        super(context, R.layout.dialog_placa_sede);
        this._activity = (Activity)context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
    }

    private void init() {
        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignado);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);
        btnIngresarApp.setEnabled(false);
        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);
        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaPlacasDisponibles.get(position-1);
                    placa = (String) spinnerPlacas.getSelectedItem();
                    btnIngresarApp.setEnabled(true);
                }else{
                    btnIngresarApp.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btnIngresarApp.setEnabled(false);
            }
        });
        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlacaSedeRecolector.this.dismiss();

            }
        });

        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,3);
                int idVehiculo = c!=null?c.getIdSistema():-1;
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+idVehiculo);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+placa);
                //datosLotesDisponibles();
                cargarManifiesto();
                dismiss();
            }
        });

        datosPlacasDisponibles();
    }


    private void datosPlacasDisponibles(){
        consultarPlacas = new UserConsultarVehiculosSedeTask(getActivity(),1);
        consultarPlacas.setOnVehiculoListener(new UserConsultarVehiculosSedeTask.OnVehiculoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaPlacasDisponibles = catalogos;
                spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
            }
        });
        consultarPlacas.execute();
    }
    public Spinner cargarSpinnerPalca(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){

        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        listaData.add("SELECCIONE");
        if(catalogos.size() > 0){
            for (DtoCatalogo r : catalogos){
                listaData.add(r.getCodigo());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
        }
    };

    private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
    }

    @SuppressLint("SetTextI18n")
    private void cargarManifiesto(){
        consultarHojaRutaTask = new UserConsultarManifiestosSedeTask(_activity);
        consultarHojaRutaTask.setmOnVehiculoListener(new UserConsultarManifiestosSedeTask.OnPlacaListener() {
            @Override
            public void onSuccessful(List<DtoManifiestoSede> catalogos) {
                if(mOnSincronizarListener!=null){ mOnSincronizarListener.onSuccessfull();}
            }
        });
        consultarHojaRutaTask.execute();
        //lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
    }

    private void datosLotesDisponibles(){

        consultarLotes = new UserConsultaLotes(getActivity());
        consultarLotes.execute();
    }

    public void setmOnSincronizarListener (@Nullable onSincronizarListener l){ mOnSincronizarListener = l;}
}
