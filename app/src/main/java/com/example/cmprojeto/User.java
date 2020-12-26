package com.example.cmprojeto;

public class User {
    private String email, username;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(){

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
}
