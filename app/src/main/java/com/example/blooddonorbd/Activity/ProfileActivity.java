package com.example.blooddonorbd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.blooddonorbd.Activity.EditProfileActivity;
import com.example.blooddonorbd.Activity.HelpActivity;
import com.example.blooddonorbd.Activity.MessagesActivity;
import com.example.blooddonorbd.Activity.SettingActivity;
import com.example.blooddonorbd.Activity.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    TextView username,usernumber;
    CardView message,setting,logout,help;
    String currentnumber,name,city;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

       // getSupportActionBar().hide();
        username=findViewById(R.id.username);
        usernumber=findViewById(R.id.usernumber);
        message=findViewById(R.id.message);
        setting=findViewById(R.id.setting);
        logout=findViewById(R.id.logout);

        help=findViewById(R.id.help);
        city = getIntent().getStringExtra("city");
        currentnumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        usernumber.setText(currentnumber);


        try {
            currentnumber=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            usernumber.setText(currentnumber);


            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(currentnumber);
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
        }catch (Exception e){}


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setTitle("Do you want to Logout ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Calendar cal = Calendar.getInstance();
                        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String currentDateandTime = sdf.format(cal.getTime());
                        FirebaseAuth.getInstance().signOut();
                        databaseReference.child("Token id").setValue("");
                        databaseReference.child("Exit time").setValue(currentDateandTime);
                        databaseReference.child("Location").setValue("Off");
                        finish();
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, HelpActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }

    public void edit(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void closeBt(View view) {
        finish();
    }
}