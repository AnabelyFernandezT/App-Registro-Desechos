package com.caircb.rcbtracegadere.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;

import java.util.Timer;

public class DialogAudio extends MyDialog implements View.OnClickListener {
 LinearLayout btnStar, btnStop;
 TextView txtHora;
 Thread cont;
 boolean isOn=false;
 int mili =0, seg=0, minutos=0;
 Handler h = new Handler();
 private Timer timer;

    public DialogAudio(
            @NonNull Context context) {
        super(context, R.layout.dialog_audio_);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        cronometro();
    }



    private void init() {
        btnStar = getView().findViewById(R.id.btn_iniciar);
        btnStop = getView().findViewById(R.id.btn_parar);
        txtHora = getView().findViewById(R.id.txtHora);
        btnStar.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private void cronometro(){
        cont = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(isOn){
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        mili++;
                        if (mili==999){
                            seg++;
                            mili=0;
                        }
                        if (seg==59){
                            minutos++;
                            seg=0;
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                           String m = "", s="", mi="";
                           if (mili<10){
                               m="00"+mili;
                           }else if (mili<100){
                                m="00"+mili;
                           }else{
                               m=""+mili;
                           }
                           if (seg<10){
                               s="0"+seg;
                           }else {
                               s=""+seg;
                           }
                           if (minutos<10){
                               mi="0"+minutos;
                           }else{
                               mi=""+minutos;
                           }
                           txtHora.setText(mi+":"+s);
                            }
                        });
                    }
                }
            }
        });
        cont.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_iniciar:
                isOn=true;
                break;
            case R.id.btn_parar:
                isOn = false;
                break;
        }

    }


}
