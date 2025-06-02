package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class CategoryServiceImpl implements CategoryService {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String BASE_URL = "https://brkpgt18-7207.uks1.devtunnels.ms/";  // Change to your actual API URL

    @Override
    public void getCategories(final CategoryServiceCallback callback) {
        String url = BASE_URL + "categories";  // Assuming the endpoint to fetch categories is `/categories`

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Read response body
                        String json = response.body().string();

                        // Parse JSON into List<Category> using TypeToken
                        Type categoryListType = new TypeToken<List<Category>>() {}.getType();
                        List<Category> categories = gson.fromJson(json, categoryListType);

                        // Post result to callback on main thread
                        handler.post(() -> callback.onCategoriesFetched(categories));
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> callback.onFailure("Parsing failed: " + e.getMessage()));
                    }
                } else {
                    handler.post(() -> callback.onFailure("Server error: " + response.code()));
                }
            }
        });
    }
}
