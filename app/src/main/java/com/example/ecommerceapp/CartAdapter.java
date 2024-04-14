package com.example.ecommerceapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cartmodel> list;
    int total = 0;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    public CartAdapter(Context context, List<Cartmodel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.newName.setText(list.get(position).getProductName());
        holder.newTotal.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.newQuantity.setText(list.get(position).getTotalQuantity());
        holder.newPrice.setText(list.get(position).getProductPrice());
        holder.newDate.setText(list.get(position).getProductDate());

        holder.incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(list.get(position).getTotalQuantity());
                int totalprice = Integer.parseInt(list.get(position).getProductPrice());
                counter++;
                list.get(position).setTotalQuantity(String.valueOf(counter));
                list.get(position).setTotalPrice(Integer.parseInt(String.valueOf(counter*totalprice)));
                notifyDataSetChanged();
                updatePrice();
            }
        });

        holder.decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(list.get(position).getTotalQuantity());
                int totalprice = Integer.parseInt(list.get(position).getProductPrice());
                counter--;
                list.get(position).setTotalQuantity(String.valueOf(counter));
                list.get(position).setTotalPrice(Integer.parseInt(String.valueOf(counter*totalprice)));
                notifyDataSetChanged();
                updatePrice();
            }
        });

        holder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("AddtoCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .collection("User")
                        .document(list.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    list.remove(list.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Item Deleted!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Error in deleting the Item", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        updatePrice();
//        total = total + list.get(position).getTotalPrice();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updatePrice() {
        int sum=0, i;
        for(i=0; i<list.size(); i++) {
            sum += (Integer.parseInt(list.get(i).getProductPrice())*Integer.parseInt(list.get(i).getTotalQuantity()));
        }
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("TotalAmount", sum);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView newName, newPrice, newTime, newDate, newQuantity, newTotal;
        ImageView bin;
        ImageView incr, decr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newName = itemView.findViewById(R.id.product_name);
            newPrice = itemView.findViewById(R.id.product_price);
            newDate = itemView.findViewById(R.id.product_date);
            newQuantity = itemView.findViewById(R.id.product_quantity);
            newTotal = itemView.findViewById(R.id.product_total);
            bin = itemView.findViewById(R.id.bin);
            incr = itemView.findViewById(R.id.plus);
            decr = itemView.findViewById(R.id.minus);
        }
    }
}
