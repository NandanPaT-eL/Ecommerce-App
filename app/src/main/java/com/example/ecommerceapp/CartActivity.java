package com.example.ecommerceapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class CartActivity extends AppCompatActivity {
    float totalBill = 0.2f;
    TextView totalBillAmt;
    RecyclerView recyclerView;
    ArrayList<Cartmodel> cartmodels;
    CartAdapter cartAdapter;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalBillAmt = findViewById(R.id.textView3);

        cartmodels = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartmodels);
        recyclerView.setAdapter(cartAdapter);

        LocalBroadcastManager.getInstance(this)
                        .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        firestore.collection("AddtoCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                Cartmodel cartmodel = doc.toObject(Cartmodel.class);
                                cartmodels.add(cartmodel);
                                cartAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalBill = intent.getIntExtra("TotalAmount", 0);
            totalBillAmt.setText("Total Cart Value: $ " + totalBill);
        }
    };
}