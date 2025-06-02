package com.example.myapplication.services;

import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.AttributeService;
import com.example.myapplication.AttributeDefinition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AttributeServiceImpl implements AttributeService {

    private static final String BASE_URL = "https://brkpgt18-7207.uks1.devtunnels.ms/definitions/";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void getAttributesByCategory(int categoryId, AttributeCallback callback) {
        String url = BASE_URL + categoryId;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> callback.onFailure("Network error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    mainHandler.post(() -> callback.onFailure("Server error: " + response.code()));
                    return;
                }

                String responseBody = response.body().string();
                Type listType = new TypeToken<List<AttributeDefinition>>() {}.getType();
                List<AttributeDefinition> attributes = gson.fromJson(responseBody, listType);

                mainHandler.post(() -> callback.onAttributesFetched(attributes));
            }
        });
    }
}