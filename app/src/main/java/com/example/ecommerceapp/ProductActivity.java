package com.example.ecommerceapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;



public class ProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ProductObject> productObjects;
    ProductAdapter productAdapter;
    ImageView imageView;
    ImageView imageView1;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        productObjects = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productObjects);
        recyclerView.setAdapter(productAdapter);
        imageView = findViewById(R.id.imageView3);
        imageView1 = findViewById(R.id.imageView4);

        db.collection("ProductList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductObject productObject = document.toObject(ProductObject.class);
                                productObjects.add(productObject);
                                productAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(ProductActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductActivity.this, CartActivity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(ProductActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}