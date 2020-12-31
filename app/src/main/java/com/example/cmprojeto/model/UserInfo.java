package com.example.cmprojeto.model;

public class UserInfo {
    private String username, password, email, description;
    private boolean isPopulated;

    public UserInfo(String username, String password, String email, String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
    }

    public UserInfo() {
        this.isPopulated = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPopulated() {
        return isPopulated;
    }

    public void populateUser(String username, String password, String email, String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;

        setPopulated();
    }

    public void clear() {
        isPopulated = false;
    }

    private void setPopulated() {
        isPopulated = true;
    }
}
