package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarNoRecoleccion;

public class DialogMensajes extends MyDialog {
    Activity _activity;
    EditText txtMensaje;
    LinearLayout btnIngresarApp, btnCancelarApp;

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

        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);
        txtMensaje = getView().findViewById(R.id.txtMensaje);

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotificacionTask notificacionTask = new UserNotificacionTask(getContext(),null,txtMensaje.getText().toString());
                notificacionTask.execute();
            }
        });

    }
}
