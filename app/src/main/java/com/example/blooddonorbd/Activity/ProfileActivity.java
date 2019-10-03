package com.example.blooddonorbd.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.blooddonorbd.R;
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
    Toolbar toolbar;
    TextView username,usernumber,bloodgroup;
    CardView message,setting,logout,help,faqs;
    String currentnumber,name,city;
    Switch prolocation;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*bloodgroup=findViewById(R.id.bloodgroup);
        prolocation=findViewById(R.id.locaswitch);*/
        username=findViewById(R.id.username);
        usernumber=findViewById(R.id.usernumber);
        message=findViewById(R.id.message);
        setting=findViewById(R.id.setting);
        faqs=findViewById(R.id.faqs);
        logout=findViewById(R.id.logout);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

        /*if (isLocationEnabled()){
            prolocation.setChecked(true);
            prolocation.setClickable(false);
        }
        prolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    runTimePermission();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        });*/
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
                        /*if(d.getKey().equals("Blood Group")){
                            bloodgroup.setText("("+d.getValue().toString()+") blood");
                        }*/
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
        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,FAQSActivity.class);
                startActivity(intent);
            }
        });
    }

    public void edit(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    /*private boolean runTimePermission() {
        if (Build.VERSION.SDK_INT>23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }
    private boolean isLocationEnabled() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
*/

}