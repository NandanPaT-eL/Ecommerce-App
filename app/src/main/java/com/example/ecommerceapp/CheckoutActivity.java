package com.example.ecommerceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.Map;


public class CheckoutActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    EditText firstName, lastName, address, email, number;
    Button confirm;
    RadioButton credit, debit, razor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);

        confirm = findViewById(R.id.placeOrder);

        credit = findViewById(R.id.credit);
        debit = findViewById(R.id.debit);
        razor = findViewById(R.id.razorpay);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Firstname = firstName.getText().toString();
                String Lastname = lastName.getText().toString();
                String Address = address.getText().toString();
                String Email = email.getText().toString();
                String Number = number.getText().toString();
                String c = credit.getText().toString();
                String d = debit.getText().toString();
                String r = razor.getText().toString();
                String fullname = "";
                if(!Firstname.isEmpty()) {
                    fullname+=Firstname;
                    fullname+=" ";
                }
                if(!Lastname.isEmpty()) {
                    fullname+=Lastname;
                }

                if (!Firstname.isEmpty() && !Lastname.isEmpty() && !Address.isEmpty() && !Email.isEmpty() && !Number.isEmpty()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("FullName", fullname);
                    map.put("Address", Address);
                    map.put("Email",Email);
                    map.put("Number", Number);
                    if (credit.isChecked()) {
                        map.put("Payment Method", c);
                    } else if (debit.isChecked()) {
                        map.put("Payment method", d);
                    } else if (razor.isChecked()) {
                        map.put("Payment method", r);
                    }
                    firestore.collection("UserCheckout").document(auth.getCurrentUser().getUid())
                            .collection("Details").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(CheckoutActivity.this,"Record Stored",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CheckoutActivity.this, "Fill All the details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}