package com.example.nickomarsellino.scheduling;

/**
 * Created by nicko marsellino on 3/21/2018.
 */

public class ScheduleImage {

    private long id;
    private String image;
    private long idSchedule;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(long idSchedule) {
        this.idSchedule = idSchedule;
    }

    public ScheduleImage(){

    }

    public ScheduleImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
