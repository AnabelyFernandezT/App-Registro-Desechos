package com.caircb.rcbtracegadere.generics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.processes.GPSService;
import com.caircb.rcbtracegadere.processes.OffLineWorker;
import com.caircb.rcbtracegadere.utils.Utils;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;

import java.util.concurrent.TimeUnit;

public class MyAppCompatActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener {
    private AlertDialog.Builder messageBox;
    private Context mContext;
    public Fragment fragment;
    GPSService gps;
    private SoundPool soundpool = null;
    private int soundid;
    private ScanManager mScanManager;

    //Barcode Honeywell
    private static BarcodeReader barcodeReader;
    private AidcManager manager;

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //if(mContext instanceof MainActivity){
            //initWorked();
        //}
    }

    private void initConnectivity(){
        if(mContext instanceof MainActivity) {

            initGPS();
           /***Cometado para dispositivos emulador***
            initListenerScan();
            /***********/

            boolean estado = Utils.isDataConnectivity(mContext);
            if(MySession.isConnecticity()!=estado) {
            MySession.setConnecticity(estado);
            showMessage();
            }
        }
    }

    private void initGPS(){
        // Check if GPS enabled
        gps = new GPSService(this);
        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
        }else{
            gps.showSettingsAlert();
        }
    }

    private void initWorked(){
        final Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                //.setRequiresCharging(true)
                .build();

        final PeriodicWorkRequest periodicWorkRequest
                = new PeriodicWorkRequest.Builder(OffLineWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    public Location getLocation(){
        return gps!=null? gps.getLocation():null;
    }

    private void showMessage(){
        ((MainActivity)mContext).findViewById(R.id.header_message).setVisibility(MySession.isConnecticity()? View.GONE: View.VISIBLE);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras  = intent.getExtras();
            Boolean estado = extras.getBoolean("connecticity");
            if(MySession.isConnecticity()!=estado) {
                MySession.setConnecticity(estado);
                showMessage();
            }
        }
    };

    private void initBroadcast(){
        LocalBroadcastManager.getInstance(this).registerReceiver((mReceiver),
                new IntentFilter("mReceiverCONNECTIVITY")
        );

    }

    private void initBarcodeScan(){
        if(MySession.getDispositivoManufacturer().equals("Honeywell")){
            initListenerScanHoneywell();
        }else if (MySession.getDispositivoManufacturer().equals("UBX")){
            stopUnitechScan();
            initListenerScan();

            //CONFIGURACION DEL SCANNER DE CODIGO DE BARRAS
            IntentFilter filter = new IntentFilter();
            int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
            String[] value_buf = mScanManager.getParameterString(idbuf);
            if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
                filter.addAction(value_buf[0]);
            } else {
                filter.addAction(SCAN_ACTION);
            }

            registerReceiver(mScanReceiver, filter);
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void message(String message)
    {
        DialogBuilder dialogBuilder = new DialogBuilder(mContext);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(message);
        dialogBuilder.setTitle("INFO");
        dialogBuilder.setPositiveButton("OK",null);
        dialogBuilder.show();

        /*messageBox = new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();*/
    }

    public void showToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public int getResources(String name){
        return  this.getResources().getIdentifier(name, "mipmap", this.getPackageName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null) LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConnectivity();
        initBarcodeScan();
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            soundpool.play(soundid, 1, 1, 0, 0, 1);

            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            String barcodeStr = new String(barcode, 0, barcodelen);
            if(barcodeStr!=null && fragment!=null && barcodeStr.length()>0){
                barcodeStr =barcodeStr.replaceAll(System.getProperty("line.separator"), "");
                if(fragment instanceof OnBarcodeListener){
                    ((OnBarcodeListener)fragment).reciveData(barcodeStr);
                }

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

       if (MySession.getDispositivoManufacturer().equals("UBX")){
           stopUnitechScan();
        }

    }

    private void stopUnitechScan(){
        try {
            if (mScanManager != null) {
                mScanManager.stopDecode();
            }
            unregisterReceiver(mScanReceiver);
        }
        catch(Exception ex){

        }
    }

    private void initListenerScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    private void initListenerScanHoneywell(){
        try {
            AidcManager.create(this, new AidcManager.CreatedCallback() {
                @Override
                public void onCreated(AidcManager aidcManager) {
                    manager = aidcManager;
                    try {
                        barcodeReader = manager.createBarcodeReader();

                        if(barcodeReader!=null) {
                            //Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                            barcodeReader.claim();
                        }
                    }
                    catch (ScannerUnavailableException e) {
                        Toast.makeText(MyAppCompatActivity.this, "Failed to claim scanner",
                                Toast.LENGTH_SHORT).show();
                        //e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // register bar code event listener
                    barcodeReader.addBarcodeListener(MyAppCompatActivity.this);
                }
            });
        }
        catch(Exception ex){

        }
    }

    @Override
    public void onBarcodeEvent(final  BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragment instanceof OnBarcodeListener) {
                    ((OnBarcodeListener) fragment).reciveData(barcodeReadEvent.getBarcodeData());
                    //Toast.makeText(MyActivity.this, barcodeReadEvent.getBarcodeData(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }
}
