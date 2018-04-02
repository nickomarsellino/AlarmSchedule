package com.example.nickomarsellino.scheduling;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.File;
import java.util.List;

import butterknife.BindView;

public class show_Detail_Schedule extends AppCompatActivity {


    //untuk binding jadi
    @BindView(R.id.container_image) LinearLayout mContainerImage;

    //Inisialisasi Atribut
    private TextView title,content,date;
    private ImageView image;

    //Inisialisasi database
    private ScheduleDBHelper dbHelper;
    private long receivedScheduleId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__detail__schedule);



        //Inisialisasi
        title = (TextView) findViewById(R.id.detail_Title);
        content = (TextView) findViewById(R.id.detail_Content);
        date = (TextView) findViewById(R.id.detail_Date);

        //Harus ada ini
        ButterKnife.bind(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 400);
        params.setMargins(0, 50, 0, 50);




        dbHelper = new ScheduleDBHelper(this);

        try{
            receivedScheduleId = getIntent().getLongExtra("USER_ID", 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Get Data Dari Id nya
        Schedule schedule = dbHelper.getSchedule(receivedScheduleId);
        List<ScheduleImage> scheduleImage = dbHelper.getScheduleImage(receivedScheduleId);




        //nampilin Datanya
        title.setText(schedule.getTitle());
        content.setText(schedule.getContent());
        date.setText(schedule.getDate());


        for(ScheduleImage img: scheduleImage){

            ImageView ivPicture = new ImageView(this);
            Uri uriFromPath = Uri.fromFile(new File(img.getImage()));
            ivPicture.setLayoutParams(params);
            ivPicture.setImageURI(uriFromPath);

            mContainerImage.addView(ivPicture);
        }


//        Uri uriFromPath = Uri.fromFile(new File(schedule.getImage()));
//
//        image.setImageURI(uriFromPath);

    }


}
