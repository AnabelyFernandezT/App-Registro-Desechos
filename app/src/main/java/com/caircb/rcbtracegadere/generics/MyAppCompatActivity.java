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

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.processes.GPSService;
import com.caircb.rcbtracegadere.utils.Utils;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MyAppCompatActivity extends AppCompatActivity {
    private AlertDialog.Builder messageBox;
    private Context mContext;
    public Fragment fragment;
    GPSService gps;
    private SoundPool soundpool = null;
    private int soundid;
    private ScanManager mScanManager;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    private void initConnectivity(){
        if(mContext instanceof MainActivity) {

            initGPS();
            initListenerScan();
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





    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void message(String message)
    {
        messageBox = new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
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
        //initConnectivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConnectivity();
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
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
        unregisterReceiver(mScanReceiver);
    }

    private void initListenerScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }


}
