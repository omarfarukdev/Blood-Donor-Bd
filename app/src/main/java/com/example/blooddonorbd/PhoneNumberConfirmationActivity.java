package com.example.blooddonorbd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneNumberConfirmationActivity extends AppCompatActivity {
    EditText codeEt;
    private String verificationId;
    ProgressDialog progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_confirmation);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        codeEt = findViewById(R.id.codeEt);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phoneNumber);
        //test code from other devise
        if(codeEt.getText().toString().length()==6){
            verifyVerificationCode(codeEt.getText().toString());
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                codeEt.setText(code);
                verifyVerificationCode(code);
                progressBar.dismiss();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneNumberConfirmationActivity.this, "Invalid phone number try again"+e, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PhoneNumberConfirmationActivity.this,VerifyPhoneNumberActivity.class);
            finish();
            startActivity(intent);
            //Log.d("exception",e.toString());
            progressBar.dismiss();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
           // super.onCodeSent(s, forceResendingToken);
           // Toast.makeText(PhoneNumberConfirmationActivity.this, "Code sent called", Toast.LENGTH_SHORT).show();
            verificationId = s;
            //progressBar.dismiss();
        }
    };
    public void sendVerificationCode(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback
        );
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Sending verification code");
        progressBar.show();
    }

    private void verifyVerificationCode(String code){
        //creating credential
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(PhoneNumberConfirmationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(task.getResult().getUser().getUid());
                            Toast.makeText(PhoneNumberConfirmationActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneNumberConfirmationActivity.this,SignUpActivity.class);
                            databaseReference.child("Phone number").setValue(task.getResult().getUser().getPhoneNumber());
                            startActivity(intent);
                        }
                    }
                });
    }
}
