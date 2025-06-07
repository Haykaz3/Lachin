package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class AuctionActivity extends AppCompatActivity {

    private HubConnection hubConnection;
    private static final String TAG = "AuctionActivity";
    private AuctionService auctionService;
    private final String AUCTION_ID = "1";
    private final String USER_ID = "80df53cc-c6c9-4b9f-a4b6-9a95732542dc";
    private TextView tvAuctionInfo;
    private EditText bidAmount;
    private RecyclerView rvBids;
    private BidAdapter bidAdapter;
    private List<BidDTO> bids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);
        tvAuctionInfo = findViewById(R.id.tvAuctionInfo);
        rvBids = findViewById(R.id.rvBids);

        bidAdapter = new BidAdapter(new ArrayList<>());
        rvBids.setLayoutManager(new LinearLayoutManager(this));
        rvBids.setAdapter(bidAdapter);
        auctionService = new AuctionServiceImpl();
        loadAuction(Integer.parseInt(AUCTION_ID));
        Button placeBidButton = findViewById(R.id.placeBid);
        bidAmount = findViewById(R.id.amount);

        // Create the HubConnection
        hubConnection = HubConnectionBuilder.create("https://hlrfbqc0-7207.euw.devtunnels.ms/auctionHub")
                .withTransport(com.microsoft.signalr.TransportEnum.WEBSOCKETS)
                .build();

        // Set up ReceiveBid handler
        hubConnection.on("ReceiveBid", (auctionId, userId, bidAmount) -> {
            Log.d(TAG, "Received bid: auctionId=" + auctionId + ", userId=" + userId + ", amount=" + bidAmount);
            BidDTO bidDTO = new BidDTO();
            bidDTO.id = 0;
            bidDTO.amount = bidAmount;
            bidDTO.auctionId = Integer.parseInt(auctionId);
            bidDTO.userId = userId;
            bidDTO.placedAt = "asdasdasd";
            bids.add(bidDTO);
            runOnUiThread(() -> bidAdapter.notifyDataSetChanged());
        }, String.class, String.class, Integer.class);

        // Start connection
        hubConnection.start().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Connecting to AuctionHub...");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Connected to AuctionHub");

                // Join the auction
                hubConnection.invoke("JoinAuction", AUCTION_ID)
                        .doOnComplete(() -> Log.d(TAG, "Joined auction: " + AUCTION_ID))
                        .doOnError(error -> Log.e(TAG, "Error joining auction", error))
                        .subscribe();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Connection failed", e);
            }
        });

        // Handle button click
        placeBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hubConnection != null && hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                    hubConnection.invoke("SendBid", AUCTION_ID, USER_ID, Integer.parseInt(bidAmount.getText().toString()))
                            .doOnComplete(() -> Log.d(TAG, "Bid placed successfully"))
                            .doOnError(error -> Log.e(TAG, "Error placing bid", error))
                            .subscribe();
                } else {
                    Log.w(TAG, "SignalR not connected yet");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hubConnection != null) {
            hubConnection.stop();
        }
    }
    private void loadAuction(int auctionId)
    {
        auctionService.getAuctionById(auctionId, new AuctionService.AuctionServiceCallback() {
            @Override
            public void onAuctionFound(AuctionDTO auction) {
                runOnUiThread(() -> {
                    tvAuctionInfo.setText(
                            "Auction ID: " + auction.id + "\n" +
                                    "Product ID: " + auction.productId + "\n" +
                                    "Start: " + auction.startDate + "\n" +
                                    "End: " + auction.endDate + "\n" +
                                    "Active: " + auction.isActive
                    );
                    bids = auction.bids;
                    bidAdapter.setBids(bids);
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("AuctionActivity", error);
            }
        });
    }
}