package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle, String pesoBulto);
    }
    public interface OnCancelarBultoListener {
        public void onSuccesfull();
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;
    private OnCancelarBultoListener mOnCancelarBultoListener;

    public DialogDetallesGestoresNo(@NonNull Context context, Integer idManifiestoDetalle, Integer idManifiesto) {
        super(context, R.layout.dialog_detalle_gestores_no);
        this._activity = (Activity)context;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idManifiesto = idManifiesto;
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

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    if (mOnRegistrarBultoListener != null) {
                        mOnRegistrarBultoListener.onSuccesfull(txtCantidadBultos.getText().toString(), idManifiestoDetalle, txtPesoBultos.getText().toString());
                    }
                }else {
                    messageBox("Datos ingresados no validos");
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
        if(txtPesoBultos.getText().toString().equals("")||txtPesoBultos.getText().toString().equals("0")){
            return validar = false;
        }else if(txtCantidadBultos.getText().toString().equals("") || txtCantidadBultos.getText().toString().equals("0")){
            return  validar = false;
        }
        return validar;
    }
}
