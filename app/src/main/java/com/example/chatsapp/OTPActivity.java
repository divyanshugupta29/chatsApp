package com.example.chatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chatsapp.databinding.ActivityOtpactivityBinding;
import com.example.chatsapp.databinding.ActivityPhoneNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding;
    FirebaseAuth auth;
    String verificationId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();;
        auth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP .....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        binding.otpView.requestFocus();
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.phoneLbl.setText("Verify "+phoneNumber);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("Fail",e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        progressDialog.dismiss();
                        verificationId = s;
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(OTPActivity.this,SetupProfileActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }else{
                            Toast.makeText(OTPActivity.this, "Logged in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    /*------------------------------*/
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(OTPActivity.this,SetupProfileActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(OTPActivity.this, "Logged in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}