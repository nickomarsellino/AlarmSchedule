package com.example.nickomarsellino.scheduling;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.media.app.NotificationCompat;
import android.util.Log;

/**
 * Created by nicko marsellino on 3/29/2018.
 */

public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

       createNotification(context, "Ini Notifikasi");

    }

    private void createNotification(Context context, String s) {

        Log.d("abcde", "test notif");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , new Intent(context,Home_Page.class), 0);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.plusdata)
                .setContentTitle(s);

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_SOUND);

        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(1, builder.build());
    }
}
