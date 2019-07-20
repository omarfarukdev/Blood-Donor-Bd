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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment,new SetupPersonalInfoFragment(),"setupPersonalInfo");
        fragmentTransaction.commit();
    }
}
