package com.example.blooddonorbd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blooddonorbd.Service.LocationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        //Intent i = new Intent(getApplicationContext(), LocationService.class);
        //startService(i);
    }

    public void signUpTv(View view) {
       /* Intent intent = new Intent(this, SetupProfileActivity.class);

        startActivity(intent);*/
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        startService(i);
    }
}
