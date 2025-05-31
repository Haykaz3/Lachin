package com.example.myapplication;

public interface UserService {

    void registerUser(User user, UserServiceCallback callback);

    interface UserServiceCallback {
        void onUserRegistered();
        void onFailure(String error);
    }
}