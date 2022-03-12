package com.example.chatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatsapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(PhoneNumberActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding.phoneNum.requestFocus();
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PhoneNumberActivity.this,OTPActivity.class);
                i.putExtra("phoneNumber",binding.phoneNum.getText().toString());
                startActivity(i);
            }
        });
    }
}