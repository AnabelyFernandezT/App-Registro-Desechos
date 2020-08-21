package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CodigoQrTransportistaEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

public class DialogQrLoteTransportista extends MyDialog {

    Activity _activity;
    ImageView imgQrLoteTransportista;
    LinearLayout btnCancelQr;


    public DialogQrLoteTransportista(@NonNull Context context ) {
        super(context, R.layout.dialog_qr_lote_transportista);
        // this.taskListener = listener;
        //this.bandera = bandera;
        //dbHelper = new DBAdapter(context);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        try {
            onInit();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

    }

    private void onInit() throws IOException, WriterException {
        imgQrLoteTransportista = (ImageView) findViewById(R.id.imgQrLoteTransportista);
        String qr="";
        CodigoQrTransportistaEntity codigoQrTransportistaEntity= MyApp.getDBO().codigoQrTransportistaDao().fetchCodigoQr2();
        if (codigoQrTransportistaEntity.getCodigoQr()!=""){
            qr=codigoQrTransportistaEntity.getCodigoQr();
            imgQrLoteTransportista.setVisibility(View.VISIBLE);
        }else {
            qr="";
            imgQrLoteTransportista.setVisibility(View.GONE);
        }


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        BitMatrix bitMatrix=multiFormatWriter.encode(qr+"", BarcodeFormat.QR_CODE,600,600,null);
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imgQrLoteTransportista.setImageBitmap(bitmap);



        btnCancelQr = (LinearLayout)getView().findViewById(R.id.btnCancelQr);
        btnCancelQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



}
