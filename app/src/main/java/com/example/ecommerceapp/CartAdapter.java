package com.example.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cartmodel> list;
    int total = 0;
    public CartAdapter(Context context, List<Cartmodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.newName.setText(list.get(position).getProductName());
        holder.newTotal.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.newQuantity.setText(list.get(position).getTotalQuantity());
        holder.newPrice.setText(list.get(position).getProductPrice());
        holder.newDate.setText(list.get(position).getProductDate());
        holder.newTime.setText(list.get(position).getProductTime());

        total = total + list.get(position).getTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("TotalAmount", total);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView newName, newPrice, newTime, newDate, newQuantity, newTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newName = itemView.findViewById(R.id.product_name);
            newPrice = itemView.findViewById(R.id.product_price);
            newTime = itemView.findViewById(R.id.product_time);
            newDate = itemView.findViewById(R.id.product_date);
            newQuantity = itemView.findViewById(R.id.product_quantity);
            newTotal = itemView.findViewById(R.id.product_total);
        }
    }
}
