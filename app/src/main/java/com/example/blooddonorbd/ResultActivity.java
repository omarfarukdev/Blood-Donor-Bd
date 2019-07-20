package com.example.blooddonorbd;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.blooddonorbd.Adapters.UserListAdapters;
import com.example.blooddonorbd.Models.UserInformation;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private String searchLocation,searchPlaceNamee,fName;
    double searchLatitude,searchLongitude;
    double deviceLatitude,deviceLongitude;
    private long diffSeconds,diffMinutes,diffHours,diffDays;
    private long diffDaysOnLastDonation = 0,diffHoursOnLastDonation,diffMinutesOnLastDonation;
    public String deviceToken;
    private ImageView activeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.userListListView);
        bloodGrpTv = findViewById(R.id.bloodgrpTv);
        noOfDonorTv = findViewById(R.id.noOfDonotTv);
        searchIm = findViewById(R.id.searchImg);
        //listviewEmptyTv = findViewById(R.id.listviewEmptyMsgTv);

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


        search(fullAddress,state,city,road,deviceLatitude,deviceLongitude);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this,ResultDetails.class);
                intent.putExtra("name",userList.get(position).getfName());
                intent.putExtra("dateOfBirth",userList.get(position).getDateOfBirth());
                intent.putExtra("gender",userList.get(position).getGender());
                intent.putExtra("phoneNo",userList.get(position).getPhnNo());
                intent.putExtra("tokenId",userList.get(position).getToken());
                intent.putExtra("joiningDate",userList.get(position).getJoiningDate());
                //intent.putExtra("name",fName);

                startActivity(intent);
            }
        });
        //listView.setEmptyView(listviewEmptyTv);
        
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
       // FirebaseAuth.getInstance().signOut();
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

                searchLocation = place.getAddress();
                String s[] = splitString(searchLocation);
                //for (String st:s){
                    if (s.length == 2){
                        city = s[s.length-2];
                    }
                   // else if (s.length.)
                //}
                //Toast.makeText(this, ""+place.getLatLng(), Toast.LENGTH_LONG).show();
                fullAddress = place.getAddress();
                searchPlaceNamee = place.getName();
                searchLatitude = place.getLatLng().latitude;
                searchLongitude = place.getLatLng().longitude;

                customSearch(searchLocation,place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
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


        search(fullAddress,state,city,road,searchLatitude,searchLongitude);
    }
//search functionality
    public void search(final String fullAddress, final String state, final String cityy, final String road, final double latitude, final double longitude){
        userList.clear();
        userListAdapters.notifyDataSetChanged();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c = 0,cc=0;
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    try {
                        String address,dateOfBirth,gender,bldGroup,phnNo,lastDonationDate = null,joiningDate=null;
                        double homeAddressLat=0,homeAddressLong=0;;
                        try {
                            homeAddressLat = Double.parseDouble(d.child("Current home latitude").getValue().toString());
                        }catch (Exception e){}

                        try {
                            homeAddressLong = Double.parseDouble(d.child("Current home longitude").getValue().toString());
                        }catch (Exception e){}
                        try {
                            lastDonationDate = d.child("Last donation date").getValue().toString();
                        }catch (Exception e){}



                       if (!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){//checking current user or not
                           String statee = d.child("State").getValue().toString();
                           String city = d.child("City").getValue().toString();
                           String r = d.child("Road").getValue().toString();

                           String userExitTime = d.child("Active time").getValue().toString();

                           Calendar cal = Calendar.getInstance();
                           DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                           String currentDateandTime = sdf.format(cal.getTime());

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

                           } catch (Exception e) {
                               e.printStackTrace();
                           }

                           double dbLatitude = Double.parseDouble(d.child("Latitude").getValue().toString());
                           double dbLongitude = Double.parseDouble(d.child("Longitude").getValue().toString());
                           try {
                               lastDonationDate = d.child("Last donation date").getValue().toString();
                           }catch (Exception e){}

                           String[] roadList = r.split(" ");
                           String[] stateList = statee.split(" ");
                           String[] cityList = city.split(" ");

                           String[] deviceState = state.split(" ");
                           String[] deviceCity = cityy.split(" ");
                           String[] deviceRoad  = road.split(" ");

                           ArrayList<Long> exitTimeDifList;
                           try{
                               exitTimeDifList = dateDifference(userExitTime,currentDateandTime);
                               diffDays = exitTimeDifList.get(3);
                               diffHours = exitTimeDifList.get(2);
                               diffMinutes = exitTimeDifList.get(1);
                           }catch (Exception e){}

                           if ((d.child("Location").getValue().toString().equals("On") && diffDays ==0 && diffHours ==0 && diffMinutes<=30) ||
                                   (d.child("Location").getValue().toString().equals("Off") && diffDays ==0 && diffHours ==0 && diffMinutes<=30)){
                               if (d.child("Blood Group").getValue().equals(bloodGroup)){
                                   ArrayList<Long> lastDonationDateList;
                                   try{
                                       lastDonationDateList = lastDonationDateDiff(lastDonationDate,currentDateandTime);
                                       diffDaysOnLastDonation = lastDonationDateList.get(3);
                                       diffHoursOnLastDonation = lastDonationDateList.get(2);
                                       diffMinutesOnLastDonation = lastDonationDateList.get(1);
                                   }catch (Exception e){}

                                   if (diffDaysOnLastDonation>=120){
                                       if (splitString2(fullAddress) == 1 && d.child("Country").getValue().toString().trim().equals(fullAddress.trim())){//search like "Bangladesh"
                                           fName = d.child("Full Name").getValue().toString();
                                           address = d.child("Full address").getValue().toString();
                                           dateOfBirth = d.child("Date of birth").getValue().toString();
                                           bldGroup = d.child("Blood Group").getValue().toString();
                                           phnNo = d.getKey();
                                           gender = d.child("Gender").getValue().toString();
                                           joiningDate = d.child("Sign up date").getValue().toString();
                                           deviceToken = d.child("Token id").getValue().toString();
                                           userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.activestatus);
                                           userList.add(userInformation);
                                           listView.setAdapter(userListAdapters);
                                           c++;
                                           //pgsBar.setVisibility(View.GONE);
                                       }
                                       else if (splitString2(fullAddress) == 2 &&  checkCity(cityList,deviceCity) == true){//search like "Dhaka,Bangladesh"
                                           fName = d.child("Full Name").getValue().toString();
                                           address = d.child("Full address").getValue().toString();
                                           dateOfBirth = d.child("Date of birth").getValue().toString();
                                           bldGroup = d.child("Blood Group").getValue().toString();
                                           phnNo = d.getKey();
                                           gender = d.child("Gender").getValue().toString();
                                           joiningDate = d.child("Sign up date").getValue().toString();
                                           deviceToken = d.child("Token id").getValue().toString();
                                           userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.activestatus);
                                           userList.add(userInformation);
                                           listView.setAdapter(userListAdapters);
                                           c++;
                                          // pgsBar.setVisibility(View.GONE);
                                       }
                                       else if ((splitString2(fullAddress) > 2 &&
                                               (checkCity(cityList,deviceCity) == true)) || (splitString2(fullAddress) > 2 && (checkState(stateList,deviceState) == true) &&//search like "Khilkhet,Dhaka,Bangladesh"
                                               (checkCity(cityList,deviceCity) == true) && checkCity(roadList,deviceRoad))){
                                           if (distance(dbLatitude,dbLongitude,latitude,longitude) <= 5000){//finding user around 5 miles
                                               fName = d.child("Full Name").getValue().toString();
                                               address = d.child("Full address").getValue().toString();
                                               dateOfBirth = d.child("Date of birth").getValue().toString();
                                               bldGroup = d.child("Blood Group").getValue().toString();
                                               phnNo = d.getKey();
                                               gender = d.child("Gender").getValue().toString();
                                               joiningDate = d.child("Sign up date").getValue().toString();
                                               deviceToken = d.child("Token id").getValue().toString();
                                               userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.activestatus);
                                               userList.add(userInformation);
                                               listView.setAdapter(userListAdapters);
                                               c++;
                                               //pgsBar.setVisibility(View.GONE);
                                           }
                                       }
                                   }
                               }
                           }
                           else{
                               if (d.child("Blood Group").getValue().equals(bloodGroup)){
                                   ArrayList<Long> lastDonationDateList;
                                   try{
                                       lastDonationDateList = lastDonationDateDiff(lastDonationDate,currentDateandTime);
                                       diffDaysOnLastDonation = lastDonationDateList.get(3);
                                       diffHoursOnLastDonation = lastDonationDateList.get(2);
                                       diffMinutesOnLastDonation = lastDonationDateList.get(1);
                                   }catch (Exception e){}
                                   if (diffDaysOnLastDonation>=120){
                                       if (splitString2(fullAddress) == 1 && d.child("Country").getValue().toString().trim().equals(fullAddress.trim())){//search like "Bangladesh"
                                           fName = d.child("Full Name").getValue().toString();
                                           address = d.child("Current Location").getValue().toString();
                                           dateOfBirth = d.child("Date of birth").getValue().toString();
                                           bldGroup = d.child("Blood Group").getValue().toString();
                                           phnNo = d.getKey();
                                           gender = d.child("Gender").getValue().toString();
                                           joiningDate = d.child("Sign up date").getValue().toString();
                                           deviceToken = d.child("Token id").getValue().toString();
                                           userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.notactivestatus);
                                           userList.add(userInformation);
                                           listView.setAdapter(userListAdapters);
                                           c++;
                                           //pgsBar.setVisibility(View.GONE);
                                       }
                                       else if (splitString2(fullAddress) == 2 &&  checkCity(cityList,deviceCity) == true){//search like "Dhaka,Bangladesh"
                                           fName = d.child("Full Name").getValue().toString();
                                           address = d.child("Current Location").getValue().toString();
                                           dateOfBirth = d.child("Date of birth").getValue().toString();
                                           bldGroup = d.child("Blood Group").getValue().toString();
                                           phnNo = d.getKey();
                                           gender = d.child("Gender").getValue().toString();
                                           joiningDate = d.child("Sign up date").getValue().toString();
                                           deviceToken = d.child("Token id").getValue().toString();
                                           userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.notactivestatus);
                                           userList.add(userInformation);
                                           listView.setAdapter(userListAdapters);
                                           c++;
                                           //pgsBar.setVisibility(View.GONE);
                                       }
                                       else if ((splitString2(fullAddress) > 2 &&
                                               (checkCity(cityList,deviceCity) == true)) || (splitString2(fullAddress) > 2 && (checkState(stateList,deviceState) == true) &&//search like "Khilkhet,Dhaka,Bangladesh"
                                               (checkCity(cityList,deviceCity) == true) && checkCity(roadList,deviceRoad))){
                                           if (distance(homeAddressLat,homeAddressLong,latitude,longitude) <= 5000){//finding user around 5 miles
                                               fName = d.child("Full Name").getValue().toString();
                                               address = d.child("Current Location").getValue().toString();
                                               dateOfBirth = d.child("Date of birth").getValue().toString();
                                               bldGroup = d.child("Blood Group").getValue().toString();
                                               phnNo = d.getKey();
                                               gender = d.child("Gender").getValue().toString();
                                               joiningDate = d.child("Sign up date").getValue().toString();
                                               deviceToken = d.child("Token id").getValue().toString();
                                               userInformation = new UserInformation(fName,address,joiningDate,dateOfBirth,bldGroup,phnNo,gender,deviceToken,R.drawable.notactivestatus);
                                               userList.add(userInformation);
                                               listView.setAdapter(userListAdapters);
                                               c++;
                                           }
                                       }
                                   }

                               }
                           }
                        }
                    }catch (Exception e){
                    }

                }

                noOfDonorTv.setText("We found "+String.valueOf(c)+" donors available");
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

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lng1);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lng2);
        return  startPoint.distanceTo(endPoint);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<Long> lastDonationDateDiff(String userExitTime, String currentDateandTime){
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
