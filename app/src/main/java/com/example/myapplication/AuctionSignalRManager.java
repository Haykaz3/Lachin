package com.example.myapplication;

import android.util.Log;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.TransportEnum;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class AuctionSignalRManager {
    private static final String HUB_URL = "https://hlrfbqc0-7207.euw.devtunnels.ms/auctionHub";
    private HubConnection hubConnection;

    public void connect() {
        hubConnection = HubConnectionBuilder.create(HUB_URL)
                .withTransport(TransportEnum.ALL)
                .build();

        // Listen for incoming bids
        hubConnection.on("ReceiveBid", (auctionId, userId, bidAmount) -> {
            // Handle the received bid (log or update UI)
            System.out.println("Received bid:");
            System.out.println("Auction ID: " + auctionId);
            System.out.println("User ID: " + userId);
            System.out.println("Bid Amount: " + bidAmount);

            // You can update a view model, notify the UI thread, etc.

        }, String.class, String.class, Double.class);

        Log.d("ACT", "Before start");

        hubConnection.start()
                .doOnComplete(() -> Log.d("ACT","Connected to AuctionHub!"))
                .doOnError(error -> Log.d("ACT","Connection failed: " + error.getMessage()))
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("ACT","Start to AuctionHub!");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ACT","Connected to AuctionHub!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("ACT","Connection failed: " + e.getMessage());
                    }
                });


        Log.d("ACT", "After start");
    }

    public void joinAuction(String auctionId) {
        hubConnection.invoke("JoinAuction", auctionId)
                .subscribe(() -> Log.d("ACT","Joined auction " + auctionId),
                        error -> Log.e("ACT","JoinAuction error: " + error.getMessage()));
    }

    public void sendBid(String auctionId, String user, double amount) {
        hubConnection.invoke("SendBid", auctionId, user, amount)
                .subscribe(() -> System.out.println("Bid sent!"),
                        error -> System.err.println("SendBid error: " + error.getMessage()));
    }

    public void disconnect() {
        if (hubConnection != null) {
            hubConnection.stop();
        }
    }
}
