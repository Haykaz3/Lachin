package com.example.myapplication;

public interface AuctionService {
    void getAuctionById(int auctionId, AuctionServiceCallback callback);

    interface AuctionServiceCallback {
        void onAuctionFound(AuctionDTO auction);
        void onFailure(String error);
    }
}
