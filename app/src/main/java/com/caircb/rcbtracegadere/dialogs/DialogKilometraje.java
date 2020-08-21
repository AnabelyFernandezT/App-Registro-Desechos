package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;

public class DialogKilometraje extends MyDialog {
    Activity _activity;

    EditText txtKilometrajeFinal;
    TextView txtKilometrajeInicio, txtPlaca, txtPlaca1;

    LinearLayout btnCancelar,btnSiguiente;

    DialogNuevoKilometraje nuevoKilometraje;

    String kilometrajeFinalAnterior, kilometrajeinicial;

    RutaInicioFinEntity entity;
    ManifiestoEntity manifiesto;

    public DialogKilometraje(@NonNull Context context) {
        super(context, R.layout.dialog_kilometraje);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {
        entity = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if(entity!=null){
            MyApp.getDBO().parametroDao().saveOrUpdate("kilometraje_anterior"+entity.getIdRutaInicioFin(),entity.getKilometrajeInicio());
            kilometrajeinicial = entity.getKilometrajeInicio();
        }
        txtKilometrajeFinal = getView().findViewById(R.id.kilometrajeFinal);
        txtKilometrajeInicio = getView().findViewById(R.id.txt_kilometraje_inicio);
        txtPlaca = getView().findViewById(R.id.txtPlaca);
        txtPlaca1 = getView().findViewById(R.id.txtPlaca1);
        btnCancelar =  getView().findViewById(R.id.btnCancelar);
        btnSiguiente = getView().findViewById(R.id.btnSiguiente);
        if(manifiesto!=null){
            txtPlaca.setText(manifiesto.getNumeroPlacaVehiculo());
            txtPlaca1.setText(manifiesto.getNumeroPlacaVehiculo());
        }

        txtKilometrajeInicio.setText(kilometrajeinicial);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogKilometraje.this.dismiss();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //traerAnterirKm();
                kilometrajeFinalAnterior = txtKilometrajeFinal.getText().toString();
                if(txtKilometrajeFinal.getText().length()<1){
                    messageBox("Se requiere que digite el kilometraje.");
                    return;
                }
                if(Double.parseDouble(kilometrajeFinalAnterior)<Double.parseDouble(kilometrajeinicial)) {
                    messageBox("Kilometraje incorrecto");
                    return;
                }

                MyApp.getDBO().parametroDao().saveOrUpdate("kilometraje_final_ant", kilometrajeFinalAnterior);
                nuevoKilometraje = new DialogNuevoKilometraje(getActivity());
                nuevoKilometraje.setCancelable(false);
                nuevoKilometraje.show();
                DialogKilometraje.this.dismiss();

            }
        });
    }

    private void traerAnterirKm(){
        kilometrajeFinalAnterior = txtKilometrajeFinal.getText().toString();
        if(txtKilometrajeFinal.getText().length()<1){
         messageBox("Se requiere que digite el kilometraje.");
         return;
        }
        if(Double.parseDouble(kilometrajeFinalAnterior)<Double.parseDouble(kilometrajeinicial)) {
            messageBox("Kilometraje incorrecto");
            return;
        }

        MyApp.getDBO().parametroDao().saveOrUpdate("kilometraje_final_ant", kilometrajeFinalAnterior);
        nuevoKilometraje = new DialogNuevoKilometraje(getActivity());
        nuevoKilometraje.setCancelable(false);
        nuevoKilometraje.show();

    }
}
