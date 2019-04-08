package com.example.blooddonorbd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DiscoverableActivity extends AppCompatActivity {
    Switch locationSw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discoverable);

        getSupportActionBar().hide();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//getting service from system
        locationSw = findViewById(R.id.locationSw);

        if (isLocationEnabled()){
            locationSw.setChecked(true);
            locationSw.setClickable(false);
        }
        locationSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(DiscoverableActivity.this, "Enabled switch", Toast.LENGTH_SHORT).show();
                    runTimePermission();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        });
    }

    private boolean runTimePermission() {
        if (Build.VERSION.SDK_INT>23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                locationSw.setChecked(true);
                Intent i = new Intent(DiscoverableActivity.this,SignInActivity.class);//dummy activity added
                startActivity(i);
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void nextBtOnDiscoverableAc(View view) {
        //FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DiscoverableActivity.this,SignInActivity.class);
        startActivity(intent);
    }

}
