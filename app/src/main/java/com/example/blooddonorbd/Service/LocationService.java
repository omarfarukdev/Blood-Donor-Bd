package com.example.blooddonorbd.Service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 5 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)//google map api client
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        registerReceiver(broadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
       // checkLocation();

        if (isLocationEnabled()) {
            databaseReference.child("Location").setValue("On");
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "Permission granted bro", Toast.LENGTH_SHORT).show();
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.getFusedLocationProviderClient(this);

        mLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location==null){
                    startLocationUpdates();
                }
                if (location!=null){

                }else {
                    Toast.makeText(getApplicationContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
                    //onCreate();
                }
            }
        });

        //Toast.makeText(this, "" + mLocation, Toast.LENGTH_SHORT).show();
       /* if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            // mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed. Error: " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }
    //connection of api client when service start
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    //destroy api Client when service destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //location update
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        ArrayList<String> addressList = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);

            String fullAddress = obj.getAddressLine(0);

            String[] sp = fullAddress.split(",");
            if(sp.length != 0){
                for(int i=0;i<sp.length;i++){
                    addressList.add(sp[i]);
                }
                try{
                    databaseReference.child("Country").setValue(addressList.get(addressList.size()-1).trim());
                }catch (Exception e){}
                try{
                    databaseReference.child("City").setValue(addressList.get(addressList.size()-2).trim());
                }catch (Exception e){}
                try{
                    databaseReference.child("State").setValue(addressList.get(addressList.size()-3).trim());
                }catch (Exception e){}
                try{
                    databaseReference.child("Full address").setValue(fullAddress);
                }catch (Exception e){}
                try{
                    databaseReference.child("Latitude").setValue(location.getLatitude());
                }catch (Exception e){}
                try{
                    databaseReference.child("Longitude").setValue(location.getLongitude());
                }catch (Exception e){}
                try{
                    Calendar cal = Calendar.getInstance();
                    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String currentDateandTime = sdf.format(cal.getTime());
                    databaseReference.child("Active time").setValue(currentDateandTime);
                }catch (Exception e){}
                try{
                    databaseReference.child("Road").setValue(addressList.get(addressList.size()-4));
                }catch (Exception e){
                    databaseReference.child("Road").setValue("Null");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isLocationEnabled()) {
                databaseReference.child("Location").setValue("On");
            }else if(intent.getAction().matches("android.location.PROVIDERS_CHANGED") && !isLocationEnabled()){
                databaseReference.child("Location").setValue("Off");
            }
        }
    };

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
