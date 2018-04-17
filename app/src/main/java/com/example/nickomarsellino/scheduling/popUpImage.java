package com.example.nickomarsellino.scheduling;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class popUpImage extends Activity {


    ImageView imageView;

    //Inisialisasi database
    private ScheduleDBHelper dbHelper;
    private long receivedImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_image);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //inisialisasi
        imageView = (ImageView) findViewById(R.id.imgData);


        //Untuk get Id yang dilempar dari show_Detail_Schedule.java
        try{
            receivedImageId = getIntent().getLongExtra("IMAGE_ID", 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        dbHelper = new ScheduleDBHelper(this);
        ScheduleImage scheduleImage = (ScheduleImage) dbHelper.getImageData(receivedImageId);

        Uri uriFromPath = Uri.fromFile(new File(scheduleImage.getImage()));
        imageView.setImageURI(uriFromPath);



    }
}
