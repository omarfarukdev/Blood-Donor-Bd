package com.example.blooddonorbd.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonorbd.DiscoverableActivity;
import com.example.blooddonorbd.EditProfileActivity;
import com.example.blooddonorbd.HomeActivity;
import com.example.blooddonorbd.Models.UserInformation;
import com.example.blooddonorbd.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class SetupPersonalInfoFragment extends Fragment {
    EditText fullName,dateOfBirth,lastDonationDate,currentHomeAddressEt;
    Spinner bloodGroup,gender;
    String fName,bGroup,dBirth,g;
    UserInformation userInformation;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    String currentLocation;
    double latitude;
    double logitude;
    Button nextBt;
    int REQUEST_CODE = 10;
    int AUTOCOMPLETE_REQUEST_CODE =1;
    String lastdate;
    private DatePickerDialog.OnDateSetListener mDatasetListener,bDatasetListener;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_setup_personal_info, container, false);
        //all views initializing here
        fullName = view.findViewById(R.id.fullNameEt);
        dateOfBirth = view.findViewById(R.id.dateOfBirthEt);
        currentHomeAddressEt = view.findViewById(R.id.homeAddressSearchEt);

        bloodGroup = view.findViewById(R.id.bloodGroupSp);
        //currentAdd = findViewById(R.id.currentAddEt);
        lastDonationDate = view.findViewById(R.id.lastDonationDateEt);
        gender = view.findViewById(R.id.genderSP);

        nextBt = view.findViewById(R.id.btIdOnsetupInfo);

        //userInformation = new UserInformation(fullName.getText().toString(),bGroup,dateOfBirth.getText().toString(),g);

        // Creating adapter for spinner
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroup));
        bloodGroup.setAdapter(bloodAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.gender));
        gender.setAdapter(genderAdapter);


        // Initialize Places.
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDPci6XtzK5LMbXvILYVQNj8CPI7qUArdg");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyDPci6XtzK5LMbXvILYVQNj8CPI7qUArdg");
        }
        //final Calendar calendar1 = Calendar.getInstance();
        final java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final SimpleDateFormat formatterd = new SimpleDateFormat("dd");
        final SimpleDateFormat formatterm = new SimpleDateFormat("MM");
        final Date date = new Date();
        final String d=formatter.format(date);
        Log.d("oaaaa",""+d);
        calendar1.set(Calendar.DAY_OF_MONTH,Integer.parseInt(formatterd.format(date)));
        calendar1.set(Calendar.MONTH,(Integer.parseInt(formatterm.format(date))-1));
        calendar1.set(Calendar.YEAR,Integer.parseInt(formatter.format(date)));
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        lastDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDatasetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
                dialog.show();
            }
        });
        mDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(calendar.getTime());
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                lastdate=(month+1) + "/"+dayOfMonth+"/"+year+" "+currentTime;
                String date=dayOfMonth+"/"+(month+1) + "/"+year;
                lastDonationDate.setText(date);
            }
        };


        final java.util.Calendar calendar2 = java.util.Calendar.getInstance();

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year=calendar2.get(Calendar.YEAR);
                int month=calendar2.get(Calendar.MONTH);
                int day=calendar2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,bDatasetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
                dialog.show();
            }
        });
        bDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar2.set(Calendar.YEAR,year);
                calendar2.set(Calendar.MONTH,month);
                calendar2.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String date=dayOfMonth+"/"+(month+1) + "/"+year;
                dateOfBirth.setText(date);
            }
        };

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupprofileBt(v);
            }
        });

        currentHomeAddressEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE);
                }
        });
        return view;
    }

    public void setupprofileBt(View view) {

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMMM/yyyy");
        final String currentDate = df.format(date);

        final Fragment[] fragment = new Fragment[1];
        int fName = fullName.getText().toString().length();
        int dateOfBt = dateOfBirth.getText().toString().length();
        if(fName == 0){
            fullName.setError("Enter full name.");
        }
        if(dateOfBt == 0){
            dateOfBirth.setError("Enter your date of birth.");
        }
        if (currentHomeAddressEt.getText().length() == 0){
            dateOfBirth.setError("Enter your current home address.");
        }
        else if(fName!=0 && dateOfBt!=0 && currentHomeAddressEt.getText().length() != 0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Your blood group is "+bloodGroup.getSelectedItem().toString()+" . Are you sure with that ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());//current user database reference
                    databaseReference.child("Full Name").setValue(fullName.getText().toString());
                    databaseReference.child("Blood Group").setValue(bloodGroup.getSelectedItem().toString());
                    databaseReference.child("Date of birth").setValue(dateOfBirth.getText().toString());
                    databaseReference.child("Gender").setValue(gender.getSelectedItem().toString());
                    databaseReference.child("Last donation date").setValue(lastDonationDate.getText().toString());*/

                    /*Intent intent = new Intent(getActivity(), CurrentLocationSetupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().finish();
                    startActivity(intent);*/
                    /*fragment[0] = new SetupHomeAddressFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragment);
                    //passing data via fragment
                    Bundle bundle=new Bundle();
                    bundle.putString("fName",fullName.getText().toString());
                    bundle.putString("BloodGroup",bloodGroup.getSelectedItem().toString());
                    bundle.putString("dateOfBirth",dateOfBirth.getText().toString());
                    bundle.putString("gender",gender.getSelectedItem().toString());
                    bundle.putString("lastDonationDate",lastDonationDate.getText().toString());

                    fragmentTransaction.remove(fragment1);
                    fragmentTransaction.commit();
                    fragment[0].setArguments(bundle); //data being send to SecondFragment
                    fragmentTransaction.replace(R.id.fragment,fragment[0]);*/


                    String country = null,city = null,state = null,road = null;
                    try{
                        String[] address = currentLocation.split(",");
                        int addLength = address.length;
                        if (addLength == 3){
                            try{
                                country = address[addLength-1];
                            }catch (Exception e){}
                            try{
                                city = address[addLength-2];
                            }catch (Exception e){}
                            try{
                                state = address[addLength-3];
                            }catch (Exception e){}
                        }
                        //road = address[addLength-4];
                    }catch (NullPointerException n){};

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());//current user database reference
                    databaseReference.child("Current home country").setValue(country);
                    databaseReference.child("Current home city").setValue(city);
                    databaseReference.child("Current home state").setValue(state);
                    databaseReference.child("Current home latitude").setValue(latitude);
                    databaseReference.child("Current home longitude").setValue(logitude);
                    databaseReference.child("Current Location").setValue(currentLocation);
                    databaseReference.child("Sign up date").setValue(currentDate);

                    databaseReference.child("Full Name").setValue(fullName.getText().toString());
                    databaseReference.child("Blood Group").setValue(bloodGroup.getSelectedItem().toString());
                    databaseReference.child("Date of birth").setValue(dateOfBirth.getText().toString());
                    databaseReference.child("Gender").setValue(gender.getSelectedItem().toString());
                    databaseReference.child("Last donation date").setValue(lastdate);
                    //databaseReference.child("Current home road").setValue(road);
                    //databaseReference.child("Full Name").setValue(fullName.getText().toString());
                    if (isLocationEnabled()){
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), DiscoverableActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(intent);
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Intent intent = new Intent(SetupProfileActivity.this,CurrentLocationSetupActivity.class);
                    //startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.d("pl", "Place: " + place.getName() + ", " + place.getId());
                //Toast.makeText(getActivity(), ""+place.getAddress(), Toast.LENGTH_SHORT).show();

                currentLocation = place.getAddress();
                latitude = place.getLatLng().latitude;
                logitude = place.getLatLng().longitude;

                currentHomeAddressEt.setText(place.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                //Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
