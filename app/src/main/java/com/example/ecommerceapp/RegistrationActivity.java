package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    Button button1;
    Button button2;
    EditText name;
    EditText email;
    EditText password;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        button1 = findViewById(R.id.buttonin);
        button2 = findViewById(R.id.buttonup);
        name = findViewById(R.id.editTextText);
        email = findViewById(R.id.editTextText4);
        password = findViewById(R.id.editTextTextPassword);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if(TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegistrationActivity.this, "Name Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RegistrationActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPassword.length()<6) {
                    Toast.makeText(RegistrationActivity.this, "Password too short, Enter minimum 6 characters.", Toast.LENGTH_SHORT).show();
                }
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                }
                            }
                        });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}