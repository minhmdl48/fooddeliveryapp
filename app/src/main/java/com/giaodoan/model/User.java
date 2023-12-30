package com.giaodoan.model;

public class User {
    private String uid;
    private String full_name;
    private String username;
    private String password;
    private String email;

    public User() {
    }
    public User(String uid, String full_name, String username, String password, String email) {
        this.uid = uid;
        this.full_name = full_name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
