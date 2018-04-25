package com.example.nickomarsellino.scheduling;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;




/**
 * Created by nicko marsellino on 3/18/2018.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private List<Schedule> mScheduleList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView scheduleTitleTxtV;
        public TextView scheduleContentxtV;
        public TextView scheduleDateTxtV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            scheduleTitleTxtV = (TextView) v.findViewById(R.id.title);
            scheduleContentxtV = (TextView) v.findViewById(R.id.content);
            scheduleDateTxtV = (TextView) v.findViewById(R.id.date);

        }
    }


    public void add(int position, Schedule schedule){
        mScheduleList.add(position, schedule);
        notifyItemInserted(position);
    }

    public void remove(int position){
        mScheduleList.remove(position);
        notifyItemRemoved(position);
    }

    public ScheduleAdapter(List<Schedule> myDataset, Context context, RecyclerView recyclerView){
        mScheduleList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("So... What ?");

                builder.setPositiveButton("View Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goViewActivity(schedule.getId());
                    }
                });

                builder.setNeutralButton("Delete Shedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScheduleDBHelper dbHelper = new ScheduleDBHelper(mContext);
                        dbHelper.deleteSchedule(schedule.getId(), mContext);


                        Intent myIntent = new Intent(mContext, MyAlarm.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) schedule.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

                        alarmManager.cancel(pendingIntent);

                        mScheduleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mScheduleList.size());
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Update Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goUpdateActivity(schedule.getId());
                    }
                });


                builder.create().show();
            }
        });

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


}
