package com.example.nickomarsellino.scheduling;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nicko marsellino on 3/18/2018.
 */

public class Schedule implements Parcelable {

    private long id;
    private String title;
    private String content;
    private String date;
    private String time;
    private List<String> images;

    public Schedule(){

    }

    public Schedule(String title, String content, String date, String time,List<String> images) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.images = images;
    }






    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) { this.time = time; }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time; }


    public Schedule(Parcel source) {
        title = source.readString();
        date = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(date);
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }

        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }
    };
}




