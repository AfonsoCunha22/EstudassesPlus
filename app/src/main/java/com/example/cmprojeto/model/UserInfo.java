package com.example.cmprojeto.model;

import android.net.Uri;

public class UserInfo {
    private String username, password, email, description;
    private boolean isPopulated;
    private byte[] avatar;

    public UserInfo(String username, String password, String email, String description, byte[] avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.avatar = avatar;
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

    public byte[] getAvatar() { return avatar; }

    public boolean isPopulated() {
        return isPopulated;
    }

    public void populateUser(String username, String password, String email, String description, byte[] avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.description = description;
        this.avatar = avatar;

        setPopulated();
    }

    public void clear() {
        isPopulated = false;
    }

    private void setPopulated() {
        isPopulated = true;
    }
}
