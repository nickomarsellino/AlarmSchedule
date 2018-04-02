package com.example.nickomarsellino.scheduling;

import java.util.List;

/**
 * Created by nicko marsellino on 3/18/2018.
 */

public class Schedule {

    private long id;
    private String title;
    private String content;
    private String date;
    private List<String> images;

    public Schedule(){

    }

    public Schedule(String title, String content, String date, List<String> images) {
        this.title = title;
        this.content = content;
        this.date = date;
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

}
