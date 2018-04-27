package com.example.nickomarsellino.scheduling;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class Home_Page extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScheduleDBHelper dbHelper;
    private ScheduleAdapter adapter;


    //Inisialisasi Untuk Buttonnya
    private FloatingActionButton createSchedule;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Insialisasi Untuk FOnt
        Typeface typeFaceTitle = Typeface.createFromAsset(getAssets(), "Raleway-SemiBold.ttf");

        //////////////////////////////////

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        //Inisialisasi Buttonya
        createSchedule = (FloatingActionButton) findViewById(R.id.fab_create_schedule);

        title = (TextView) findViewById(R.id.textView);
        title.setTypeface(typeFaceTitle);


        createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Page.this, Add_Schedule.class));
            }
        });




        //Untuk Nampilin Data dari recycle view nya.
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dbHelper = new ScheduleDBHelper(this);
        adapter = new ScheduleAdapter(dbHelper.schedulesList(), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder adbuilder = new AlertDialog.Builder(Home_Page.this);
        adbuilder.setMessage("Do You Really Want To Exit ?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })

                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setTitle("WAIT");
        adbuilder.show();
    }
}
