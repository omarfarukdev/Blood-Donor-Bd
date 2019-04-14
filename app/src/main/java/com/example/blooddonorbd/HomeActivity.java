package com.example.blooddonorbd;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Service.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity implements LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private FusedLocationProviderClient client;
    TextView currentLocationTv, donotNumberTv;
    String fullAddress, city, country, state,road;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentLocationTv = findViewById(R.id.currentlocation);
        donotNumberTv = findViewById(R.id.donorNumberTv);

        Intent i = new Intent(getApplicationContext(), LocationService.class);
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        startService(i);

        //database reference
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        donotNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "" + fullAddress, Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//fusedLocation provider client

        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10: {
                getLocation();
            }
            break;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Toast.makeText(HomeActivity.this, "fused location", Toast.LENGTH_SHORT).show();
                        if (location != null) {
                            // Logic to handle location object

                            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                            List<Address> addresses;
                            ArrayList<String> addressList = new ArrayList<>();
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                Address obj = addresses.get(0);

                                final String fullAddress = obj.getAddressLine(0);

                                String[] sp = fullAddress.split(",");

                                for(int i=0;i<sp.length;i++){
                                    addressList.add(sp[i]);
                                }
                                currentLocationTv.setText(fullAddress);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
                                DatabaseReference databaseReferenceForBloodGroup = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("Blood Group");

                                databaseReferenceForBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Toast.makeText(HomeActivity.this, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                country = addressList.get(addressList.size()-1);
                                city = addressList.get(addressList.size()-2);
                                state = addressList.get(addressList.size()-3);

                                try{
                                   road = addressList.get(addressList.size()-4);
                                }catch (Exception e){
                                    road = "Null";
                                }
                                //reading value from firebase

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int c = 0;
                                        //Toast.makeText(HomeActivity.this, ""+dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).getKey(), Toast.LENGTH_SHORT).show();
                                        for (DataSnapshot d:dataSnapshot.getChildren()){
                                            try {
                                                //Toast.makeText(HomeActivity.this, ""+d.child("User id").getValue(), Toast.LENGTH_SHORT).show();
                                                if ((d.child("Location").getValue().equals("On")) && (d.child("Full address").getValue().equals(fullAddress))){
                                                    Toast.makeText(HomeActivity.this, ""+d.child("Full Name").getValue(), Toast.LENGTH_SHORT).show();
                                                    c++;
                                                }

                                            }catch (Exception e){}

                                        }
                                        donotNumberTv.setText(String.valueOf(c));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(HomeActivity.this, "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void findBt(View view) {
        Intent intent = new Intent(HomeActivity.this,SignInActivity.class);
        intent.putExtra("fullAddress",fullAddress);
        intent.putExtra("country",country);
        intent.putExtra("city",city);
        intent.putExtra("state",state);
        try{
            intent.putExtra("road",road);
        }catch (Exception e){}
        startActivity(intent);
    }
}
