package com.caircb.rcbtracegadere.Firebase;

//import android.support.v4.content.LocalBroadcastManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.ResultActivity;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoDetalle;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.google.firebase.messaging.RemoteMessage;

;import java.util.Date;

/**
 * Created by jlsuarez on 03/08/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    public static final String TAG = "Noticias";

    @Override
    public void onCreate() {

    }

    public interface OnRegisterListener {
        public void onNoPeso();

        public void onSiPeso();
    }

    private OnRegisterListener mOnRegisterListener;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG, "mensaje de: " + from);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "TITULO " + remoteMessage.getNotification().getTitle());
           /* if(remoteMessage.getNotification().getTitle().equals("NOTIFICACIÓN DE PESO EXTRA")){
                showNotificationAutoPesos(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }*/
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
            MyApp.getDBO().parametroDao().saveOrUpdate("notif_value", "" + remoteMessage.getData().get("idCatalogoRespuesta"));
        }
        if (remoteMessage.getData().get("idCatalogoRespuesta").equals(5)) {
            if (mOnRegisterListener != null) mOnRegisterListener.onNoPeso();
        }

        if (remoteMessage.getData().get("idCatalogoRespuesta").equals(6)) {
            if (mOnRegisterListener != null) mOnRegisterListener.onSiPeso();
        }

        if (remoteMessage.getData().get("idCatalogoRespuesta").equals("10")) {//si(autorizacion sin impresora)
            MyApp.getDBO().parametroDao().saveOrUpdate("auto_impresion" + MySession.getIdUsuario(), "1");
            MyApp.getDBO().manifiestoDetallePesosDao().updateImpresionByIdUsuarioRecolector(MySession.getIdUsuario(), true);
        }
        if (remoteMessage.getData().get("idCatalogoRespuesta").equals("11")) {//no
            MyApp.getDBO().parametroDao().saveOrUpdate("auto_impresion" + MySession.getIdUsuario(), "0");
        }
    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("notification_data", body);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(notifyPendingIntent);

        String channelId = getString(R.string.default_notification_channel_name);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void showNotificationAutoPesos(String title, String body) {
        Intent intent = new Intent(this, TabManifiestoDetalle.class);
        intent.putExtra("notification_data", body);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getsInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        String channelId = getString(R.string.default_notification_channel_name);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l) {
        mOnRegisterListener = l;
    }

}
