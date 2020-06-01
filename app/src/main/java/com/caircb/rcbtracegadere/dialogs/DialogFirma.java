package com.caircb.rcbtracegadere.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

public class DialogFirma extends MyDialog {

    Button btnCancelar,btnAplicar;
    SignaturePad signaturePad;

    public interface OnSignaturePadListener {
        public void onSuccessful(Bitmap bitmap);
        public void  onCanceled();
    }

    private OnSignaturePadListener mOnSignaturePadListener;

    public DialogFirma(@NonNull Context context) {
        super(context, R.layout.dialog_firma);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init(){
        btnCancelar = (Button)getView().findViewById(R.id.btnCancelarFirma);
        btnAplicar = (Button)getView().findViewById(R.id.btnAplicarFirma);
        signaturePad = (SignaturePad)getView().findViewById(R.id.signature_pad_generador);

        //------------------------------------------------------------------------------------------
        //      EVENTOS
        //------------------------------------------------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSignaturePadListener!=null){
                    mOnSignaturePadListener.onCanceled();
                }
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSignaturePadListener!=null){
                    mOnSignaturePadListener.onSuccessful(signaturePad.isEmpty()?null:signaturePad.getSignatureBitmap());
                }
            }
        });

    }

    public void setOnSignaturePadListener(@NonNull OnSignaturePadListener l){
        mOnSignaturePadListener =l;
    }
}
