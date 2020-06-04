package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.utils.Utils;

public class DialogOptionsManifiesto extends MyDialog {

    Activity _activity;
    ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    LinearLayout btnAgregarFirmaTransportista, btnCancelar, btnGuardar;
    EditText txtPeso;
    TextView txtFirmaMensajeTransportista;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    Bitmap firmaConfirmada;

    public DialogOptionsManifiesto(@NonNull Context context, Integer idManifiesto){
        super(context, R.layout.dialog_options_manifiesto);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init(){
        txtPeso = getView().findViewById(R.id.txtPeso);

        btnGuardar = getView().findViewById(R.id.btnGuardar);
        btnCancelar = getView().findViewById(R.id.btnCancelar);

        btnAgregarFirmaTransportista = getView().findViewById(R.id.btnAgregarFirmaTransportista);
        imgFirmaTecnico= getView().findViewById(R.id.imgFirmaTecnico);
        imgFirmaTecnicoTrasnsportista = getView().findViewById(R.id.imgFirmaTecnicoTrasnsportista);
        txtFirmaMensajeTransportista = getView().findViewById(R.id.txtFirmaMensajeTransportista);

        btnAgregarFirmaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFirma==null) {
                    dialogFirma = new DialogFirma(getActivity());
                    dialogFirma.setTitle("SU FIRMA");
                    dialogFirma.setCancelable(false);
                    dialogFirma.setOnSignaturePadListener(new DialogFirma.OnSignaturePadListener() {
                        @Override
                        public void onSuccessful(Bitmap bitmap) {
                            dialogFirma.dismiss();
                            dialogFirma=null;
                            if(bitmap!=null){
                                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setImageBitmap(bitmap);
                                firmaConfirmada = bitmap;

                            }else{
                                txtFirmaMensajeTransportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCanceled() {
                            dialogFirma.dismiss();
                            dialogFirma=null;

                        }
                    });
                    dialogFirma.show();
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtPeso.getText().toString().equals("")){
                    messageBox("Debe ingresar un peso!");
                }else {
                    if(firmaConfirmada!=null){
                        String unicodeImg = Utils.encodeTobase64(firmaConfirmada);
                        MyApp.getDBO().manifiestoDao().updateFirmaWithPesoTransportista(idManifiesto, Double.valueOf(txtPeso.getText().toString()), "IMG_FIRMA" + System.currentTimeMillis() + ".jpg", unicodeImg);
                        dismiss();
                        messageBox("Registrado correctamente!!");
                    }else {
                        messageBox("Debe ingresar una firma!");
                    }
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
