package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidViewHolder> {
    private List<BidDTO> bids;

    public BidAdapter(List<BidDTO> bids) {
        this.bids = bids;
    }

    public void setBids(List<BidDTO> bids) {
        this.bids = bids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid, parent, false);
        return new BidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
        BidDTO bid = bids.get(position);
        holder.tvAmount.setText("Amount: $" + bid.amount);
        holder.tvUser.setText("User ID: " + bid.userId.toString());
        holder.tvTime.setText("Placed At: " + bid.placedAt);
    }

    @Override
    public int getItemCount() {
        return bids != null ? bids.size() : 0;
    }

    public static class BidViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvUser, tvTime;

        public BidViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvBidAmount);
            tvUser = itemView.findViewById(R.id.tvBidUser);
            tvTime = itemView.findViewById(R.id.tvBidTime);
        }
    }
}
