package com.example.nickomarsellino.scheduling;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
//import android.support.v4.media.app.NotificationCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by nicko marsellino on 3/29/2018.
 */

public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("A");
        createNotification(context, title);

    }

    public void createNotification(Context context, String s) {

        Log.d("abcde", s);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , new Intent(context,Home_Page.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.plusdata)
                .setContentTitle(s);

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_SOUND);

        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
    }
}
