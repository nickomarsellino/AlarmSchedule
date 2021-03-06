package com.example.nickomarsellino.scheduling;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    EditText titleDataUpdate, contentDataUpdate, inputMinute;
    Button saveButtonUpdate;
    FloatingActionButton loadImageUpdate;
    Spinner spinReminder;
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
    int dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate, remindTime;
    int dayCurrent, monthCurrent, yearCurrent, hourCurrent, minuteCurrent;
    int minuteFlag;
    int hourFlag;
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


    //Untuk SPinner Reminder
    String Reminder [] = {"Hour", "Day", "Minute"};
    ArrayAdapter<String> adapter;
    String flagString;
    //

    String nameMonth [] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__schedule);

        //Insialisasi Untuk FOnt
        Typeface typeFaceTitle = Typeface.createFromAsset(getAssets(), "Raleway-SemiBold.ttf");

        Typeface typeFaceContent = Typeface.createFromAsset(getAssets(), "Raleway-Light.ttf");

        Typeface typeFaceCalendar = Typeface.createFromAsset(getAssets(), "Raleway-LightItalic.ttf");
        //////////////////////////////////


        mCurrentDate = Calendar.getInstance();

        //get current date and time for validation
        dayCurrent = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        monthCurrent = mCurrentDate.get(Calendar.MONTH);
        yearCurrent = mCurrentDate.get(Calendar.YEAR);
        hourCurrent = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        minuteCurrent = mCurrentDate.get(Calendar.MINUTE);




        //Inisialisasi Atribut input
        titleDataUpdate = findViewById(R.id.update_title);
        contentDataUpdate = findViewById(R.id.update_content);
        text_CalendarUpdate = (TextView) findViewById(R.id.update_reminder);
        text_TimeUpdate = (TextView) findViewById(R.id.update_time);
        saveButtonUpdate = (Button) findViewById(R.id.button_saveUpdate);
        inputMinute = (EditText) findViewById(R.id.input_minute);
        loadImageUpdate = (FloatingActionButton) findViewById(R.id.fab_create_imageUpdate);


        spinReminder = (Spinner) findViewById(R.id.spinReminder);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Reminder);
        spinReminder.setAdapter(adapter);

        titleDataUpdate.setTypeface(typeFaceTitle);
        contentDataUpdate.setTypeface(typeFaceContent);



        //Harus ada ini
        ButterKnife.bind(this);


        dbHelper = new ScheduleDBHelper(this);

        try{
            receivedScheduleId = getIntent().getLongExtra("USER_ID", 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Get Data Dari Id nya
        final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);
        final List<ScheduleImage> scheduleImage = dbHelper.getScheduleImage(receivedScheduleId);


        //Untuk Calender & nampilin Datanya
        titleDataUpdate.setText(schedule.getTitle());
        contentDataUpdate.setText(schedule.getContent());

        text_TimeUpdate.setTypeface(typeFaceCalendar);
        text_CalendarUpdate.setTypeface(typeFaceCalendar);



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


        monthName =  viewMonth(monthUpdate);

        text_CalendarUpdate.setText("Reminder For: "+dayUpdate+"-"+monthName+"-"+yearUpdate);
        text_TimeUpdate.setText("Time: "+schedule.getTime());



        //Untuk ngecek apakah spinnernya diilih atau tidak
        spinReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        flagString = "hour";
                        break;
                    case 1:
                        flagString = "day";
                        break;
                    case 2:
                        flagString = "minute";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        for(final ScheduleImage img: scheduleImage){

            //Untuk COntainer di gambar dan tombol delete
            final LinearLayout ContainerContentData = new LinearLayout(Update_Schedule.this);
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


            //Pada saat "Delete Image" Yang untuk view gambar yang di database di tekan
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContainerContentData.removeAllViewsInLayout();
                    ContainerContentData.removeAllViews();

                    mContainerGallery.removeView(ContainerContentData);


                    long test = img.getId();

                    dbHelper.deleteImageView(test, Update_Schedule.this);

                }
            });
        }


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
                saveUpdateImage();
            }
        });

    }

    public void saveUpdateImage(){
        String title =  titleDataUpdate.getText().toString().trim();
        String content = contentDataUpdate.getText().toString().trim();
        String date =  String.valueOf(yearUpdate)+"-"+ String.valueOf(monthUpdate)+"-"+ String.valueOf(dayUpdate);
        String time = String.valueOf(hourUpdate)+":"+ String.valueOf(minuteUpdate);
        remindTime = Integer.parseInt(inputMinute.getText().toString().trim());
        String reminder;




        if(flagString.equals("hour")){
            if(remindTime > 0 && remindTime < 24){

                reminder = remindTime + " hour";

                //Untuk Save Data di tabel Schedule

                Schedule updatedSchedule = new Schedule(title, content, reminder, date, time, imgs);
                dbHelper.updateSchedule(receivedScheduleId, Update_Schedule.this,  updatedSchedule);


                //Untuk add gambar baru ke tabel Schedule Image
                //foreach untuk nyimpen datanya sesuai banyak yang dimasukin
                for(String img:updatedSchedule.getImages()){

                    ScheduleImage scheduleImage = new ScheduleImage();
                    scheduleImage.setIdSchedule(receivedScheduleId);
                    scheduleImage.setImage(img);

                    dbHelper.saveNewScheduleImage(scheduleImage);
                }

                if(yearCurrent <= yearUpdate && monthCurrent <= monthUpdate && dayCurrent<= dayUpdate && hourCurrent <= (hourUpdate-remindTime) && minuteCurrent <= minuteUpdate){
                    //Untuk masang Alarm dari inputan
                    mCurrentDate.set(Calendar.DAY_OF_MONTH,dayUpdate);
                    mCurrentDate.set(Calendar.MONTH,monthUpdate);
                    mCurrentDate.set(Calendar.YEAR,yearUpdate);
                    mCurrentDate.set(Calendar.HOUR_OF_DAY,hourUpdate - remindTime);
                    mCurrentDate.set(Calendar.MINUTE,minuteUpdate);
                    mCurrentDate.set(Calendar.SECOND,0);


                    final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                    Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                    Bundle args = new Bundle();
                    args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                    intent.putExtra("a", args);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                }

                startActivity(new Intent(Update_Schedule.this, Home_Page.class));
            }
            else{
                Toast.makeText(this, "Please Change To Day", Toast.LENGTH_SHORT).show();
            }
        }

        else if (flagString.equals("day")){
            if(remindTime > 0) {

                reminder = remindTime + " day";

                //Untuk Save Data di tabel Schedule

                Schedule updatedSchedule = new Schedule(title, content, reminder, date, time, imgs);
                dbHelper.updateSchedule(receivedScheduleId, Update_Schedule.this,  updatedSchedule);


                //Untuk add gambar baru ke tabel Schedule Image
                //foreach untuk nyimpen datanya sesuai banyak yang dimasukin
                for(String img:updatedSchedule.getImages()){

                    ScheduleImage scheduleImage = new ScheduleImage();
                    scheduleImage.setIdSchedule(receivedScheduleId);
                    scheduleImage.setImage(img);

                    dbHelper.saveNewScheduleImage(scheduleImage);
                }


                if(yearCurrent <= yearUpdate && monthCurrent <= monthUpdate && dayCurrent <= (dayUpdate-remindTime)) {
                    //Untuk masang Alarm dari inputan
                    mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate - remindTime);
                    mCurrentDate.set(Calendar.MONTH, monthUpdate);
                    mCurrentDate.set(Calendar.YEAR, yearUpdate);
                    mCurrentDate.set(Calendar.HOUR_OF_DAY, hourUpdate);
                    mCurrentDate.set(Calendar.MINUTE, minuteUpdate);
                    mCurrentDate.set(Calendar.SECOND, 0);


                    final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                    Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                    Bundle args = new Bundle();
                    args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                    intent.putExtra("a", args);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                }

                startActivity(new Intent(Update_Schedule.this, Home_Page.class));
            }
            else{
                Toast.makeText(this, "Please Change To Day", Toast.LENGTH_SHORT).show();
            }
        }


        else if (flagString.equals("minute")){
            if(remindTime > 0) {

                reminder = remindTime + " minute";

                //Untuk Save Data di tabel Schedule
                Schedule updatedSchedule = new Schedule(title, content, reminder, date, time, imgs);
                dbHelper.updateSchedule(receivedScheduleId, Update_Schedule.this,  updatedSchedule);


                //Untuk add gambar baru ke tabel Schedule Image
                //foreach untuk nyimpen datanya sesuai banyak yang dimasukin
                for(String img:updatedSchedule.getImages()){

                    ScheduleImage scheduleImage = new ScheduleImage();
                    scheduleImage.setIdSchedule(receivedScheduleId);
                    scheduleImage.setImage(img);

                    dbHelper.saveNewScheduleImage(scheduleImage);
                }


                if(yearCurrent <= yearUpdate && monthCurrent <= monthUpdate) {

                    minuteFlag = minuteUpdate-remindTime;

                    //Jika ia memilih minute tetapui di tangal yg sama
                    if(dayCurrent == dayUpdate){

                        //jika hasil input menit bisa merubah jam
                        if(minuteFlag < 0){
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


                                    final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                                    Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                                    Bundle args = new Bundle();
                                    args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                                    intent.putExtra("a", args);

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                                }
                            }
                        }

                        else{
                            if(minuteCurrent <= minuteFlag){
                                //Untuk masang Alarm dari inputan
                                mCurrentDate.set(Calendar.DAY_OF_MONTH, dayUpdate);
                                mCurrentDate.set(Calendar.MONTH, monthUpdate);
                                mCurrentDate.set(Calendar.YEAR, yearUpdate);
                                mCurrentDate.set(Calendar.HOUR_OF_DAY, hourUpdate);
                                mCurrentDate.set(Calendar.MINUTE, minuteFlag);
                                mCurrentDate.set(Calendar.SECOND, 0);

                                final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                                Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                                Bundle args = new Bundle();
                                args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                                intent.putExtra("a", args);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
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

                            final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                            Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                            Bundle args = new Bundle();
                            args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                            intent.putExtra("a", args);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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

                            final Schedule schedule = dbHelper.getSchedule(receivedScheduleId);


                            Intent intent = new Intent(Update_Schedule.this, MyAlarm.class);
                            Bundle args = new Bundle();
                            args.putParcelable(MyAlarm.EXTRA_SCHEDULE, updatedSchedule);
                            intent.putExtra("a", args);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(Update_Schedule.this, (int) schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmMgr.set(AlarmManager.RTC_WAKEUP,mCurrentDate.getTimeInMillis(), pendingIntent);
                        }
                    }
                }
                finish();
//                startActivity(new Intent(Update_Schedule.this, Home_Page.class));
            }
            else{
                Toast.makeText(this, "Please Change To Day", Toast.LENGTH_SHORT).show();
            }
        }


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

        monthName =  viewMonth(month);

        text_CalendarUpdate.setText("Reminder For: "+day+"-"+monthName+"-"+year);

        dayUpdate = day;
        monthUpdate = month;
        yearUpdate = year;

        TimePickerDialog timePickerDialog = new TimePickerDialog
                (Update_Schedule.this, Update_Schedule.this,hourUpdate, minuteUpdate, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        text_TimeUpdate.setText("Time: "+hour+":"+minute);

        minuteUpdate = minute;
        hourUpdate = hour;
    }

    ////////////////////////////////////////////////////////////////////////////////


    public String viewMonth(int month){
        String monthView = nameMonth[month];

        return monthView;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adbuilder = new AlertDialog.Builder(Update_Schedule.this);
        adbuilder.setMessage("Do You Really Want To Exit ?")
                .setCancelable(false)
                .setPositiveButton("Save Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveUpdateImage();
                    }
                })

                .setNegativeButton("Discard Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setTitle("WAIT");
        adbuilder.show();
    }

}
