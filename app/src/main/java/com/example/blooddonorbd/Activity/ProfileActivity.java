package com.example.blooddonorbd.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.blooddonorbd.Classes.ReadMessage;
import com.example.blooddonorbd.R;
import com.example.blooddonorbd.Service.LocationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                Intent intent=new Intent(ProfileActivity.this,MessagesActivity.class);
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
               /* AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setTitle("Do you want to Logout ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProfileActivity.this,SplashActivity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Calendar cal = Calendar.getInstance();
                        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String currentDateandTime = sdf.format(cal.getTime());
                        FirebaseAuth.getInstance().signOut();
                        databaseReference.child("Token id").setValue("");
                        databaseReference.child("Active time").setValue(currentDateandTime);
                        databaseReference.child("Location").setValue("Off");
                        Intent myService = new Intent(ProfileActivity.this, LocationService.class);
                        stopService(myService);
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
                dialog.show();*/
               logoutDialog();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,HelpActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }

    public void edit(View view) {
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadMessage readMessage = new ReadMessage(this,2);
        readMessage.readUser();
    }

    public void closeBt(View view) {
        finish();
    }

    private void logoutDialog(){
        final Dialog exitDialog = new Dialog(ProfileActivity.this);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //exitDialog.setCancelable(false);

        exitDialog.setContentView(R.layout.exit_logout_dialog);

        Button dialogButton = (Button) exitDialog.findViewById(R.id.dilog_yesBt);
        ImageButton cancelIm = exitDialog.findViewById(R.id.cancelImageButton);
        TextView textView = exitDialog.findViewById(R.id.dialogTitle);
        TextView textView1 = exitDialog.findViewById(R.id.desText_dialog);
        textView.setText("Logout!");
        textView1.setText("Do you really want to logout ?");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,SplashActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Calendar cal = Calendar.getInstance();
                DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(cal.getTime());
                FirebaseAuth.getInstance().signOut();
                databaseReference.child("Token id").setValue("");
                databaseReference.child("Active time").setValue(currentDateandTime);
                databaseReference.child("Location").setValue("Off");
                Intent myService = new Intent(ProfileActivity.this, LocationService.class);
                stopService(myService);
                finish();
                exitDialog.dismiss();
                startActivity(intent);

            }
        });
        cancelIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        exitDialog.show();
    }
}