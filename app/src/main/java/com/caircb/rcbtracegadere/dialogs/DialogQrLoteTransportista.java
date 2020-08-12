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

import com.caircb.rcbtracegadere.R;
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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.reactivex.annotations.NonNull;

public class DialogQrLoteTransportista extends MyDialog {

    Activity _activity;
    ImageView imgQrLoteTransportista;
    LinearLayout btnCancelQr;
    QRGEncoder qrgEncoder;

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
        String qr="1597225669$3$522$131";
      /*  int smallerdimention=500;
        smallerdimention=smallerdimention*3/4;
        qrgEncoder=new QRGEncoder(qr,null, QRGContents.Type.TEXT,smallerdimention);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imgQrLoteTransportista.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
*/
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

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.WHITE : Color.RED;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }

   /* public static Bitmap encodeStringToBitmap(String contents) throws WriterException {
//Null check, just b/c
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, BarcodeFormat.QR_CODE, 700, 900, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }*/

}
