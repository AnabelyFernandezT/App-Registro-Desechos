package com.caircb.rcbtracegadere.dialogs;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class DialogAudio extends MyDialog implements View.OnClickListener {

    private static final int THRESHOLD_EXERSISE = 120000;

    private long timeWhenStopped = 0;

     LinearLayout btnStar, btnStop,btnCancel, btnPausa,btnAplicar;
     Chronometer chronometer;
     MediaRecorder mediaRecorder;
     String outputFile;

    public interface OnAgregarAudioListener {
        void onSuccessful(String audio,String tiempo);
        void onCanceled();
    }

    private OnAgregarAudioListener mOnAgregarAudioListener;

    boolean grabando=false;

    public DialogAudio(
            @NonNull Context context) {
        super(context, R.layout.dialog_audio_);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }



    private void init() {

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio.mp3";

        btnStar = getView().findViewById(R.id.btn_iniciar);
        btnStop = getView().findViewById(R.id.btn_parar);
        btnAplicar = getView().findViewById(R.id.btnAplicar);
        chronometer = getView().findViewById(R.id.chronometer);


        btnCancel = getView().findViewById(R.id.btnCancel);
        btnPausa = getView().findViewById(R.id.btn_pausa);

        btnAplicar.setEnabled(false);

        btnStar.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAplicar.setOnClickListener(this);
        btnPausa.setOnClickListener(this);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                if(elapsedMillis>THRESHOLD_EXERSISE){
                    chronometer.stop();
                    messageBox("El tiempo que usted tiene para esta grabación ha llegado a su limite.");
                }
            }
        });
    }

    private void initRecorder(){
        try {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);

        }catch (Exception e){

        }
    }

    private void start(){
        try {
        if(!grabando){
            initRecorder();
            grabando=true;
        }
        chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
        chronometer.start();

        mediaRecorder.prepare();
        mediaRecorder.start();

        }catch (Exception e){

        }
    }

    private void pause(){
        try {
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
                mediaRecorder.resume();
            }
        }catch (Exception e){}

    }

    private void stop(){
        try{
            chronometer.stop();
            //chronometer.setBase(SystemClock.elapsedRealtime());
            //timeWhenStopped = 0;
            mediaRecorder.stop();
            mediaRecorder.release();

            btnAplicar.setEnabled(true);


        }catch (Exception e){

        }
    }

    private void aplicar(){
        try {
            File file = new File(outputFile);
            byte[] bytesArray = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();

            String data = Utils.encodeTobase64(bytesArray);
            if(mOnAgregarAudioListener!=null){
                mOnAgregarAudioListener.onSuccessful(data,chronometer.getText().toString());
            }
            //eliminar audio temporal...
            if(file.exists()){
                file.delete();
            }
            //PlayAudio(data);


        }catch (Exception e){
            int x=0;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_iniciar:
                start();
                btnStar.setVisibility(View.GONE);
                btnPausa.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_pausa:
                pause();
                btnStar.setVisibility(View.VISIBLE);
                btnPausa.setVisibility(View.GONE);
                break;
            case R.id.btn_parar:
                stop();
                btnStar.setVisibility(View.VISIBLE);
                btnPausa.setVisibility(View.GONE);
                break;
            case R.id.btnCancel:
                DialogAudio.this.dismiss();
                break;
            case R.id.btnAplicar:
                aplicar();
                break;

        }

    }

    public void setOnAgregarAudioListener(@NonNull OnAgregarAudioListener l){
       mOnAgregarAudioListener = l;
    }


}
