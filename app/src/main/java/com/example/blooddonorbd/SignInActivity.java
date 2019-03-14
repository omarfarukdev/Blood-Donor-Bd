package com.example.blooddonorbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
    }

    public void signUpTv(View view) {
        Intent intent = new Intent(this,SignUpActivity.class);

        startActivity(intent);
    }
}
