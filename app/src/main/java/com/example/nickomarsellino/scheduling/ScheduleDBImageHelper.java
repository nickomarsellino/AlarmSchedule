package com.example.nickomarsellino.scheduling;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nicko marsellino on 3/21/2018.
 */

public class ScheduleDBImageHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1 ;

    public static final String TABLE_NAME = "ScheduleImage";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMAGE = "image";



    public ScheduleDBImageHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE+ " TEXT NOT NULL);"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }



}
