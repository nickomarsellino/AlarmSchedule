package com.example.nickomarsellino.scheduling;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by nicko marsellino on 3/20/2018.
 */

class NotificationGenerator {

    private String title;
    private String date;


    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 999;

    public static void openActivityNotification(Context context, String title, String date){




        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, Home_Page.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);


        //Yang Akan Tampil di Notifikasi
        nc.setSmallIcon(R.drawable.plusdata);
        nc.setContentTitle(title);
        nc.setContentText(date);
        nc.setAutoCancel(true);


        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());




    }


    public static void NotificationReminder(Context context, String title, String date){




        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, Home_Page.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);


        //Yang Akan Tampil di Notifikasi
        nc.setSmallIcon(R.drawable.icon);
        nc.setContentTitle(title);
        nc.setContentText(date);
        nc.setAutoCancel(true);


        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());




    }

}
