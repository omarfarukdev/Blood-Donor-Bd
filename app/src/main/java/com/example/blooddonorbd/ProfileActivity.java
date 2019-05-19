package com.example.blooddonorbd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView username,usernumber;
    CardView message,setting,logout,notification;
    String currentnumber,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();
        username=findViewById(R.id.username);
        usernumber=findViewById(R.id.usernumber);
        message=findViewById(R.id.message);
        notification=findViewById(R.id.notification);
        setting=findViewById(R.id.setting);
        logout=findViewById(R.id.logout);

        currentnumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        usernumber.setText(currentnumber);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(currentnumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                        if (d.getKey().equals("Full Name")){
                            name=d.getValue().toString();
                            username.setText(name);
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,MessagesActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    public void edit(View view) {
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }

    public void closeBt(View view) {
        finish();
    }
}