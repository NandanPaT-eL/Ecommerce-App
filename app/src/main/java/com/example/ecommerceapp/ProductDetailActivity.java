package com.example.ecommerceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    FirebaseAuth auth;
    ProductObject productObject = null;
    TextView name, price, quantity;
    int totalQuantity = 1;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detailed");
        if(object instanceof ProductObject) {
            productObject = (ProductObject) object;
        }

        TextView textDesc = findViewById(R.id.textDesc);
        ImageView button1 = findViewById(R.id.button1);
        ImageView detailedImg = findViewById(R.id.detailedImg);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailedName);
        price = findViewById(R.id.price);
        Button addCart = findViewById(R.id.cartbutton);
        Button buynow = findViewById(R.id.addbutton);
        ImageView addItem = findViewById(R.id.addItem);
        ImageView removeItem = findViewById(R.id.removeItem);

        if(productObject != null) {
            Glide.with(getApplicationContext()).load(productObject.getImg_url()).into(detailedImg);
            name.setText(productObject.getName());
            textDesc.setText(productObject.getDescription());
            price.setText(String.valueOf(productObject.getPrice()));
            totalPrice = productObject.getPrice() * totalQuantity;
        }
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity < 10) {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(productObject != null) {
                        totalPrice = productObject.getPrice() * totalQuantity;
                    }
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1) {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(productObject != null) {
                        totalPrice = productObject.getPrice() * totalQuantity;
                    }
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textDesc.getVisibility() == View.GONE)
                    textDesc.setVisibility(View.VISIBLE);
                else
                    textDesc.setVisibility(View.GONE);
            }
        });
    }
    public void addtoCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("productTime", saveCurrentTime);
        cartMap.put("productDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddtoCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(ProductDetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}