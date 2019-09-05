package com.example.blooddonorbd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.blooddonorbd.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //getSupportActionBar().hide();
    }

    public void getStartedBt(View view) {
        Intent intent = new Intent(this, VerifyPhoneNumberActivity.class);
        finish();
        startActivity(intent);
    }
}

