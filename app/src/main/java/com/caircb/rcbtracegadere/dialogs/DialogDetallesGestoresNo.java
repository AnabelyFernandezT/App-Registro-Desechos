package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.R;

public class DialogDetallesGestoresNo extends MyDialog {

    Activity _activity;
    EditText txtCantidadBultos, txtPesoBultos;
    LinearLayout btnCancelarApp,btnRegistrar;
    Integer idManifiestoDetalle, idManifiesto;
    Boolean validar=true;
    TextView txtDescripcion;
    String descripcion;

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle, String pesoBulto);
    }
    public interface OnCancelarBultoListener {
        public void onSuccesfull();
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;
    private OnCancelarBultoListener mOnCancelarBultoListener;

    public DialogDetallesGestoresNo(@NonNull Context context, Integer idManifiestoDetalle, Integer idManifiesto, String descripcion) {
        super(context, R.layout.dialog_detalle_gestores_no);
        this._activity = (Activity)context;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idManifiesto = idManifiesto;
        this.descripcion = descripcion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
        loadData();
    }

    private void loadData() {
        txtDescripcion.setText(descripcion);
    }

    private void init() {

        txtCantidadBultos = getView().findViewById(R.id.txtCantidadBultos);
        txtPesoBultos = getView().findViewById(R.id.txtPesoBultos);
        btnRegistrar = getView().findViewById(R.id.btnRegistrar);
        btnCancelarApp = getView().findViewById(R.id.btnCancelarApp);
        txtDescripcion = getView().findViewById(R.id.txtDescripcion);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    if (mOnRegistrarBultoListener != null) {
                        mOnRegistrarBultoListener.onSuccesfull(txtCantidadBultos.getText().toString(), idManifiestoDetalle, txtPesoBultos.getText().toString());
                    }
                }else {
                    messageBox("Debe ingresar cantidad de bultos y peso para continuar.");
                    validar = true;
                    return;
                }
            }
        });

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCancelarBultoListener.onSuccesfull();
            }
        });
    }

    public void setmOnRegistrarBultoListener(@Nullable OnRegistrarBultoListener l) {
        mOnRegistrarBultoListener = l;
    }
    public void setmOnCancelarBultoListener(@Nullable OnCancelarBultoListener l) {
        mOnCancelarBultoListener = l;
    }

    private Boolean validar (){
        if(txtPesoBultos.getText().toString().equals(".")){
            return validar = false;
        }else{
        double  peso = Double.parseDouble(txtPesoBultos.getText().toString());
        System.out.println(peso);
        double cero =0.0;
        if(txtPesoBultos.getText().toString().equals("")||txtPesoBultos.getText().toString().equals("0")||peso == cero){
            return validar = false;
        }else if(txtCantidadBultos.getText().toString().equals("") || txtCantidadBultos.getText().toString().equals("0")){
            return  validar = false;
        }
        }
        return validar;
    }
}
