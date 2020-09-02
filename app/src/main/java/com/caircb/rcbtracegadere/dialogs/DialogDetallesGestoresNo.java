package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.R;

public class DialogDetallesGestoresNo extends MyDialog {

    Activity _activity;
    EditText txtCantidadBultos, txtPesoBultos;
    LinearLayout btnCancelarApp,btnRegistrar;

    public DialogDetallesGestoresNo(@NonNull Context context) {
        super(context, R.layout.dialog_detalle_gestores_no);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
    }

    private void init() {

        txtCantidadBultos = getView().findViewById(R.id.txtCantidadBultos);
        txtPesoBultos = getView().findViewById(R.id.txtPesoBultos);
        btnRegistrar = getView().findViewById(R.id.btnRegistrar);
        btnCancelarApp = getView().findViewById(R.id.btnCancelarApp);




    }
}
