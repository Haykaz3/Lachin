package com.example.myapplication;


public class User {
    public String username;
    public String fullName;
    public String email;
    public String password;

    public User(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}