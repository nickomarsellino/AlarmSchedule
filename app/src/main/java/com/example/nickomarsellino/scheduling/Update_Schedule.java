package com.example.nickomarsellino.scheduling;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Update_Schedule extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //untuk binding jadi
    @BindView(R.id.update_gallery)
    LinearLayout mContainerGallery;


    //Inisialisasi Atribut input
    EditText titleDataUpdate, contentDataUpdate;
    Button saveButtonUpdate;
    FloatingActionButton loadImageUpdate;
    /////////////////////////////////////////////////////

    private final static int REQ_PERMISSION = 1;
    String realPath;
    private Context mContext;

    //Untuk Database nya
    private ScheduleDBHelper dbHelper;
    private long receivedScheduleId;


    //Untuk Munculin Calendar
    TextView text_CalendarUpdate;
    TextView text_TimeUpdate;
    Calendar mCurrentDate;
    int dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate;
    /////////////////////////////////////////////////////

    //Untuk Notifikasi
    public ScheduleClient scheduleClient;
    ///////////////////////////////////////////////////////////

    //String Builder Supay bisa nyimpan path gambar supaya banyak
    StringBuilder sbPicture;
    //////////////////////////////////////////////////////


    //Supaya bisa simpen gambar lebih dari 1
    private List<String> imgs = new ArrayList<String>();
    //


    //Create Button Delete lebih dari satu
    private List<Button> delete_imgsUpdate = new ArrayList<Button>();
    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__schedule);


        //Inisialisasi Atribut input
        titleDataUpdate = findViewById(R.id.update_title);
        contentDataUpdate = findViewById(R.id.update_content);
        text_CalendarUpdate = (TextView) findViewById(R.id.update_reminder);
        text_TimeUpdate = (TextView) findViewById(R.id.update_time);
        saveButtonUpdate = (Button) findViewById(R.id.button_saveUpdate);
        loadImageUpdate = (FloatingActionButton) findViewById(R.id.fab_create_imageUpdate);


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



        //Untuk Calender & nampilin Datanya
        titleDataUpdate.setText(schedule.getTitle());
        contentDataUpdate.setText(schedule.getContent());
        text_TimeUpdate.setText(schedule.getTime());
        text_CalendarUpdate.setText(schedule.getDate());


        for(ScheduleImage img: scheduleImage){

            //Untuk COntainer di gambar dan tombol delete
            LinearLayout ContainerContentData = new LinearLayout(Update_Schedule.this);
            ContainerContentData.setOrientation(LinearLayout.VERTICAL);


            //Untuk Nampilin Gambar
            ImageView ivPictureData = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 500);
            params.gravity = Gravity.CENTER;
            params.setMargins(0, 0, 0, 50);


            //timbol button delete
            Button buttonDelete = new Button(this);
            buttonDelete.setText("Delete Image");
            LinearLayout.LayoutParams cancel = new LinearLayout.LayoutParams(600, 130);
            cancel.gravity = Gravity.CENTER;


            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(0, 0, 0, 100);

            ///////////////////////////////////////////////////////////


            Uri uriFromPath = Uri.fromFile(new File(img.getImage()));
            ivPictureData.setLayoutParams(params);
            ivPictureData.setImageURI(uriFromPath);
            buttonDelete.setLayoutParams(cancel);


            ContainerContentData.addView(buttonDelete);
            ContainerContentData.addView(ivPictureData);

            mContainerGallery.addView(ContainerContentData, param);

            imgs.add(img.getImage());
        }


        //Untuk split si Tanggal sesuai yang dibutuhkan
        String calender = schedule.getDate().toString();

        String[] separated = calender.split("-");
        String[] separatedDay = separated[0].split(": ");

        dayUpdate = Integer.parseInt(separatedDay[1]);
        yearUpdate = Integer.parseInt(separated[2]);
        monthUpdate = Integer.parseInt(separated[1]);

        //Untuk split si Time sesuai yang dibutuhkan
        String time = schedule.getTime().toString();

        String[] separatedTime = time.split(":");

        hourUpdate = Integer.parseInt(separatedTime[1].trim());
        minuteUpdate = Integer.parseInt(separatedTime[2].trim());
        ////////////////////////////////////////////////////////////////////////////////

        sbPicture = new StringBuilder();

        //Jika Tombol Add Image Di Tekan
        loadImageUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                reqPermission();

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);

            }
        });

        ///////////////////////////////////////////////////////////


        saveButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title =  titleDataUpdate.getText().toString().trim();
                String content = contentDataUpdate.getText().toString().trim();
                String date = text_CalendarUpdate.getText().toString().trim();
                String time = text_TimeUpdate.getText().toString().trim();




                Schedule updatedSchedule = new Schedule(title, content, date, time, imgs);

                dbHelper.updateSchedule(receivedScheduleId, Update_Schedule.this,  updatedSchedule);
            }
        });

    }







    //Minta Permission Untuk akses ke gallery
    public void reqPermission(){
        int reqEX = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(reqEX != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
        }
    }

    ///////////////////////////////////////////////////////////////////


    //Untuk Masukin Gambar ke Form

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == Add_Schedule.RESULT_OK && data != null){


            // SDK >= 11 && SDK < 19
            if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            setImageViews(realPath);
        }
    }


    private void setImageViews(final String realPath) {
        final Uri uriFromPath = Uri.fromFile(new File(realPath));

        //supaya dia bisa generate lebih dari 1 gambar
        final ImageView ivPicture = new ImageView(this);


        final Button buttonImg = new Button(this);
        buttonImg.setText("Delete Image");


        final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 0, 0, 150);

        final LinearLayout ContainerContent = new LinearLayout(this);
        ContainerContent.setOrientation(LinearLayout.VERTICAL);


        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 500);
        params.gravity = Gravity.CENTER;


        LinearLayout.LayoutParams cancel = new LinearLayout.LayoutParams(600, 130);
        cancel.gravity = Gravity.CENTER;

        ivPicture.setLayoutParams(params);
        ivPicture.setImageURI(uriFromPath);
        buttonImg.setLayoutParams(cancel);


        ContainerContent.addView(buttonImg);
        ContainerContent.addView(ivPicture);

        mContainerGallery.addView(ContainerContent, param);


        //Pada saat "Delete Image" di tekan
        buttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Iterator<String> iterator = imgs.iterator();
                while(iterator.hasNext())
                {
                    String value = iterator.next();
                    if (realPath.equals(value))
                    {
                        ContainerContent.removeAllViewsInLayout();
                        ContainerContent.removeAllViews();
                        mContainerGallery.removeView(ContainerContent);
                        iterator.remove();
                        break;
                    }
                }
            }

        });
        //Nyimpen Path Gambarnya ke image databasenya
        imgs.add(realPath);
    }









    //Untuk Calendar
    public void openDatePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Update_Schedule.this, Update_Schedule.this, yearUpdate, monthUpdate, dayUpdate);
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        text_CalendarUpdate.setText("Reminder For: "+day+"-"+month+"-"+year);

        TimePickerDialog timePickerDialog = new TimePickerDialog
                (Update_Schedule.this, Update_Schedule.this,hourUpdate, minuteUpdate, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        text_TimeUpdate.setText("Time: "+hour+":"+minute);

    }

    ////////////////////////////////////////////////////////////////////////////////
}
