package com.example.nickomarsellino.scheduling;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
//import android.support.v4.media.app.NotificationCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nicko marsellino on 3/29/2018.
 */

public class MyAlarm extends BroadcastReceiver {

    public static final String EXTRA_SCHEDULE = "extra_schedule";

    @Override
    public void onReceive(Context context, Intent intent) {

//        Schedule schedule = intent.getExtras().getParcelable("schedule");
        Bundle b = intent.getBundleExtra("a");
        Schedule schedule = b.getParcelable(EXTRA_SCHEDULE);

        String title = schedule.getTitle();
        String date = schedule.getDate();


        createNotification(context, title, date);

    }

    public void createNotification(Context context, String title, String date) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(
//                    channelId, channelName, importance);
//
//
//            mChannel.enableLights(true);
//            mChannel.enableVibration(true);
//            mChannel.setLightColor(Color.LTGRAY);
//            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(mChannel);
//
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, getAlarmId(context) , new Intent(context,Home_Page.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//            Notification.Builder builder = new Notification.Builder(context)
//                    .setSmallIcon(R.drawable.plusdata)
//                    .setContentTitle(title)
//                    .setContentText(date);
//
//            builder.setContentIntent(pendingIntent);
//            builder.setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_SOUND);
//
//            builder.setAutoCancel(true);
//
//            notificationManager.notify(getAlarmId(context), builder.build());
//
//
//        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.plusdata)
                .setContentTitle(title)
                .setContentText(date);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, getAlarmId(context) ,
                new Intent(context,Home_Page.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_SOUND);

        builder.setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);


            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setLightColor(Color.LTGRAY);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(getAlarmId(context), builder.build());
    }

    public static int getAlarmId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();
        return alarmId;
    }
}
