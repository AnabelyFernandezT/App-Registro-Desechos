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

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;


public class DialogBultosNo extends MyDialog implements View.OnClickListener {
    Activity _activity;
    EditText txtPesoBultoNo;
    LinearLayout btnCancelarBultos,btnAplicarBultos;
    private int idManifiesto;
    private int idManifiestoDetalle;

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle);
    }
    public interface OnCancelarBultoListener {
        public void onSuccesfull();
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;
    private OnCancelarBultoListener mOnCancelarBultoListener;

    public DialogBultosNo(@NonNull Context context, int idManifiesto, int idManifiestoDetalle) {
        super(context, R.layout.dialog_bultos_no);
        this._activity = (Activity) context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    @Override
    public void onClick(View v) {

    }

    public void init() {
        txtPesoBultoNo = findViewById(R.id.txtPesoBultoNo);
        String valor = txtPesoBultoNo.getText().toString();
        btnAplicarBultos = findViewById(R.id.btnAplicarBultos);
        btnAplicarBultos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRegistrarBultoListener != null) {
                    mOnRegistrarBultoListener.onSuccesfull(txtPesoBultoNo.getText().toString(),idManifiestoDetalle);
                }
            }
        });
        btnCancelarBultos = findViewById(R.id.btnCancelarBultos);
        btnCancelarBultos.setOnClickListener(new View.OnClickListener() {
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
}
