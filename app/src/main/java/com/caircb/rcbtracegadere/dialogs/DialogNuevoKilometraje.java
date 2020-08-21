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
import com.caircb.rcbtracegadere.ResultKilometraje;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.tasks.UserRutaTransladoInicioFinKTask;

public class DialogNuevoKilometraje extends MyDialog {

    Activity _activity;
    EditText txtKilometrajeInicial;
    LinearLayout btnCancelar,btnFinalizar;
    String nuevoKilometraje;

    public DialogNuevoKilometraje(@NonNull Context context) {
        super(context, R.layout.dialog_kilometraje_nuevo);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {
        txtKilometrajeInicial = getView().findViewById(R.id.kilometrajeInicial);
        btnCancelar = getView().findViewById(R.id.btnCancelar);
        btnFinalizar = getView().findViewById(R.id.btnFinalizar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNuevoKilometraje.this.dismiss();
            }
        });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtKilometrajeInicial.getText().length()<0){
                    messageBox("Se requiere que digite el kilometraje.");
                    return;
                }else{
                    nuevoKilometraje = txtKilometrajeInicial.getText().toString();
                    UserRutaTransladoInicioFinKTask registro = new UserRutaTransladoInicioFinKTask(_activity,nuevoKilometraje);
                    registro.setOnRegisterListener(new UserRutaTransladoInicioFinKTask.OnRegisterListener() {
                        @Override
                        public void onSuccessful() {
                            MyApp.getDBO().rutaInicioFinDao().actualizarKilometraje(MySession.getIdUsuario(),nuevoKilometraje);
                            DialogNuevoKilometraje.this.dismiss();

                            ((ResultKilometraje)_activity).initMain();
                        }
                    });
                    registro.execute();

                }
               // guardarDatos();

            }
        });
    }

    private void guardarDatos (){
        if(txtKilometrajeInicial.getText().length()<0){
            messageBox("Se requiere que digite el kilometraje.");
            return;
        }else{
            nuevoKilometraje = txtKilometrajeInicial.getText().toString();
        }


    }
}
