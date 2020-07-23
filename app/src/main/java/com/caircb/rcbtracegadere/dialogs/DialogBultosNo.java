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
    LinearLayout btnAgregar;
    private int idManifiesto;
    private int idManifiestoDetalle;

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle);
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;

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
        btnAgregar = findViewById(R.id.btn_addNo);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idManifiestoDetalle,cantidadBultos,0,0,true);
                if (mOnRegistrarBultoListener != null) {
                    mOnRegistrarBultoListener.onSuccesfull(txtPesoBultoNo.getText().toString(),idManifiestoDetalle);
                }
            }
        });
    }

    public void setmOnRegistrarBultoListener(@Nullable OnRegistrarBultoListener l) {
        mOnRegistrarBultoListener = l;
    }

}
