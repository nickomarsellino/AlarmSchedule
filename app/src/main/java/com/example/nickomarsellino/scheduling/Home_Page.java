package com.example.nickomarsellino.scheduling;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home_Page extends AppCompatActivity implements RecyclerItemTouchHelperListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScheduleDBHelper dbHelper;
    private ScheduleAdapter adapter;


    //Inisialisasi Untuk Buttonnya
    private FloatingActionButton createSchedule;
    private TextView title;
    Schedule scheduleDelete;
    int positionDelete;

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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // item decoration
        // swap gesture
        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackLeft
                = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallbackLeft).attachToRecyclerView(mRecyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackRight
                = new RecyclerItemTouchHelper(0,ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallbackRight).attachToRecyclerView(mRecyclerView);


        dbHelper = new ScheduleDBHelper(this);
        adapter = new ScheduleAdapter(dbHelper.schedulesList(), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ScheduleAdapter.ViewHolder){

            positionDelete = viewHolder.getAdapterPosition();
            scheduleDelete = adapter.getPosition(positionDelete);
            adapter.remove(viewHolder.getAdapterPosition());

            AlertDialog.Builder adbuilder = new AlertDialog.Builder(Home_Page.this);
            adbuilder.setMessage("Delete ?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // remove the item from recycler view
                            Log.v("test", String.valueOf(scheduleDelete));

                        }
                    })

                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.restoreItem(scheduleDelete,positionDelete);
                            dialog.cancel();
                        }
                    })
                    .setTitle("WAIT");
            adbuilder.show();


        }
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
