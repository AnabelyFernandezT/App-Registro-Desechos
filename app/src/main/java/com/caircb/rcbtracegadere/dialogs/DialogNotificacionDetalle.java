package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
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

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrTask;
import com.caircb.rcbtracegadere.tasks.UserEnviarCorreoNuevoDesecho;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;

import java.util.ArrayList;
import java.util.List;

public class DialogNotificacionDetalle extends MyDialog {
    Activity _activity;
    EditText txtMensaje;
    Integer idNotificacion;
    LinearLayout btnIngresarApp, btnCancelarApp;
    Spinner ltsNotificaciones;
    List<DtoCatalogo> catalogos;
    String novedad;
    Integer idAppManifiesto;
    String identificacion;
    String nombreCliente;
    String sucursal;
    String numeroManifiesto;

    public DialogNotificacionDetalle(@NonNull Context context, Integer idAppManifiesto,String identificacion,String nombreCliente,String sucursal,String numeroManifiesto) {
        super(context, R.layout.dialog_mensajes);
        this._activity = (Activity)context;
        this.idAppManifiesto = idAppManifiesto;
        this.identificacion=identificacion;
        this.nombreCliente=nombreCliente;
        this.sucursal=sucursal;
        this.numeroManifiesto=numeroManifiesto;
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

        ltsNotificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    catalogos.get(position-1);
                    novedad = (String) ltsNotificaciones.getSelectedItem();
                    idNotificacion=2;

                    btnIngresarApp.setEnabled(true);
                }else{
                    btnIngresarApp.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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

                if(txtMensaje.getText().toString().equals("")){
                    messageBox("Debe ingresar la descripción del nuevo desecho");
                }else {
                    UserNotificacionTask notificacionTask = new UserNotificacionTask(getContext(), idAppManifiesto,
                            txtMensaje.getText().toString(),
                            idNotificacion,
                            "1", 0.0);
                    notificacionTask.setOnRegisterListener(new UserNotificacionTask.OnNotificacionListener() {
                        @Override
                        public void onSuccessful() {
                            DialogNotificacionDetalle.this.dismiss();

                            UserEnviarCorreoNuevoDesecho userEnviarCorreoNuevoDesecho = new UserEnviarCorreoNuevoDesecho(getContext(), identificacion, nombreCliente, sucursal, numeroManifiesto,txtMensaje.getText().toString());
                            userEnviarCorreoNuevoDesecho.execute();

                        }
                    });
                    notificacionTask.execute();
                }
            }
        });

        /*
        if(ltsNotificaciones.getSelectedItem().toString().equals("SELECCIONE")){
            btnIngresarApp.setEnabled(false);
        }else{
            btnIngresarApp.setEnabled(true);
        }

         */
    /*
        txtMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable texto) {

                if(!texto.toString().equals("")){
                    if(ltsNotificaciones.getSelectedItem().toString().equals("SELECCIONE")){
                        btnIngresarApp.setEnabled(false);
                    }else{
                        btnIngresarApp.setEnabled(true);
                    }
                }else{
                    btnIngresarApp.setEnabled(false);
                }
            }
        });
     */

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
        catalogos = MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipoId(2,7);

        loadSpinner(ltsNotificaciones,catalogos,true);
    }
}
