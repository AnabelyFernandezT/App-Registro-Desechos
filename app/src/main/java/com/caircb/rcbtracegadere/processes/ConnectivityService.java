package com.caircb.rcbtracegadere.processes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.utils.Utils;

public class ConnectivityService extends BroadcastReceiver {

    public static final int DATACONNECTIVITY = 0x063;
    public static final int GPSCONNECTIVITY = 0x062;
    //LocalBroadcastManager broadcaster=LocalBroadcastManager.getInstance(MyApp.getsInstance());



    public ConnectivityService(){
        //broadcaster = LocalBroadcastManager.getInstance(MyApp.getsInstance());
        //_isDataConnectivity = Utils.isDataConnectivity(MyApp.getsInstance());
        //_isGpsEnabled = .isLocation(MyApp.getsInstance());
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("mReceiverCONNECTIVITY");
        boolean d = Utils.isDataConnectivity(MyApp.getsInstance());
        i.putExtra("connecticity", d);
        LocalBroadcastManager.getInstance(MyApp.getsInstance()).sendBroadcast(i);
    }
}
