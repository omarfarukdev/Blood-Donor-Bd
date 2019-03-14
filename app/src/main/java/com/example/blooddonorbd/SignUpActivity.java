package com.example.blooddonorbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText fullNameEt,bloodGroupEt,dateOfBirthEt,phoneNumEt,passwordEt,rePasswordEt;
    Button nextBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();//hiding actionbar here

        viewIntialization();//all views initializing here
        //FirebaseAuth firebaseAuth;
        //firebaseAuth = FirebaseAuth.getInstance();

        //firebaseAuth.createUserWithEmailAndPassword(phoneNum,password)

    }

    private void viewIntialization() {
        fullNameEt = findViewById(R.id.fullNameEt);
        bloodGroupEt = findViewById(R.id.bloodGroupEt);
        dateOfBirthEt = findViewById(R.id.dateOfBirthEt);
        phoneNumEt = findViewById(R.id.phoneNumEt);
        passwordEt = findViewById(R.id.passEt);
        rePasswordEt = findViewById(R.id.rePasswordEt);
    }

    public void nextBtt(View view) {
        FirebaseAuth.getInstance().signOut();
    }
}
