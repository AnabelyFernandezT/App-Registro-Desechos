package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.VistaPreliminarFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;

public class DialogManifiestoCliente extends MyDialog {
    Activity _activity;
    EditText txtManifiestoCliente;
    LinearLayout btnIniciaRutaCancel,btnIniciaRutaAplicar;
    VistaPreliminarFragment vistaPreliminarFragment;

    Manifiesto2Fragment principal = new Manifiesto2Fragment();
    Integer idManifiesto, tipoPaquete;

    public interface onRegisterListenner {
        public void onSucessfull();
    }
    public onRegisterListenner mOnRegisterListener;

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
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);
        txtManifiestoCliente = getView().findViewById(R.id.txtManifiestoCliente);
        txtManifiestoCliente.setText(manifiesto.getNumManifiestoCliente());
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
                    messageBox("No ingresó un número de manifiesto cliente!");
                }else{
                    //System.out.println("Placa transportista: "+MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista"));
                    MyApp.getDBO().manifiestoDao().updateManifiestoCliente(idManifiesto,txtManifiestoCliente.getText().toString());
                    ((MainActivity) getActivity()).NavegationFragment(VistaPreliminarFragment.newInstance(idManifiesto,tipoPaquete));

                    DialogManifiestoCliente.this.dismiss();

                }
            }
        });

    }

    public void setmOnRegisterListener(@Nullable onRegisterListenner l){ mOnRegisterListener = l;}
}
