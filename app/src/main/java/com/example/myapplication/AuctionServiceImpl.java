package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AuctionServiceImpl implements AuctionService {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String BASE_URL = "https://tr4m9tkv-7207.euw.devtunnels.ms/";  // Change to your actual API URL
    @Override
    public void getAuctionById(int auctionId, AuctionServiceCallback callback) {
        String url = BASE_URL + "auctions/" + auctionId;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(String.valueOf(new IOException("Unexpected code " + response)));
                    return;
                }

                String json = response.body().string();
                AuctionDTO auction = gson.fromJson(json, AuctionDTO.class);
                callback.onAuctionFound(auction);
            }
        });
    }
}
