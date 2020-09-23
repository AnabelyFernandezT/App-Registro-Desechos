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
import com.caircb.rcbtracegadere.Notificaciones.ResultCambioChoferActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.ResultActivity;
import com.caircb.rcbtracegadere.ResultKilometraje;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoDetalle;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemNotificacion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.CierreLoteActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

/**
 * Created by jlsuarez on 03/08/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    public static final String TAG = "Noticias";

    @Override
    public void onCreate() {

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG, "mensaje de: " + from);

        if (remoteMessage.getNotification() != null) {
            ItemNotificacion it = new ItemNotificacion();
            it.setNombreNotificacion(remoteMessage.getNotification().getTitle());
            it.setEstadoNotificacion(remoteMessage.getNotification().getBody());
            it.setTipoNotificacion(remoteMessage.getData().get("idCatalogoRespuesta"));
            it.setIdManifiesto(remoteMessage.getData().get("idManifiesto"));
            it.setPeso(remoteMessage.getData().get("pesoAprobado"));
            MyApp.getDBO().notificacionDao().saveOrUpdate(it);
            Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "TITULO " + remoteMessage.getNotification().getTitle());
           /* if(remoteMessage.getNotification().getTitle().equals("NOTIFICACIÓN DE PESO EXTRA")){
                showNotificationAutoPesos(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }*/
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),remoteMessage.getData().get("idCatalogoRespuesta"), getApplicationContext());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
            /*
            String name = "notif_value_"+remoteMessage.getData().get("idManifiestoDetalle");
            MyApp.getDBO().parametroDao().saveOrUpdate("notif_value",""+remoteMessage.getData().get("idCatalogoRespuesta"));
            MyApp.getDBO().parametroDao().saveOrUpdate(name,""+remoteMessage.getData().get("idManifiestoDetalleRespuesta"));
            ---------------------------------
            String name = "notif_value_"+remoteMessage.getData().get("idManifiestoDetalle");
            MyApp.getDBO().parametroDao().saveOrUpdate("notif_value",""+remoteMessage.getData().get("idCatalogoRespuesta"));
            MyApp.getDBO().parametroDao().saveOrUpdate(name,""+remoteMessage.getData().get("idManifiestoDetalleRespuesta"));*/
            if(remoteMessage.getData().get("idCatalogoRespuesta").equals("5")){
                MyApp.getDBO().pesoExtraDao().saveOrUpdate(Integer.parseInt(remoteMessage.getData().get("idManifiesto")),
                                                           Integer.parseInt(remoteMessage.getData().get("idManifiestoDetalle")),
                                                           Double.parseDouble(remoteMessage.getData().get("pesoAprobado")),
                                                           1);

                MyApp.getDBO().manifiestoDetalleDao().updatePesoReferncial(Integer.parseInt(remoteMessage.getData().get("idManifiestoDetalle")),
                                                                               (Double.parseDouble(remoteMessage.getData().get("pesoAprobado"))));
            }

            if(remoteMessage.getData().get("idCatalogoRespuesta").equals("6")){
                MyApp.getDBO().pesoExtraDao().saveOrUpdate(Integer.parseInt(remoteMessage.getData().get("idManifiesto")),
                        Integer.parseInt(remoteMessage.getData().get("idManifiestoDetalle")),
                        Double.parseDouble(remoteMessage.getData().get("pesoAprobado")),
                        3);

            }

            if(remoteMessage.getData().get("idCatalogoRespuesta").equals("2")||remoteMessage.getData().get("idCatalogoRespuesta").equals("15")){
                showNotificationPlacas(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }


        }


        if (remoteMessage.getData().get("idCatalogoRespuesta").equals("10")) {//si(autorizacion sin impresora)
            MyApp.getDBO().parametroDao().saveOrUpdate("auto_impresion" + MySession.getIdUsuario(), "1");
            MyApp.getDBO().manifiestoDetallePesosDao().updateImpresionByIdUsuarioRecolector(MySession.getIdUsuario(), true);
        }
        if (remoteMessage.getData().get("idCatalogoRespuesta").equals("11") || remoteMessage.getData().get("idCatalogoRespuesta").equals("17")) {//no
            MyApp.getDBO().parametroDao().saveOrUpdate("auto_impresion" + MySession.getIdUsuario(), "0");
        }
    }

    public void showNotification(String title, String body, String tipo, Context context) {


            Intent intent;
            if(tipo.equals("2")||tipo.equals("15")){
                intent = new Intent(context, ResultKilometraje.class);
            }else if(tipo.equals("7")){
                intent = new Intent(context, ResultCambioChoferActivity.class);
            }/*else if(tipo.equals("8")) {
                intent = new Intent(context, MainActivity.class);
            }*/else if (tipo.equals("12")){
                intent= new Intent(context, CierreLoteActivity.class);
            }else{
                intent = new Intent(context, ResultActivity.class);
            }


                intent.putExtra("notification_data",body);
                intent.putExtra("notification_tipo",tipo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                String channelId1 = getString(R.string.default_notification_channel_name);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setChannelId(channelId1)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setOngoing(true)
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

    public void showNotificationPlacas(String title, String body) {
        Intent intent = new Intent(this, ResultKilometraje.class);
        intent.putExtra("notification_data",body);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getsInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOngoing(true)
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



}
