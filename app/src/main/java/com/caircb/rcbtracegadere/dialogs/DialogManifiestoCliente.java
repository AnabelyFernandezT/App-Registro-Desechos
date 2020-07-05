package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyDialog;

public class DialogManifiestoCliente extends MyDialog {
    Activity _activity;
    EditText txtManifiestoCliente;
    LinearLayout btnIniciaRutaCancel,btnIniciaRutaAplicar;

    Manifiesto2Fragment principal = new Manifiesto2Fragment();
    Integer idManifiesto, tipoPaquete;

    public DialogManifiestoCliente(@NonNull Context context, Integer idManifiesto, Integer tipoPaquete) {
        super(context, R.layout.dialog_manifiesto_cliente);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.tipoPaquete = tipoPaquete;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {
        txtManifiestoCliente = getView().findViewById(R.id.txtManifiestoCliente);
        btnIniciaRutaAplicar = getView().findViewById(R.id.btnIniciaRutaAplicar);
        btnIniciaRutaCancel = getView().findViewById(R.id.btnIniciaRutaCancel);

        btnIniciaRutaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManifiestoCliente.this.dismiss();
            }
        });

        btnIniciaRutaAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtManifiestoCliente.getText().toString().equals("")){
                    messageBox("No se a ingresado numero de manifiesto Cliente");
                }else{
                    MyApp.getDBO().manifiestoDao().updateManifiestoCliente(idManifiesto,txtManifiestoCliente.getText().toString());
                    DialogManifiestoCliente.this.dismiss();
                    //principal.vistaPrevia(idManifiesto,tipoPaquete);
                }
            }
        });

    }
}
