package com.example.myapplication;

public interface UserService {

    void registerUser(User user, UserServiceCallback callback);
    void login(LoginDTO userDTO, UserLoginCallback callback);
    interface UserServiceCallback {
        void onUserRegistered();
        void onFailure(String error);
    }
    interface UserLoginCallback {
        void onUserLoggedIn(String userId);
        void onFailure(String error);
    }
}