package com.example.cmprojeto;

public class User {
    private String email, username, description, uID;

    public User(String username, String email, String description, String uID) {
        this.username = username;
        this.email = email;
        this.description = description;
        this.uID = uID;
    }

    public User(){

    }


    public String getDescription() {
        return description;
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

    public String getUserInfo(){
        return "Dados de utilizador:\n\n"+
                "Username: "+ getUsername() +
                "\nEmail: "+ getEmail()+
                "\nuID: "+ getuID()+
                "\nDescription: "+ getDescription();
    }

    public void clearData(){
        username = "";
        email = "";
        uID = "";
        description = "";
    }
}
