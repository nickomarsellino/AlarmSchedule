package com.example.nickomarsellino.scheduling;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private TextView title,content,date,time;
    private ViewPager image;

    //Inisialisasi database
    private ScheduleDBHelper dbHelper;
    private long receivedScheduleId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__detail__schedule);


        //Insialisasi Untuk FOnt
        Typeface typeFaceTitle = Typeface.createFromAsset(getAssets(), "Raleway-SemiBold.ttf");

        Typeface typeFaceContent = Typeface.createFromAsset(getAssets(), "Raleway-Light.ttf");

        Typeface typeFaceCalendar = Typeface.createFromAsset(getAssets(), "Raleway-LightItalic.ttf");

        //////////////////////////////////

        //Inisialisasi
        title = (TextView) findViewById(R.id.detail_Title);
        content = (TextView) findViewById(R.id.detail_Content);
        date = (TextView) findViewById(R.id.detail_Date);
        time = (TextView) findViewById(R.id.detail_Time);

        title.setTypeface(typeFaceTitle);
        content.setTypeface(typeFaceContent);
        date.setTypeface(typeFaceCalendar);
        time.setTypeface(typeFaceCalendar);

        //Harus ada ini
        ButterKnife.bind(this);



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
        title.setText("Title : "+schedule.getTitle());
        content.setText("Content : \n"+schedule.getContent());
        date.setText("Reminder For: "+schedule.getDate());
        time.setText("Time: "+schedule.getTime());



        for(final ScheduleImage img: scheduleImage){

            //Bikin Image View secara programaticly
            ImageView ivPicture = new ImageView(this);

            LinearLayout.LayoutParams paramImg = new LinearLayout.LayoutParams(400, 400);
            paramImg.setMargins(0, 50, 0, 50);

            //Untuk COntainer di gambar dan tombol delete
            final LinearLayout ContainerImageData = new LinearLayout(show_Detail_Schedule.this);
            ContainerImageData.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(10, 0, 10, 50);


            Uri uriFromPath = Uri.fromFile(new File(img.getImage()));
            ivPicture.setLayoutParams(paramImg);
            ivPicture.setImageURI(uriFromPath);

            ContainerImageData.addView(ivPicture);
            mContainerImage.addView(ContainerImageData, param);


            ContainerImageData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long test = img.getId();
                    Log.v("test", String.valueOf(test));

                    goViewActivity(img.getId());


                }
            });



        }

    }


    //untuk pindah ke ativity popup
    private void goViewActivity(long id) {

        Intent goToView = new Intent(getApplicationContext(), popUpImage.class);
        goToView.putExtra("IMAGE_ID", id);
        startActivity(goToView);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
