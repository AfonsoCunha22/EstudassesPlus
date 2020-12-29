package com.example.cmprojeto;

public class User {
    private String email, username, description, uID, password, language;
    private boolean sessionStartNotifications, studyStartNotifications, studyBreakNotifications, studyEndNotification, lightSensor, tempSensor, allowLocalization;

    public User(String username, String email, String description, String uID, String password) {
        this.username = username;
        this.email = email;
        this.description = description;
        this.uID = uID;
        this.password = password;
    }

    public User() { }

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getuID() {
        return uID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isSessionStartNotifications() {
        return sessionStartNotifications;
    }

    public boolean isStudyStartNotifications() {
        return studyStartNotifications;
    }

    public boolean isStudyBreakNotifications() {
        return studyBreakNotifications;
    }

    public boolean isStudyEndNotification() {
        return studyEndNotification;
    }

    public boolean isLightSensor() {
        return lightSensor;
    }

    public boolean isTempSensor() {
        return tempSensor;
    }

    public boolean isAllowLocalization() {
        return allowLocalization;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSessionStartNotifications(boolean sessionStartNotifications) {
        this.sessionStartNotifications = sessionStartNotifications;
    }

    public void setStudyStartNotifications(boolean studyStartNotifications) {
        this.studyStartNotifications = studyStartNotifications;
    }

    public void setStudyBreakNotifications(boolean studyBreakNotifications) {
        this.studyBreakNotifications = studyBreakNotifications;
    }

    public void setStudyEndNotification(boolean studyEndNotification) {
        this.studyEndNotification = studyEndNotification;
    }

    public void setLightSensor(boolean lightSensor) {
        this.lightSensor = lightSensor;
    }

    public void setTempSensor(boolean tempSensor) {
        this.tempSensor = tempSensor;
    }

    public void setAllowLocalization(boolean allowLocalization) {
        this.allowLocalization = allowLocalization;
    }

    public String getUserInfo(){
        return "Dados de utilizador:\n\n"+
                "Username: "+ getUsername() +
                "\nEmail: "+ getEmail()+
                "\nPassword: "+ getPassword()+
                "\nuID: "+ getuID()+
                "\nDescription: "+ getDescription();
    }

    public void clearData(){
        username = "";
        email = "";
        password = "";
        uID = "";
        description = "";
    }
}
