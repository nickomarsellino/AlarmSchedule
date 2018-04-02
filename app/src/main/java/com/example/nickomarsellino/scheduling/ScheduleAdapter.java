package com.example.nickomarsellino.scheduling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        final Schedule schedule = mScheduleList.get(position);
        holder.scheduleTitleTxtV.setText("Title: " + schedule.getTitle());
        holder.scheduleContentxtV.setText("Content: " + schedule.getContent());
        holder.scheduleDateTxtV.setText(schedule.getDate());


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("View or Delete Schedule ?");

                builder.setPositiveButton("View Schedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goViewActivity(schedule.getId());
                    }
                });

                builder.setNegativeButton("Delete Shedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScheduleDBHelper dbHelper = new ScheduleDBHelper(mContext);
                        dbHelper.deleteSchedule(schedule.getId(), mContext);


                        mScheduleList.remove(position);
                        mRecyclerV.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mScheduleList.size());
                        notifyDataSetChanged();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
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


}
