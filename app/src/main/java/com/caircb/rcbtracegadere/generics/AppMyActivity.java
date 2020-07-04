package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.ScanManager.IOnScannerEvent;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.utils.ApplicationService;
import com.caircb.rcbtracegadere.utils.Utils;

;

public class AppMyActivity extends Activity {

    public AppDatabase dbHelper;
    public Cursor cursor;
    AlertDialog.Builder messageBox;
    public ProgressDialog dialog;
    public Fragment fragment;

    private ProgressDialog pausingDialog;

    private int[] to1 = new int[]{android.R.id.text1};
    private SimpleCursorAdapter adapter1;
    private Spinner defaulSpiner;

    //private MyReceiver receiver;
    public EditText Barcode;

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;


    public void showToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
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




    public ProgressDialog ProgressDialog(){
        if(dialog!=null) {if(dialog.isShowing())dialog.dismiss();dialog=null;}

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        return dialog;

    }

    public void messageBox(String method, String message)
    {
        messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public void messageBox(String message)
    {
        messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void toggleKeyBoardEvent(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void hideKeyBoardEvent(TextView txt){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(txt.getWindowToken(), 0);
    }


    public void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int getIndex(Cursor cursor, String myString)
    {
        int index = 0,i=0;
        if (cursor.moveToFirst()) {
            do {
                i++;
                if(cursor.getString(4).trim().equals(myString.trim())){
                    index=i;
                }
            } while ( index==0 && cursor.moveToNext());
        }
        return index;
    }






    private void scanBarcode(String data){
        if(Barcode!=null){
            Barcode.setText(data);
            Barcode.setSelection(Barcode.getText().length());
        }
    }

    private void initListenerScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        receiver = new MyReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.server.scannerservice.broadcast");
        registerReceiver(receiver, filter);
        mBoundReceiver=true;
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onDestroy() {
        Barcode=null;
        MySession.setDestroy(true);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        MySession.setStop(true);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initListenerScan();

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

    @Override
    protected void onStart() {
        startService(new Intent(getBaseContext(), ApplicationService.class));
        MySession.setStop(false);
        if(MySession.isDestroy()){
            MySession.setDestroy(false);
        }
        super.onStart();
    }

}
