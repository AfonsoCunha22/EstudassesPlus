package com.example.cmprojeto.model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

public class Session {
    private String sessionID, subject, userID, userName, description;
    private Date date;
    private Time time;
    private LatLng location;

    public Session(String userID, String subject, Date date, Time time, LatLng location, String description) {
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.userID = userID;
        this.location = location;
        this.description = description;
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
        return subject;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
