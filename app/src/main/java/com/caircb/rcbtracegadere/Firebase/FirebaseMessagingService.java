package com.caircb.rcbtracegadere.Firebase;

//import android.support.v4.content.LocalBroadcastManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;

;

/**
 * Created by jlsuarez on 03/08/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    }

    private void showNotification(String message) {

    }
}
