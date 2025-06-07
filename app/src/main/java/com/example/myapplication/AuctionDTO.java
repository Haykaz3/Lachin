package com.example.myapplication;
import java.util.List;

public class AuctionDTO {
    public int id;
    public String startDate;
    public String endDate;
    public int productId;
    public boolean isActive;
    public List<BidDTO> bids;
}
