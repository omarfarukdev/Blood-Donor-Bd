package com.example.blooddonorbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DiscoverableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discoverable);

    }

    public void discoverable(View view) {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}