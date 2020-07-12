package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioLoteTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;
import com.caircb.rcbtracegadere.tasks.UserRegistrarLoteInicioTask;

import java.util.ArrayList;
import java.util.List;

public class DialogPlacaSede extends MyDialog {
    Activity _activity;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    String placa;
    UserConsultarPlacasInicioLoteTask placasInicioLoteTask;
    LinearLayout btnIngresarApp, btnCancelarApp, lnlIniciaLote,lnlFinLote;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    UserRegistrarLoteInicioTask registrarLoteInicioTask;
    ImageButton btnFinLote;





    public DialogPlacaSede(@NonNull Context context) {
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
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);

        lnlIniciaLote = getActivity().findViewById(R.id.LnlIniciaLote);
        lnlFinLote = getActivity().findViewById(R.id.LnlFinLote);
        btnFinLote = (ImageButton) getActivity().findViewById(R.id.btnFinLote);

        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);
        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaPlacasDisponibles.get(position-1);
                    placa = (String) spinnerPlacas.getSelectedItem();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlacaSede.this.dismiss();

            }
        });

        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
                int idVehiculo = c!=null?c.getIdSistema():-1;
                System.out.println(c!=null?c.getNombre():-1);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo_inicio_lote",""+idVehiculo);
                registrarLote();
                dismiss();
            }
        });

        datosPlacasDisponibles();
    }


    private void datosPlacasDisponibles(){
        placasInicioLoteTask = new UserConsultarPlacasInicioLoteTask(getActivity());
        placasInicioLoteTask.setOnVehiculoListener(new UserConsultarPlacasInicioLoteTask.OnVehiculoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaPlacasDisponibles = catalogos;
                spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
            }
        });
        placasInicioLoteTask.execute();
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
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesada());
    }

    private void cargarManifiesto(){
        consultarHojaRutaTask = new UserConsultarHojaRutaPlacaTask(_activity,listenerHojaRuta);
        consultarHojaRutaTask.execute();
    }

    private void registrarLote(){

        registrarLoteInicioTask = new UserRegistrarLoteInicioTask(_activity);
        registrarLoteInicioTask.setOnRegisterListener(new UserRegistrarLoteInicioTask.OnRegisterListener() {
            @Override
            public void onSuccessful() {
                messageBox("Lote Registrado");
                activarFinLote();
            }

            @Override
            public void onFail() {
                messageBox("Lote no registrado");

            }
        });
        registrarLoteInicioTask.execute();
    }

    private void activarFinLote(){
        lnlIniciaLote.setVisibility(View.GONE);
        lnlFinLote.setVisibility(View.VISIBLE);
    }


}
