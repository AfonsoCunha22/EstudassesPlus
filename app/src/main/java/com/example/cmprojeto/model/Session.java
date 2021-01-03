package com.example.cmprojeto.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class Session {
    private String sessionID, subject, userID, description;
    private Date dateTime;
    private LatLng location;

    public Session(String userID, String subject, Date date, long time, LatLng location, String description) {
        this.subject = subject;
        this.dateTime = date;
        dateTime.setTime(date.getTime()+time);
        this.userID = userID;
        this.location = location;
        this.description = description;
    }
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String id) {
        this.sessionID = id;
    }

    public void setSubject(String subject) {
        subject = subject;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }
    public LatLng getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
