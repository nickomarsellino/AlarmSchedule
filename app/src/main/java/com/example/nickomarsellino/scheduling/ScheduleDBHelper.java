package com.example.nickomarsellino.scheduling;

/**
 * Created by nicko marsellino on 3/18/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ScheduleDBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1 ;


    //Tabel Si Schedule
    public static final String TABLE_SCHEDULE_NAME = "Schedule";
    public static final String COLUMN_SCHEDULE_ID = "_id";
    public static final String COLUMN_SCHEDULE_TITLE = "title";
    public static final String COLUMN_SCHEDULE_CONTENT = "content";
    public static final String COLUMN_SCHEDULE_DATE = "date";
    public static final String COLUMN_SCHEDULE_TIME= "time";



    //Tabel si Image
    public static final String TABLE_IMAGE_NAME= "Image";
    public static final String COLUMN_IMAGE_ID = "_id";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String COLUMN_IMAGE_PATH = "path";




    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        //Ini Untuk Schedule Table
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_SCHEDULE_NAME + " (" +
                COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCHEDULE_TITLE + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_CONTENT + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_TIME + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_DATE+ " TEXT NOT NULL);"
        );

        String query = " CREATE TABLE " + TABLE_IMAGE_NAME + " (" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SCHEDULE_ID + " INTEGER," +
                COLUMN_IMAGE_PATH+ " TEXT NOT NULL," +
                "FOREIGN KEY (" + SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULE_NAME + "(" + COLUMN_SCHEDULE_ID + "))";
        //Ini Untuk Image Table
        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Ini Untuk Schedule
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_NAME);

        //Ini untuk Image
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public long saveNewSchedule(Schedule schedule) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(COLUMN_SCHEDULE_CONTENT, schedule.getContent());
        values.put(COLUMN_SCHEDULE_DATE, schedule.getDate());
        values.put(COLUMN_SCHEDULE_TIME, schedule.getTime());

        // insert
        long rowId = sqLiteDatabase.insert(TABLE_SCHEDULE_NAME,null, values);
        sqLiteDatabase.close();
        return rowId;
    }

    public void saveNewScheduleImage(ScheduleImage scheduleImage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, scheduleImage.getImage());
        values.put(SCHEDULE_ID, scheduleImage.getIdSchedule());

        // insert
        sqLiteDatabase.insert(TABLE_IMAGE_NAME,null, values);
        sqLiteDatabase.close();
    }


    public List<Schedule> schedulesList() {
        String query;

        String column = COLUMN_SCHEDULE_DATE+","+ COLUMN_SCHEDULE_TIME;
        query = "SELECT  * FROM " + TABLE_SCHEDULE_NAME + " ORDER BY "+ column;

        List<Schedule> ScheduleLinkedList = new LinkedList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Schedule schedule;


        if (cursor.moveToFirst()) {
            do {
                schedule= new Schedule();

                schedule.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_SCHEDULE_ID)));
                schedule.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE)));
                schedule.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CONTENT)));
                schedule.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TIME)));
                schedule.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_DATE)));
                ScheduleLinkedList.add(schedule);
            } while (cursor.moveToNext());
        }

        return ScheduleLinkedList;
    }

    public ScheduleImage getImageData(long id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT "+COLUMN_IMAGE_PATH+" FROM " + TABLE_IMAGE_NAME + " WHERE _id="+ id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        ScheduleImage receivedImage = new ScheduleImage();

        int idx = cursor.getColumnIndex(COLUMN_IMAGE_PATH);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            receivedImage.setImage(cursor.getString(idx));
        }
        return receivedImage;

    }

    /**Query untuk nampilin detail sesuai yang dipilih**/
    public Schedule getSchedule(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_SCHEDULE_NAME + " WHERE _id="+ id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Schedule receivedSchedule = new Schedule();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedSchedule.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE)));
            receivedSchedule.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CONTENT)));
            receivedSchedule.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_DATE)));
            receivedSchedule.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TIME)));

        }

        return receivedSchedule;
    }


    public List<ScheduleImage> getScheduleImage(long id){

        List<ScheduleImage> scheduleImages = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //kita juga disini passing id supaya kita bisa delete sesuai yang dipilih
        String queryImage = "SELECT path,_id FROM " + TABLE_IMAGE_NAME + " WHERE schedule_id="+ id;

        Cursor cursorImage = sqLiteDatabase.rawQuery(queryImage, null);


        if(cursorImage.getCount() > 0) {

            cursorImage.moveToFirst();
            for(int i=0; i<cursorImage.getCount(); i++){

                ScheduleImage receivedImage = new ScheduleImage();

                receivedImage.setImage(cursorImage.getString(cursorImage.getColumnIndex(COLUMN_IMAGE_PATH)));
                scheduleImages.add(receivedImage);

                receivedImage.setId(cursorImage.getLong(cursorImage.getColumnIndex(COLUMN_IMAGE_ID)));

                cursorImage.moveToNext();
            }

        }

        return scheduleImages;
    }



    public void deleteSchedule(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_SCHEDULE_NAME+" WHERE _id='"+id+"'");
        db.execSQL("DELETE FROM "+TABLE_IMAGE_NAME+" WHERE schedule_id='"+id+"'");

        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }


    public void deleteImageView(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_IMAGE_NAME+" WHERE _id='"+id+"'");

        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updateSchedule(long id, Context context, Schedule updatedSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_SCHEDULE_NAME+" SET title ='"+ updatedSchedule.getTitle() + "', content ='" + updatedSchedule.getContent() + "', date ='"+ updatedSchedule.getDate() + "', time ='"+ updatedSchedule.getTime() + "'  WHERE _id='" + id + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();

    }

}
