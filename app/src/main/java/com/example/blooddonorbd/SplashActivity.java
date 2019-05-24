package com.example.blooddonorbd;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    //disabling backpress
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();

//        getSupportActionBar().hide();//hiding actionbar
        linearLayout = findViewById(R.id.splachActivity);

        //thread for launching another activity
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);//starting new activity after waiting 2000 ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(firebaseUser!=null){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getPhoneNumber());//current user references
                   // FirebaseDatabase.getInstance().setPersistenceEnabled(true);//offline capabilities
                    //databaseReference.keepSynced(true);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int count = (int) dataSnapshot.getChildrenCount();
                            //Toast.makeText(SplashActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                            if (count>=7 && !isLocationEnabled() && linearLayout.getVisibility() == View.VISIBLE){
                                Intent intent = new Intent(SplashActivity.this,DiscoverableActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                                // onDestroy();
                            }else if (count>=7 && isLocationEnabled() && linearLayout.getVisibility() == View.VISIBLE){
                                Toast.makeText(SplashActivity.this, ""+count, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                                //onDestroy();
                            }
                            else if(count<=6 && linearLayout.getVisibility() == View.VISIBLE){
                                Intent intent = new Intent(SplashActivity.this, SetupProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                                //onDestroy();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Intent intent = new Intent(SplashActivity.this,StartActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
