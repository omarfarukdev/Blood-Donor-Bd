package com.example.blooddonorbd;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonorbd.Fragments.SetupPersonalInfoFragment;
import com.example.blooddonorbd.Models.UserInformation;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.libraries.places.api.Places;

import java.util.Arrays;
import java.util.List;


public class SetupProfileActivity extends AppCompatActivity {
    EditText fullName,dateOfBirth,lastDonationDate;
    Spinner bloodGroup,gender;
    String fName,bGroup,dBirth,g;
    UserInformation userInformation;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    int AUTOCOMPLETE_REQUEST_CODE =1;
    public SetupProfileActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        //getSupportActionBar().hide();//hiding actionbar here

       /* //all views initializing here
        fullName = findViewById(R.id.fullNameEt);
        dateOfBirth = findViewById(R.id.dateOfBirthEt);
        bloodGroup = findViewById(R.id.bloodGroupSp);
        //currentAdd = findViewById(R.id.currentAddEt);
        lastDonationDate = findViewById(R.id.lastDonationDateEt);
        gender = findViewById(R.id.genderSP);

        //fName = fullName.getText().toString();
        //dBirth = dateOfBirth.getText().toString();

        userInformation = new UserInformation(fullName.getText().toString(),bGroup,dateOfBirth.getText().toString(),g);

        // Creating adapter for spinner
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroup));
        bloodGroup.setAdapter(bloodAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.gender));
        gender.setAdapter(genderAdapter);
        final Calendar calendar1 = Calendar.getInstance();
        lastDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        lastDonationDate.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                };
                new DatePickerDialog(SetupProfileActivity.this, date, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();
            }

        });


        final Calendar calendar2 = Calendar.getInstance();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, month);
                        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateOfBirth.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                };
                new DatePickerDialog(SetupProfileActivity.this, date, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show();
            }

        });*/
        //Fragment fragment = new
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment,new SetupPersonalInfoFragment(),"setupPersonalInfo");
        fragmentTransaction.commit();
        //fragmentTransaction.replace(R.id.fragment,fragment[0]);
    }

    /*public void setupprofileBt(View view) {
        int fName = fullName.getText().toString().length();
        int dateOfBt = dateOfBirth.getText().toString().length();
        if(fName == 0){
            fullName.setError("Enter full name.");
        }
        if(dateOfBt == 0){
            dateOfBirth.setError("Enter your date of birth.");
        }
        else if(fName!=0 && dateOfBt!=0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your blood group is "+bloodGroup.getSelectedItem().toString()+" . Are you sure with that ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());//current user database reference
                    databaseReference.child("Full Name").setValue(fullName.getText().toString());
                    databaseReference.child("Blood Group").setValue(bloodGroup.getSelectedItem().toString());
                    databaseReference.child("Date of birth").setValue(dateOfBirth.getText().toString());
                    databaseReference.child("Gender").setValue(gender.getSelectedItem().toString());
                    databaseReference.child("Last donation date").setValue(lastDonationDate.getText().toString());
                    Intent intent = new Intent(SetupProfileActivity.this,CurrentLocationSetupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
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

    public void currentAdd(View view) {


    }*/


}
