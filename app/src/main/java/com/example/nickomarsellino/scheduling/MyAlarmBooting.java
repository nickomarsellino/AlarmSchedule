package com.example.nickomarsellino.scheduling;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MyAlarmBooting extends BroadcastReceiver {

    //Untuk Database nya
    private ScheduleDBHelper dbHelper;

    Calendar mCurrentDate;
    int dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate, remindTime;
    String reminderTime;
    int dayCurrent, monthCurrent, yearCurrent, hourCurrent, minuteCurrent;

    @Override
    public void onReceive(Context context, Intent intent) {


        dbHelper = new ScheduleDBHelper(context);
        mCurrentDate = Calendar.getInstance();
        final List<Schedule> scheduleList = dbHelper.schedulesList();


        //get current date and time for validation
        dayCurrent = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        monthCurrent = mCurrentDate.get(Calendar.MONTH);
        yearCurrent = mCurrentDate.get(Calendar.YEAR);
        hourCurrent = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        minuteCurrent = mCurrentDate.get(Calendar.MINUTE);



        for (final Schedule data : scheduleList) {

            //Untuk split si Tanggal sesuai yang dibutuhkan
            String calender = data.getDate().toString();

            String[] separated = calender.split("-");

            yearUpdate = Integer.parseInt(separated[0]);
            monthUpdate = Integer.parseInt(separated[1]);
            dayUpdate = Integer.parseInt(separated[2]);

            //Untuk split si Time sesuai yang dibutuhkan
            String time = data.getTime().toString();

            String[] separatedTime = time.split(":");

            hourUpdate = Integer.parseInt(separatedTime[0]);
            minuteUpdate = Integer.parseInt(separatedTime[1]);

            //Untuk split si Time sesuai yang dibutuhkan
            String remind = data.getReminder().toString();

            String[] separatedRemind = remind.split(" ");

            remindTime = Integer.parseInt(separatedRemind[0]);
            reminderTime = separatedRemind[1];
            ////////////////////////////////////////////////////////////////////////////////


            if(reminderTime.equals("day")){
                if(yearCurrent <= yearUpdate && monthCurrent <= monthUpdate && dayCurrent <= (dayUpdate-remindTime)) {
                    if (hourCurrent <= hourUpdate && minuteCurrent <= minuteUpdate) {
                        //Untuk masang Alarm dari inputan
                        mCurrentDate.set(Calendar.DAY_OF_MONTH,dayUpdate-remindTime);
                        mCurrentDate.set(Calendar.MONTH,monthUpdate);
                        mCurrentDate.set(Calendar.YEAR,yearUpdate);
                        mCurrentDate.set(Calendar.HOUR_OF_DAY,hourUpdate);
                        mCurrentDate.set(Calendar.MINUTE,minuteUpdate);
                        mCurrentDate.set(Calendar.SECOND,0);

                        intent = new Intent(context, MyAlarm.class);
                        Bundle args = new Bundle();
                        args.putParcelable(MyAlarm.EXTRA_SCHEDULE, data);
                        intent.putExtra("a", args);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) data.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                    }
                }
            }


            else if(yearCurrent <= yearUpdate && monthCurrent <= monthUpdate && dayCurrent <= dayUpdate){
                if(reminderTime.equals("hour")){
                    if(hourCurrent <= (hourUpdate-remindTime) && minuteCurrent <= minuteUpdate){
                        //Untuk masang Alarm dari inputan
                        mCurrentDate.set(Calendar.DAY_OF_MONTH,dayUpdate);
                        mCurrentDate.set(Calendar.MONTH,monthUpdate);
                        mCurrentDate.set(Calendar.YEAR,yearUpdate);
                        mCurrentDate.set(Calendar.HOUR_OF_DAY,hourUpdate-remindTime);
                        mCurrentDate.set(Calendar.MINUTE,minuteUpdate);
                        mCurrentDate.set(Calendar.SECOND,0);


                        intent = new Intent(context, MyAlarm.class);
                        Bundle args = new Bundle();
                        args.putParcelable(MyAlarm.EXTRA_SCHEDULE, data);
                        intent.putExtra("a", args);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) data.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                    }
                }
            }
        }

    }
}
