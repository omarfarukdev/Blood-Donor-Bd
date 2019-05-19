package com.example.blooddonorbd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportActionBar().hide();
    }

    public void backBt(View view) {
        finish();
    }
}
