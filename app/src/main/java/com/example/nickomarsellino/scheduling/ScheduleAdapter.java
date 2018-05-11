package com.example.nickomarsellino.scheduling;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by nicko marsellino on 3/18/2018.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private List<Schedule> mScheduleList;
    List<ScheduleImage> scheduleImage;
    private Context mContext;
    private RecyclerView mRecyclerV;
    Calendar mCurrentDate;
    private ScheduleDBHelper dbHelper;
    String reminderTime;
    Intent intent;
    int minuteFlag;
    int hourFlag;


    int dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate, remindTime;
    int dayCurrent, monthCurrent, yearCurrent, hourCurrent, minuteCurrent;


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView scheduleTitleTxtV;
        public TextView scheduleContentxtV;
        public TextView scheduleDateTxtV;
        public TextView deleteText;
        public TextView imageTextView;
        public ImageView imageViewData;
        public RelativeLayout viewListData, viewSwipe;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            scheduleTitleTxtV = (TextView) v.findViewById(R.id.title);
            scheduleContentxtV = (TextView) v.findViewById(R.id.content);
            scheduleDateTxtV = (TextView) v.findViewById(R.id.date);
            viewListData = (RelativeLayout) v.findViewById(R.id.viewListData);
            deleteText = (TextView) v.findViewById(R.id.delete);
            imageTextView = (TextView) v.findViewById(R.id.imageText);
            imageViewData = (ImageView) v.findViewById(R.id.imageView);
            viewSwipe= (RelativeLayout) v.findViewById(R.id.viewBackgroundSwipe);

        }
    }


    public Schedule getPosition(int position){
        final Schedule schedule = mScheduleList.get(position);

        return schedule;
    }


    public void remove(int position){

        ScheduleDBHelper dbHelper = new ScheduleDBHelper(mContext);

        final Schedule schedule = mScheduleList.get(position);

        //save image from ID
        scheduleImage = dbHelper.getScheduleImage(schedule.getId());

        dbHelper.deleteSchedule(schedule.getId(), mContext);

        Intent myIntent = new Intent(mContext, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        mScheduleList.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreItem(Schedule schedule, int position) {
        // notify item added by position
        mScheduleList.add(position, schedule);
        notifyItemInserted(position);

        dbHelper = new ScheduleDBHelper(mContext);
        long idSchedule = dbHelper.restoreSchedule(schedule);

        setAlarm(schedule,idSchedule);
        saveImages(idSchedule);

    }




    public ScheduleAdapter(List<Schedule> myDataset, Context context, RecyclerView recyclerView){
        mScheduleList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;

        // item decoration
        mRecyclerV.addItemDecoration(new SimpleItemDecorator(5));
    }

    public void ScheduleAdapter1(List<Schedule> myDataset){
        mScheduleList = myDataset;
    }


    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.list_data,parent,false);


        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Insialisasi Untuk FOnt
        Typeface typeFaceTitle = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "Raleway-SemiBold.ttf");

        Typeface typeFaceContent = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "Raleway-Light.ttf");

        Typeface typeFaceCalendar = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "Raleway-LightItalic.ttf");




        final Schedule schedule = mScheduleList.get(position);
        dbHelper = new ScheduleDBHelper(mContext);

        final List<ScheduleImage> scheduleImage = dbHelper.getScheduleImage(schedule.getId());

        holder.scheduleTitleTxtV.setTypeface(typeFaceTitle);
        holder.scheduleContentxtV.setTypeface(typeFaceContent);
        holder.scheduleDateTxtV.setTypeface(typeFaceCalendar);
        holder.deleteText.setTypeface(typeFaceContent);
        holder.imageTextView.setTypeface(typeFaceCalendar);




        holder.scheduleTitleTxtV.setText("Title: " + schedule.getTitle());
        holder.scheduleContentxtV.setText("Content: " + schedule.getContent());
        holder.scheduleDateTxtV.setText("Reminder For: " +schedule.getDate());

        if(scheduleImage.isEmpty()){
            holder.imageTextView.setVisibility(View.GONE);
            holder.imageViewData.setVisibility(View.GONE);
        }
        else {
            holder.imageTextView.setVisibility(View.VISIBLE);

            for(final ScheduleImage img: scheduleImage){
                Uri uriFromPath = Uri.fromFile(new File(img.getImage()));
                holder.imageViewData.setImageURI(uriFromPath);
            }
        }



        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goViewActivity(schedule.getId());
            }
        });

    }


    private void saveImages(long id) {
        for(final ScheduleImage img: scheduleImage){

            ScheduleImage scheduleImage = new ScheduleImage();
            scheduleImage.setIdSchedule(id);
            scheduleImage.setImage(img.getImage());

            Log.v("test", "restore Item: "+id+"\n");
            Log.v("test", "restore Item: "+img.getImage()+"\n");


            dbHelper.saveNewScheduleImage(scheduleImage);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);



    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }


    private void goViewActivity(long personId){
        Intent goToView = new Intent(mContext, show_Detail_Schedule.class);
        goToView.putExtra("USER_ID", personId);
        mContext.startActivity(goToView);
    }

    private void goUpdateActivity(long personId){
        Intent goToView = new Intent(mContext, Update_Schedule.class);
        goToView.putExtra("USER_ID", personId);
        mContext.startActivity(goToView);
    }

    private void setAlarm(Schedule schedule, long id) {


        mCurrentDate = Calendar.getInstance();

        //get current date and time for validation
        dayCurrent = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        monthCurrent = mCurrentDate.get(Calendar.MONTH);
        yearCurrent = mCurrentDate.get(Calendar.YEAR);
        hourCurrent = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        minuteCurrent = mCurrentDate.get(Calendar.MINUTE);


        //Untuk split si Tanggal sesuai yang dibutuhkan
        String calender = schedule.getDate().toString();

        String[] separated = calender.split("-");

        yearUpdate = Integer.parseInt(separated[0]);
        monthUpdate = Integer.parseInt(separated[1]);
        dayUpdate = Integer.parseInt(separated[2]);

        //Untuk split si Time sesuai yang dibutuhkan
        String time = schedule.getTime().toString();

        String[] separatedTime = time.split(":");

        hourUpdate = Integer.parseInt(separatedTime[0]);
        minuteUpdate = Integer.parseInt(separatedTime[1]);

        //Untuk split si Time sesuai yang dibutuhkan
        String remind = schedule.getReminder().toString();

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

                    intent = new Intent(mContext, MyAlarm.class);
                    Bundle args = new Bundle();
                    args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                    intent.putExtra("a", args);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
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


                    intent = new Intent(mContext, MyAlarm.class);
                    Bundle args = new Bundle();
                    args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                    intent.putExtra("a", args);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                }
            }




            //UNUTK MINUTE
            if(reminderTime.equals("minute")){

                minuteFlag = minuteUpdate - remindTime;

                if(dayCurrent == dayUpdate){

                    if(minuteFlag < 0) {

                        hourFlag = hourUpdate - 1;
                        minuteUpdate = 60 + minuteFlag;


                        if(hourCurrent <= hourFlag){

                            if(minuteCurrent <= minuteUpdate){
                                //Untuk masang Alarm dari inputan
                                mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate);
                                mCurrentDate.set(Calendar.MONTH, monthUpdate);
                                mCurrentDate.set(Calendar.YEAR, yearUpdate);
                                mCurrentDate.set(Calendar.HOUR_OF_DAY, hourFlag);
                                mCurrentDate.set(Calendar.MINUTE, minuteUpdate);
                                mCurrentDate.set(Calendar.SECOND, 0);

                                intent = new Intent(mContext, MyAlarm.class);
                                Bundle args = new Bundle();
                                args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                                intent.putExtra("a", args);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                            }
                        }
                    }

                    else{
                        if(hourCurrent <= hourUpdate){
                            if(minuteCurrent <= minuteFlag){
                                //Untuk masang Alarm dari inputan
                                mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate);
                                mCurrentDate.set(Calendar.MONTH, monthUpdate);
                                mCurrentDate.set(Calendar.YEAR, yearUpdate);
                                mCurrentDate.set(Calendar.HOUR_OF_DAY, hourUpdate);
                                mCurrentDate.set(Calendar.MINUTE, minuteFlag);
                                mCurrentDate.set(Calendar.SECOND, 0);

                                intent = new Intent(mContext, MyAlarm.class);
                                Bundle args = new Bundle();
                                args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                                intent.putExtra("a", args);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                            }
                        }
                    }
                }


                //Jika dia milih jam reminder di tanggal yang lebih lama dari hari
                else if(dayUpdate > dayCurrent){

                    if(minuteFlag < 0){
                        //Untuk Validasi itungan menit
                        hourFlag = hourUpdate - 1;
                        minuteUpdate = 60 + minuteFlag;

                        //Untuk masang Alarm dari inputan
                        mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate);
                        mCurrentDate.set(Calendar.MONTH, monthUpdate);
                        mCurrentDate.set(Calendar.YEAR, yearUpdate);
                        mCurrentDate.set(Calendar.HOUR_OF_DAY, hourFlag);
                        mCurrentDate.set(Calendar.MINUTE, minuteUpdate);
                        mCurrentDate.set(Calendar.SECOND, 0);

                        intent = new Intent(mContext, MyAlarm.class);
                        Bundle args = new Bundle();
                        args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                        intent.putExtra("a", args);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                    }
                    else {
                        //Untuk masang Alarm dari inputan
                        mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate);
                        mCurrentDate.set(Calendar.MONTH, monthUpdate);
                        mCurrentDate.set(Calendar.YEAR, yearUpdate);
                        mCurrentDate.set(Calendar.HOUR_OF_DAY, hourUpdate);
                        mCurrentDate.set(Calendar.MINUTE, minuteFlag);
                        mCurrentDate.set(Calendar.SECOND, 0);

                        intent = new Intent(mContext, MyAlarm.class);
                        Bundle args = new Bundle();
                        args.putParcelable(MyAlarm.EXTRA_SCHEDULE, schedule);
                        intent.putExtra("a", args);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                    }
                }
            }
        }
    }
}
