package com.example.blooddonorbd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
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

        final AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
        dialog.setTitle("Network error!");
        dialog.setMessage("Check your network setting and try again.");
        dialog.setCancelable(false);
       // dialog.setCanceledOnTouchOutside(false);
        //thread for launching another activity
        final Thread thread = new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);//starting new activity after waiting 2000 ms
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ConnectivityManager cm = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        if(!isConnectNetwork()){
                            dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                }
                            });
                            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();
                        }else {
                            if(firebaseUser!=null){
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getPhoneNumber());//current user references
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int count = (int) dataSnapshot.getChildrenCount();
                                        Log.d("iiii", String.valueOf(dataSnapshot.hasChild("Full Name")));
                                        if (count>=8 && !isLocationEnabled() && linearLayout.getVisibility() == View.VISIBLE){
                                            Intent intent = new Intent(SplashActivity.this,DiscoverableActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            finish();
                                            startActivity(intent);
                                            // onDestroy();
                                        }else if (count>=8 && isLocationEnabled() && linearLayout.getVisibility() == View.VISIBLE && dataSnapshot.hasChild("Full Name") && dataSnapshot.hasChild("Blood Group")){
                                            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                @Override
                                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                                    databaseReference.child("Token id").setValue(instanceIdResult.getToken());

                                                }
                                            });
                                            finish();
                                            startActivity(intent);
                                            //onDestroy();
                                        }
                                        else if(linearLayout.getVisibility() == View.VISIBLE && !dataSnapshot.hasChild("Full Name") && !dataSnapshot.hasChild("Blood Group")){
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
                    }
                });
            }
        };
        thread.start();
    }

    //network connection check
    public boolean isConnectNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
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
