package com.example.ecommerceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView subtotal, tax, finalPrice;
    EditText firstName, lastName, address, email, number;
    Button confirm;
    RadioButton credit, debit, razor;
    String getEmail, getNumber;
    double amount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Checkout.preload(getApplicationContext());

        amount = getIntent().getIntExtra("Amount", 0);

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

        subtotal = findViewById(R.id.subTotal);
        tax = findViewById(R.id.tax);
        finalPrice = findViewById(R.id.total);
        subtotal.setText("$ "+ amount);
        finalPrice.setText(String.format("$ %.2f", amount*1.18));


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
                getEmail = Email;
                getNumber = Number;
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
                                        paymentNow();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CheckoutActivity.this, "Fill All the details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void paymentNow() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_1aEV8TjCGScExR");
        checkout.setImage(R.drawable.app);
        final Activity activity = this;
        try {

            JSONObject options = new JSONObject();

            options.put("name", "STARLIGHT");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "USD");
            amount = amount*100*1.18;
            options.put("amount", amount+"");
            options.put("prefill.email", "nandanpatel@gmail.com");
            options.put("prefill.contact","9426268778");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);
        } catch (JSONException e) {
            Log.e("TAG", "Error in starting RazorPay Checkout", e);
        }
    }
//    private List<Cartmodel> list;
//    int i;
    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CheckoutActivity.this, ProductActivity.class);
        startActivity(intent);
//        for(i = 0; i<list.size(); i++) {
//            clearCart();
//        }
    }

//    void clearCart() {
//        firestore.collection("AddtoCart").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
//                .collection("User")
//                .document(list.get(i).getDocumentId())
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            list.remove(list.get(i));
//                            notifyAll();
//                        }
//                    }
//                });
//    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failure", Toast.LENGTH_SHORT).show();
    }
}