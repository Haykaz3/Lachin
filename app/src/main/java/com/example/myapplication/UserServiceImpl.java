package com.example.myapplication;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class UserServiceImpl implements UserService {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String BASE_URL = "https://tr4m9tkv-7207.euw.devtunnels.ms/";  // Change to your actual API URL

    @Override
    public void registerUser(User user, final UserServiceCallback callback) {
        String url = BASE_URL + "register";  // The endpoint for user registration

        // Create JSON request body
        String json = gson.toJson(user);
        RequestBody requestBody = RequestBody.create(json, okhttp3.MediaType.get("application/json"));

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .build();

        // Send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    handler.post(() -> callback.onUserRegistered());
                } else {
                    handler.post(() -> callback.onFailure("Error: " + response.code()));
                }
            }
        });
    }

    @Override
    public void login(LoginDTO userDTO, UserLoginCallback callback) {
        String url = BASE_URL + "login";  // The endpoint for user registration

        // Create JSON request body
        String json = gson.toJson(userDTO);
        RequestBody requestBody = RequestBody.create(json, okhttp3.MediaType.get("application/json"));

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .build();

        // Send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String userId = gson.fromJson(response.body().string(), String.class);
                    handler.post(() -> callback.onUserLoggedIn(userId));
                } else {
                    handler.post(() -> callback.onFailure("Error: " + response.code()));
                }
            }
        });
    }
}