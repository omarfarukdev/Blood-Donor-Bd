package com.example.blooddonorbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
    }

    public void setupprofile(View view) {
        Intent intent =  new Intent(this, DiscoverableActivity.class);
        startActivity(intent);

    }
}
