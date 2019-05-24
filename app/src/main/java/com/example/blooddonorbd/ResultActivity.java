package com.example.blooddonorbd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Adapters.UserListAdapters;
import com.example.blooddonorbd.Models.UserInformation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private String country,city,state,road,fullAddress,bloodGroup;
    private UserInformation userInformation;
    private UserListAdapters userListAdapters;
    private ArrayList<UserInformation> userList;
    private ListView listView;
    private TextView bloodGrpTv,noOfDonorTv;
    private ImageView searchIm;
    private int REQUEST_CODE = 1;
    private String searchLocation,searchPlaceNamee;
    double searchLatitude,searchLongitude;
    double deviceLatitude,deviceLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.userListListView);
        bloodGrpTv = findViewById(R.id.bloodgrpTv);
        noOfDonorTv = findViewById(R.id.noOfDonotTv);
        searchIm = findViewById(R.id.searchImg);

        Intent intent = getIntent();
        fullAddress = intent.getStringExtra("fullAddress");
        road = intent.getStringExtra("road");
        state = intent.getStringExtra("state");
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        bloodGroup = intent.getStringExtra("bloodGroup");
        deviceLatitude = (double) intent.getDoubleExtra("latitude",0.0);
        deviceLongitude = (double) intent.getDoubleExtra("longitude",0.0);

        bloodGrpTv.setText(bloodGroup);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        userList = new ArrayList<>();

        //adapters
        userListAdapters = new UserListAdapters(this,userList);

        Places.initialize(this.getApplicationContext(), "AIzaSyDPci6XtzK5LMbXvILYVQNj8CPI7qUArdg");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(this.getApplicationContext(), "AIzaSyDPci6XtzK5LMbXvILYVQNj8CPI7qUArdg");
        }

        searchIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                //TypeFilter typeFilter = new TypeFilter();
                //typeFilter.setFilter(typeFilter);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setCountry("Bd").build(ResultActivity.this);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //Log.d("lennn",fullAddress);

        search(fullAddress,state,city,road,deviceLatitude,deviceLongitude);

        Toast.makeText(this, "Oncreate called", Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this,ResultDetails.class);
                intent.putExtra("name",userList.get(position).getfName());
                intent.putExtra("dateOfBirth",userList.get(position).getDateOfBirth());
                intent.putExtra("gender",userList.get(position).getGender());
                intent.putExtra("phoneNo",userList.get(position).getPhnNo());
                startActivity(intent);
            }
        });
        
    }
    //checking two array have minimum one word same
    boolean checkState(String[] arr1,String[] arr2){

            for (String s:arr1){
                for (String s2:arr2){
                    if(s.trim().equals(s2.trim())){
                        return true;
                    }
                }
            }
       // Log.d("ttttt",String.valueOf(c)+" "+arr1.length +"  "+arr2.length);
        return false;
    }

    //checking two array have minimum one word same
    boolean checkCity(String[] arr1,String[] arr2){
        int c = 0;
        for (String s:arr1){
            for (String s2:arr2){
                if(s.trim().equals(s2.trim())){
                    c++;
                }
            }
        }
        //Log.d("ttttt",String.valueOf(c)+" "+arr1.length +"  "+arr2.length);
       if (c>=1){
           return true;
       }
        else return false;
    }

    public void image(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void backBt(View view) {
        finish();
    }

    /**
     * Override the activity's onActivityResult(), check the request code, and
     * do something with the returned place data (in this example it's place name and place ID).
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.d("pl", "Place: " + place.getName() + ", " + place.getId());
                //Toast.makeText(getActivity(), ""+place.getAddress(), Toast.LENGTH_SHORT).show();

                searchLocation = place.getAddress();
                String s[] = splitString(searchLocation);
                //for (String st:s){
                    if (s.length == 2){
                        city = s[s.length-2];
                    }
                   // else if (s.length.)
                //}
                //Toast.makeText(this, ""+place.getLatLng(), Toast.LENGTH_LONG).show();
                Log.d("latLog",""+place.getLatLng().latitude);
                fullAddress = place.getAddress();
                searchPlaceNamee = place.getName();
                searchLatitude = place.getLatLng().latitude;
                searchLongitude = place.getLatLng().longitude;

                customSearch(searchLocation,place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                //Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    public void customSearch(String fullAdd,String placeName){
        String[] fAd = fullAdd.split(",");
        String city="",state="",road="";
        try{
            city = fAd[fAd.length-2];
            state = fAd[fAd.length-3];
            road = fAd[fAd.length-4];
        }catch (Exception e){}

       // Toast.makeText(this, ""+city, Toast.LENGTH_SHORT).show();

        search(fullAddress,state,city,road,searchLatitude,searchLongitude);
    }
//search functionality
    public void search(final String fullAddress, final String state, final String cityy, final String road, final double latitude, final double longitude){
        //Log.d("Keyyy",String.valueOf(key));
        userList.clear();
        userListAdapters.notifyDataSetChanged();

        //Log.d("addd",state+"  "+cityy+"  "+road );
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c = 0;
                //Toast.makeText(HomeActivity.this, ""+dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).getKey(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    try {
                        //Log.d("Keyyy",String.valueOf(key));
                        //Toast.makeText(ResultActivity.this, "", Toast.LENGTH_SHORT).show();
                        String fName,address,dateOfBirth,gender,bldGroup,phnNo;



                       if (!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){//checking current user or not
                           String statee = d.child("State").getValue().toString();
                           String city = d.child("City").getValue().toString();
                           String r = d.child("Road").getValue().toString();
                           double dbLatitude = Double.parseDouble(d.child("Latitude").getValue().toString());
                           double dbLongitude = Double.parseDouble(d.child("Longitude").getValue().toString());

                           String[] roadList = r.split(" ");
                           String[] stateList = statee.split(" ");
                           String[] cityList = city.split(" ");

                           String[] deviceState = state.split(" ");
                           String[] deviceCity = cityy.split(" ");
                           String[] deviceRoad  = road.split(" ");
                           if (d.child("Location").getValue().equals("On")){//checking location on or not
                               if (d.child("Blood Group").getValue().equals(bloodGroup)){//checking blood group here
                                   Log.d("lennnnn",String.valueOf(checkCity(cityList,deviceCity))+" "+splitString2(fullAddress)+" "+String.valueOf((checkState(stateList,deviceState)))
                                           +" search state = "+state+"  db state="+statee);

                                   if (splitString2(fullAddress) == 1 && d.child("Country").getValue().toString().trim().equals(fullAddress.trim())){//search like "Bangladesh"
                                       fName = d.child("Full Name").getValue().toString();
                                       address = d.child("Full address").getValue().toString();
                                       dateOfBirth = d.child("Date of birth").getValue().toString();
                                       bldGroup = d.child("Blood Group").getValue().toString();
                                       phnNo = d.getKey();
                                       gender = d.child("Gender").getValue().toString();
                                       userInformation = new UserInformation(fName,address,dateOfBirth,bldGroup,phnNo,gender);
                                       userList.add(userInformation);
                                       listView.setAdapter(userListAdapters);
                                       c++;
                                   }
                                   else if (splitString2(fullAddress) == 2 &&  checkCity(cityList,deviceCity) == true){//search like "Dhaka,Bangladesh"
                                       fName = d.child("Full Name").getValue().toString();
                                       address = d.child("Full address").getValue().toString();
                                       dateOfBirth = d.child("Date of birth").getValue().toString();
                                       bldGroup = d.child("Blood Group").getValue().toString();
                                       phnNo = d.getKey();
                                       gender = d.child("Gender").getValue().toString();
                                       userInformation = new UserInformation(fName,address,dateOfBirth,bldGroup,phnNo,gender);
                                       userList.add(userInformation);
                                       listView.setAdapter(userListAdapters);
                                       c++;
                                   }
                                   else if ((splitString2(fullAddress) > 2 /*&& (checkState(stateList,deviceState) == true) &&//search like "Khilkhet,Dhaka,Bangladesh"
                                           (checkCity(cityList,deviceCity) == true)) || (splitString2(fullAddress) > 2 && (checkState(stateList,deviceState) == true) &&//search like "Khilkhet,Dhaka,Bangladesh"
                                           (checkCity(cityList,deviceCity) == true) && checkCity(roadList,deviceRoad))*/)){
                                       if (distance(dbLatitude,dbLongitude,latitude,longitude) < 0.6 ){//finding user around 5 miles
                                           fName = d.child("Full Name").getValue().toString();
                                           address = d.child("Full address").getValue().toString();
                                           dateOfBirth = d.child("Date of birth").getValue().toString();
                                           bldGroup = d.child("Blood Group").getValue().toString();
                                           phnNo = d.getKey();
                                           gender = d.child("Gender").getValue().toString();
                                           userInformation = new UserInformation(fName,address,dateOfBirth,bldGroup,phnNo,gender);
                                           userList.add(userInformation);
                                           listView.setAdapter(userListAdapters);
                                           c++;
                                       }
                                   }
                               }
                           }
                        }

                    }catch (Exception e){
                        Toast.makeText(ResultActivity.this, ""+e+" "+c, Toast.LENGTH_SHORT).show();
                    }

                }
                noOfDonorTv.setText("We found "+String.valueOf(c)+" donors available");
                // donotNumberTv.setText(String.valueOf(c));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public String[] splitString(String str){
       String[] sp = str.split(",");
       return sp;
    }

    public int splitString2(String str){
        String[] sp = str.split(",");
        return sp.length;
    }

//    double splitLatLong(String )

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }
}
