package com.example.blooddonorbd.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.blooddonorbd.R;
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
    FirebaseAuth auth;
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
        linearLayout = findViewById(R.id.splachActivity);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();

//        getSupportActionBar().hide();//hiding actionbar

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
                        //network manager
                        ConnectivityManager cm = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                        if(!isConnectNetwork()){//checking network connected or not
//                           dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                                }
//                            });
//                            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    finish();
//                                }
//                            });
//                            dialog.show();
                            slowNetSlowDialog();

                        }
                        else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                            if(firebaseUser!=null){
                                takeDisitionToStartAC();
                            }else{
                                Intent intent = new Intent(SplashActivity.this,StartActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                        else{
                            switch (networkInfo.getType()){
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                    //14-64 kbps
                                    slowNetSlowDialog();
                                    onStart();
                                    break;
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                    //50-100 kbps
                                    slowNetSlowDialog();
                                    onStart();
                                    break;
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                    //50-100 kbps
                                    slowNetSlowDialog();
                                    onStart();
                                    break;
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                    //100 kbps
                                    slowNetSlowDialog();
                                    onStart();
                                    break;
                                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                                    slowNetSlowDialog();
                                    onStart();
                                    break;

                                    default:
                                        if(firebaseUser!=null){
                                            takeDisitionToStartAC();
                                        }else{
                                            Intent intent = new Intent(SplashActivity.this,StartActivity.class);
                                            finish();
                                            startActivity(intent);
                                        }
                                        break;
                            }
                        }
                            /*else {
                            if(firebaseUser!=null){
                                takeDisitionToStartAC();
                            }else{
                                Intent intent = new Intent(SplashActivity.this,StartActivity.class);
                                finish();
                                startActivity(intent);
                            }*/
                       // }
                    }
                });
            }
        };
        thread.start();
    }
    private void slowNetSlowDialog(){
        final Dialog slowNetdialog = new Dialog(SplashActivity.this);
        slowNetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        slowNetdialog.setCancelable(false);

        slowNetdialog.setContentView(R.layout.slow_net_dialog);

        Button dialogButton = (Button) slowNetdialog.findViewById(R.id.btn_dialog);
        Button settingBt = slowNetdialog.findViewById(R.id.settingbt);
        ImageButton cancelIm = slowNetdialog.findViewById(R.id.cancelImageButton);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                slowNetdialog.dismiss();
            }
        });
        settingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });

        cancelIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slowNetdialog.dismiss();
                finish();
            }
        });

        slowNetdialog.show();
    }
    void takeDisitionToStartAC(){
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
