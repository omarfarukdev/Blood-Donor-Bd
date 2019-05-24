package com.example.blooddonorbd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.example.blooddonorbd.Models.UserInformation;
import com.google.android.gms.common.api.GoogleApiClient;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Service.LocationService;
import com.google.android.gms.common.ConnectionResult;
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
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private FusedLocationProviderClient client;
    Spinner bloodGroupSpinner;
    TextView currentLocationTv, donotNumberTv;
    String fullAddress, city, country, state,road,bloodGroup,spinnerSelectedItem;
    private FusedLocationProviderClient fusedLocationClient;
    ImageView userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        getSupportActionBar().hide();
        currentLocationTv = findViewById(R.id.currentlocation);
        donotNumberTv = findViewById(R.id.donorNumberTv);
        bloodGroupSpinner = findViewById(R.id.bloodgroupSpHome);
        userimage=findViewById(R.id.imageView6);

        //service started here
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        startService(i);

        //database reference
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        ArrayAdapter<String> adapter_option=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroup));
        adapter_option.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter_option);
        //bloodGroupSpinner.setSelection(2);
       // spinnerSelectedItem = bloodGroupSpinner.getSelectedItem().toString();
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        donotNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "" + fullAddress, Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//fusedLocation provider client

        getLocation();
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
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
                                        //Toast.makeText(HomeActivity.this, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                                        try{
                                            bloodGroup = dataSnapshot.getValue().toString();
                                            if (bloodGroup.equals("A+")){
                                                bloodGroupSpinner.setSelection(0);
                                            }else if(bloodGroup.equals("A-")){
                                                bloodGroupSpinner.setSelection(1);
                                            } else if(bloodGroup.equals("B+")){
                                                bloodGroupSpinner.setSelection(2);
                                            }else if(bloodGroup.equals("B-")){
                                                bloodGroupSpinner.setSelection(3);
                                            }else if(bloodGroup.equals("O+")){
                                                bloodGroupSpinner.setSelection(4);
                                            }else if(bloodGroup.equals("O-")){
                                                bloodGroupSpinner.setSelection(5);
                                            }else if(bloodGroup.equals("AB+")){
                                                bloodGroupSpinner.setSelection(6);
                                            }else if(bloodGroup.equals("AB-")){
                                                bloodGroupSpinner.setSelection(7);
                                            }
                                        }catch (Exception e){}

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
                                                String statee = d.child("State").getValue().toString();
                                                String city = d.child("City").getValue().toString();

                                                String[] stateList = statee.split(" ");
                                                String[] cityList = city.split(" ");

                                                String[] deviceState = state.split(" ");
                                                String[] deviceCity = city.split(" ");

                                                    if (!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) &&(d.child("Location").getValue().equals("On")) && (d.child("Full address").getValue().equals(fullAddress))){
                                                        //Toast.makeText(HomeActivity.this, ""+d.child("Full Name").getValue(), Toast.LENGTH_SHORT).show();

                                                        c++;
                                                    }else if(!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())&& (d.child("Location").getValue().equals("On"))&&
                                                            (checkString(stateList,deviceState) == true) && (checkString(cityList,deviceCity) == true)){

                                                        c++;
                                                    }
                                                    //else if(!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                                               // }

                                            }catch (Exception e){
                                                Toast.makeText(HomeActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                            }

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
                            //Toast.makeText(HomeActivity.this, "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(HomeActivity.this,ResultActivity.class);
        intent.putExtra("fullAddress",fullAddress);
        intent.putExtra("country",country);
        intent.putExtra("city",city);
        intent.putExtra("state",state);
        intent.putExtra("bloodGroup",spinnerSelectedItem);
        Toast.makeText(this, ""+spinnerSelectedItem, Toast.LENGTH_SHORT).show();
        try{
            intent.putExtra("road",road);
        }catch (Exception e){}
        startActivity(intent);

        //FirebaseAuth.getInstance().signOut();
    }

    public void search(View view) {

    }

    boolean checkString(String[] arr1,String[] arr2){
        for (String s:arr1){
            for (String s2:arr2){
                if(s.equals(s2)){
                    return true;
                }
            }
        }
        return false;
    }
}
