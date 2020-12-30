package com.example.cmprojeto.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class Session {
    private String Subject;
    private Date date;
    private Time time;
    private LatLng location;

    public Session(String subject, Date date, Time time, LatLng location, String description) {
        Subject = subject;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
    }

    public Session() {
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return Subject;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    private String description;
}
