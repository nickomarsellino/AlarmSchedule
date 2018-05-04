package com.example.nickomarsellino.scheduling;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;




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


    int dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate, remindTime;
    int dayCurrent, monthCurrent, yearCurrent, hourCurrent, minuteCurrent;


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView scheduleTitleTxtV;
        public TextView scheduleContentxtV;
        public TextView scheduleDateTxtV;
        public RelativeLayout viewListData, viewBackgroundDelete;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            scheduleTitleTxtV = (TextView) v.findViewById(R.id.title);
            scheduleContentxtV = (TextView) v.findViewById(R.id.content);
            scheduleDateTxtV = (TextView) v.findViewById(R.id.date);
            viewListData = (RelativeLayout) v.findViewById(R.id.viewListData);
            viewBackgroundDelete = (RelativeLayout) v.findViewById(R.id.viewBackgroundDelete);
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

        Log.v("test", "Restore Schedule ini: "+String.valueOf(scheduleImage)+"\n");


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
        long idSchedule = dbHelper.saveNewSchedule(schedule);

        setAlarm(schedule,idSchedule);
        saveImages(idSchedule);


        Log.v("test", "Restore Schedule ini: "+String.valueOf(schedule.getTitle())+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(schedule.getContent())+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(schedule.getDate())+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(schedule.getTime())+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(schedule.getReminder())+"\n");

    }




    public ScheduleAdapter(List<Schedule> myDataset, Context context, RecyclerView recyclerView){
        mScheduleList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
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

        //////////////////////////////////



        final Schedule schedule = mScheduleList.get(position);

        holder.scheduleTitleTxtV.setTypeface(typeFaceTitle);
        holder.scheduleContentxtV.setTypeface(typeFaceContent);
        holder.scheduleDateTxtV.setTypeface(typeFaceCalendar);


        holder.scheduleTitleTxtV.setText("Title: " + schedule.getTitle());
        holder.scheduleContentxtV.setText("Content: " + schedule.getContent());
        holder.scheduleDateTxtV.setText("Reminder For: " +schedule.getDate());


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goViewActivity(schedule.getId());
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("Choose option");
//                builder.setMessage("So... What ?");
//
//                builder.setPositiveButton("View Schedule", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });

//                builder.setNeutralButton("Delete Shedule", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ScheduleDBHelper dbHelper = new ScheduleDBHelper(mContext);
//                        dbHelper.deleteSchedule(schedule.getId(), mContext);
//
//
//                        Intent myIntent = new Intent(mContext, MyAlarm.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//
//                        alarmManager.cancel(pendingIntent);
//
//                        mScheduleList.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mScheduleList.size());
//                        notifyDataSetChanged();
//                    }
//                });
//
//                builder.setNegativeButton("Update Schedule", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        goUpdateActivity(schedule.getId());
//                    }
//                });
//
//
//                builder.create().show();
                 Log.v("test", "ini: "+String.valueOf(schedule.getId())+"\n");
            }
        });

    }


    private void saveImages(long id) {
        for(final ScheduleImage img: scheduleImage){

            ScheduleImage scheduleImage = new ScheduleImage();
            scheduleImage.setIdSchedule(id);
            scheduleImage.setImage(img.getImage());

            dbHelper.saveNewScheduleImage(scheduleImage);

            Log.v("test",img.getImage());
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
        ////////////////////////////////////////////////////////////////////////////////


        Log.v("test", "Restore Schedule ini: "+String.valueOf(yearUpdate)+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(monthUpdate)+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(dayUpdate)+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(hourUpdate)+"\n");
        Log.v("test", "Restore Schedule ini: "+String.valueOf(minuteUpdate)+"\n");

    }
}
