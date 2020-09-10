package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarNoRecoleccion;

import java.util.ArrayList;
import java.util.List;

public class DialogMensajes extends MyDialog {
    Activity _activity;
    EditText txtMensaje;
    Integer idNotificacion, idManifiesto;
    LinearLayout btnIngresarApp, btnCancelarApp;
    Spinner ltsNotificaciones;
    List<DtoCatalogo> catalogos;
    String novedad;
    ManifiestoEntity entity;

    public DialogMensajes(@NonNull Context context) {
        super(context, R.layout.dialog_mensajes);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {

        entity = MyApp.getDBO().manifiestoDao().fetchHojaRuta();

        catalogos = new ArrayList<>();
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);
        txtMensaje = getView().findViewById(R.id.txtMensaje);
        ltsNotificaciones = getView().findViewById(R.id.lista_notificacion);

        ltsNotificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    catalogos.get(position-1);
                    novedad = (String) ltsNotificaciones.getSelectedItem();
                    if(position==2){
                        position = position +2;
                    }
                    if(position==3){
                        position = position +3;
                    }
                    idNotificacion=position;

                    btnIngresarApp.setEnabled(true);
                }else{
                    btnIngresarApp.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cargarNovedades();

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(entity!=null){
                    idManifiesto = entity.getIdAppManifiesto();
                }else {
                    idManifiesto = 0;
                }
                UserNotificacionTask notificacionTask = new UserNotificacionTask(getContext(),idManifiesto,
                        txtMensaje.getText().toString(),
                        idNotificacion,
                        "1",0.0);
                notificacionTask.setOnRegisterListener(new UserNotificacionTask.OnNotificacionListener() {
                    @Override
                    public void onSuccessful() {
                        DialogMensajes.this.dismiss();
                    }
                });
                notificacionTask.execute();
            }
        });

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
        //catalogos = MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipoId(1,7);
        catalogos = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(7);
        loadSpinner(ltsNotificaciones,catalogos,true);
    }
}
