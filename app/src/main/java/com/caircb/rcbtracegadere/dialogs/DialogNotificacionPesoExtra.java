package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;

import java.util.ArrayList;
import java.util.List;

public class DialogNotificacionPesoExtra extends MyDialog {
    Activity _activity;
    EditText txtMensaje;
    Integer idNotificacion;
    LinearLayout btnIngresarApp, btnCancelarApp;
    Spinner ltsNotificaciones;
    List<DtoCatalogo> catalogos;
    String novedad;
    Integer idManifiestoDetalle;
    Integer numeroManifiesto;
    Double pesoExtra;

    public DialogNotificacionPesoExtra(@NonNull Context context,Integer idManifiestoDetalle, Integer numeroManifiesto, Double pesoExtra) {
        super(context, R.layout.dialog_mensajes);
        this._activity = (Activity)context;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.numeroManifiesto= numeroManifiesto;
        this.pesoExtra= pesoExtra;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {

        catalogos = new ArrayList<>();
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);
        txtMensaje = getView().findViewById(R.id.txtMensaje);
        ltsNotificaciones = getView().findViewById(R.id.lista_notificacion);


        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotificacionTask notificacionTask = new UserNotificacionTask(getContext(),numeroManifiesto,
                                                            txtMensaje.getText().toString(),
                                                            idNotificacion,
                                                            String.valueOf(idManifiestoDetalle),pesoExtra);
                notificacionTask.setOnRegisterListener(new UserNotificacionTask.OnNotificacionListener() {
                    @Override
                    public void onSuccessful() {
                        DialogNotificacionPesoExtra.this.dismiss();
                    }
                });
                notificacionTask.execute();
            }
        });

        ltsNotificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    catalogos.get(position-1);
                    novedad = (String) ltsNotificaciones.getSelectedItem();
                    idNotificacion=3;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cargarNovedades();

    }

    public Spinner loadSpinner (Spinner spinner , List<DtoCatalogo> catalogos, boolean bhabilitar){

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

    private void cargarNovedades(){
        catalogos = MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipoId(3,7);

        loadSpinner(ltsNotificaciones,catalogos,true);
    }
}
