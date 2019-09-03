package com.example.blooddonorbd.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.example.blooddonorbd.Classes.ReadMessage;
import com.example.blooddonorbd.Models.MessageInfo;
import com.example.blooddonorbd.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Service.LocationService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Spinner bloodGroupSpinner;
    private TextView currentLocationTv, donotNumberTv;
    private String fullAddress, city,currentcity , country, state,road,bloodGroup,spinnerSelectedItem;
    private FusedLocationProviderClient fusedLocationClient;
    private ImageView userimage;
    private double latitude,longitude;
    private DatabaseReference databaseReferenceExit;
    private long diffSeconds,diffMinutes,diffHours,diffDays;
    private long diffSecondOnLastDonation,diffMinutesOnLastDonation,diffHoursOnLastDonation,diffDaysOnLastDonation;
    private String lastMessage,recever,isSeen;
    private int messageCount = 0;
    private TextView notificationBadgeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentLocationTv = findViewById(R.id.currentlocation);
        donotNumberTv = findViewById(R.id.donorNumberTv);
        bloodGroupSpinner = findViewById(R.id.bloodgroupSpHome);
        userimage=findViewById(R.id.imageView6);
        notificationBadgeTv = findViewById(R.id.badge);

        Intent i = new Intent(getApplicationContext(), LocationService.class);
        startService(i);

        ArrayAdapter<String> adapter_option=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroupOnEdit));
        adapter_option.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter_option);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//fusedLocation provider client

        getLocation();
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
                intent.putExtra("city",currentcity );
                startActivity(intent);
            }
        });

        ReadMessage readMessage = new ReadMessage(this,1);
        readMessage.readUser();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10: {
                //getLocation();
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // now, you have permission go ahead
                    // TODO: something
                    getLocation();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            ACCESS_FINE_LOCATION)) {
                        // now, user has denied permission (but not permanently!)
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(false);
                        builder.setMessage("We need your location to find out near donor. Please allow the permission");
                        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getLocation();
                            }
                        });
                        builder.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(false);
                        builder.setMessage("We need your location to find out near donor. Please allow the permission"+"\n\nSetting -> App permission -> Location");
                        builder.setPositiveButton("Go to setting", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                //getLocation();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
                break;
            }
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //if (ActivityCompat.shouldShowRequestPermissionRationale())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                            List<Address> addresses;
                            ArrayList<String> addressList = new ArrayList<>();
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                Address obj = addresses.get(0);

                                fullAddress = obj.getAddressLine(0);

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

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
                                                double dbLatitude = 0,dbLongitude=0,homeAddressLat=0,homeAddressLong=0;
                                                String lastDonationDate = null;
                                                String statee = d.child("State").getValue().toString();
                                                String city = d.child("City").getValue().toString();
                                                currentcity = d.child("Current home city").getValue().toString();
                                                String bloodGrp = d.child("Blood Group").getValue().toString();
                                                String userExitTime;
                                                try {
                                                    dbLatitude = Double.parseDouble(d.child("Latitude").getValue().toString());
                                                }catch (Exception e){}
                                                try {
                                                    dbLongitude = Double.parseDouble(d.child("Longitude").getValue().toString());
                                                }catch (Exception e){}
                                                try {
                                                    homeAddressLat = Double.parseDouble(d.child("Current home latitude").getValue().toString());
                                                }catch (Exception e){}

                                                try {
                                                    homeAddressLong = Double.parseDouble(d.child("Current home longitude").getValue().toString());
                                                }catch (Exception e){}
                                                try {
                                                    lastDonationDate = d.child("Last donation date").getValue().toString();
                                                }catch (Exception e){}



                                              /*  String[] stateList = statee.split(" ");
                                                String[] cityList = city.split(" ");

                                                String[] deviceState = state.split(" ");
                                                String[] deviceCity = city.split(" ");*/
                                              //lastMessage(d.getKey());
                                              Log.d("msggggg",d.getKey());

                                                if(!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                                                    userExitTime = d.child("Active time").getValue().toString();

                                                    Calendar cal = Calendar.getInstance();
                                                    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                                    String currentDateandTime = sdf.format(cal.getTime());

                                                    // Toast.makeText(HomeActivity.this, ""+diffDays+" "+diffHours+" "+diffMinutes+" "+diffSeconds+" exit time  "+userExitTime, Toast.LENGTH_SHORT).show();
                                                    ArrayList<Long> exitTimeDifList;
                                                    try{
                                                         exitTimeDifList = dateDifference(userExitTime,currentDateandTime);
                                                         diffDays = exitTimeDifList.get(3);
                                                         diffHours = exitTimeDifList.get(2);
                                                         diffMinutes = exitTimeDifList.get(1);
                                                    }catch (Exception e){}


                                                    if ((d.child("Location").getValue().toString().equals("On") && diffDays ==0 && diffHours ==0 && diffMinutes<=30) ||
                                                            (d.child("Location").getValue().toString().equals("Off") && diffDays ==0 && diffHours ==0 && diffMinutes<=30)){
                                                        ArrayList<Long> lastDonationDateList;
                                                        try{
                                                            lastDonationDateList = dateDifference(lastDonationDate,currentDateandTime);
                                                            diffDaysOnLastDonation = lastDonationDateList.get(3);
                                                            diffHoursOnLastDonation = lastDonationDateList.get(2);
                                                            diffMinutesOnLastDonation = lastDonationDateList.get(1);
                                                        }catch (Exception e){}

                                                        if(diffDaysOnLastDonation >= 120 &&  (distance(latitude, longitude, dbLatitude, dbLongitude) <= 5000) ){//checking here,user last donation date was 120 days before or not
                                                           c++;
                                                            //Log.d("iiiiii",d.child("Full name").getValue().toString());
                                                            //Toast.makeText(HomeActivity.this, "uuu"+d.child("Full Name").getValue().toString(), Toast.LENGTH_SHORT).show();

                                                        }

                                                    } else {
                                                        if ((distance(latitude, longitude, homeAddressLat, homeAddressLong) <= 5000)){
                                                            ArrayList<Long> lastDonationDateList;
                                                            try{
                                                                lastDonationDateList = dateDifference(lastDonationDate,currentDateandTime);
                                                                diffDaysOnLastDonation = lastDonationDateList.get(3);
                                                                diffHoursOnLastDonation = lastDonationDateList.get(2);
                                                                diffMinutesOnLastDonation = lastDonationDateList.get(1);
                                                            }catch (Exception e){}
                                                            if(diffDaysOnLastDonation >=120) {
                                                               c++;
                                                                //Toast.makeText(HomeActivity.this, "ttt"+diffDaysOnLastDonation, Toast.LENGTH_SHORT).show();
                                                                //Log.d("tttttt",d.child("Full name").getValue().toString());
                                                            }
                                                        }
                                                    }
                                                }

                                            }catch (Exception e){
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
                            //Toast.makeText(HomeActivity.this, "Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public ArrayList<Long> dateDifference(String userExitTime, String currentDateandTime){
        ArrayList<Long> arrayList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(userExitTime);
            d2 = format.parse(currentDateandTime);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

            arrayList.add(diffSeconds);
            arrayList.add(diffMinutes);
            arrayList.add(diffHours);
            arrayList.add(diffDays);

            //Log.d("llllll", "" + diffDays + " " + diffHours + " " + diffMinutes + " " + diffSeconds + " exit time  " + userExitTime + "  current " + currentDateandTime + " " + d1 + " " + d2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
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
        intent.putExtra("road",road);
        intent.putExtra("bloodGroup",spinnerSelectedItem);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
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

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lng1);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lng2);

        return startPoint.distanceTo(endPoint);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //service started here
    }

    private void exitDialog(){
        final Dialog exitDialog = new Dialog(HomeActivity.this);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //exitDialog.setCancelable(false);

        exitDialog.setContentView(R.layout.exit_logout_dialog);

        try{
            databaseReferenceExit = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }catch (Exception e){}

        Button dialogButton = (Button) exitDialog.findViewById(R.id.dilog_yesBt);
        ImageButton cancelIm = exitDialog.findViewById(R.id.cancelImageButton);
        TextView textView = exitDialog.findViewById(R.id.dialogTitle);
        TextView textView1 = exitDialog.findViewById(R.id.desText_dialog);
        textView.setText("Exit!");
        textView1.setText("Do you really want to exit ?");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(cal.getTime());
                databaseReferenceExit.child("Active time").setValue(currentDateandTime);
                Intent myService = new Intent(HomeActivity.this, LocationService.class);
                stopService(myService);
                finish();
                exitDialog.dismiss();
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*try{
            databaseReferenceExit = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }catch (Exception e){}

        android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
        dialog.setTitle("Do you want to Exit ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar cal = Calendar.getInstance();
                DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(cal.getTime());
                databaseReferenceExit.child("Active time").setValue(currentDateandTime);
                Intent myService = new Intent(HomeActivity.this, LocationService.class);
                stopService(myService);
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
        exitDialog();
    }
}
