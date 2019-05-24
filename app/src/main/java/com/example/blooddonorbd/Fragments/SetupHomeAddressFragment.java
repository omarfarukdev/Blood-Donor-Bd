package com.example.blooddonorbd.Fragments;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.DiscoverableActivity;
import com.example.blooddonorbd.HomeActivity;
import com.example.blooddonorbd.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
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
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


// TODO: Rena * create an instance of this fragment.
    // */
    public class SetupHomeAddressFragment extends Fragment {

        Button button,searchBt;
    TextView placeName;
    String currentLocation,placeNamee;
    int REQUEST_CODE =1;
    PlacesAutocompleteTextView placesAutocomplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setup_home_address, container, false);
        button = view.findViewById(R.id.btIdOnsetupInfo);
        placeName = view.findViewById(R.id.placeNameTv);
        searchBt = view.findViewById(R.id.searchBt);

        final Bundle bundle=getArguments();

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
        // Initialize the AutocompleteSupportFragment.
         //AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
         /*autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME));

         autocompleteFragment.setHint("Current home address");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //Toast.makeText(SetupProfileActivity.this, ""+place.getAddress()+"  "+place.getName(), Toast.LENGTH_SHORT).show();
                //currentAdd.setText(place.getAddress()+"  "+place.getName());
                //autocompleteFragment.setText(place.getAddress()+"  "+place.getName());
                placeName.setText(place.getAddress()+"  "+place.getName());
                currentLocation = place.getAddress();
                placeNamee = place.getName().toString();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getActivity(), ""+status, Toast.LENGTH_SHORT).show();
            }
        });*/

         searchBt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME);
                 // Start the autocomplete intent.
                 Intent intent = new Autocomplete.IntentBuilder(
                         AutocompleteActivityMode.FULLSCREEN, fields)
                         .build(getActivity());
                 startActivityForResult(intent, REQUEST_CODE);
             }
         });


      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String country = null,city = null,state = null,road = null;
              try{
                  String[] address = currentLocation.split(",");
                  int addLength = address.length;
                  country = address[addLength-1];
                  city = address[addLength-2];
                  state = address[addLength-3];
                  //road = address[addLength-4];
              }catch (NullPointerException n){};

              if (placeName.getText().toString().length() == 0){
                  Toast.makeText(getActivity(), "Enter your current home address", Toast.LENGTH_SHORT).show();
              }else{
                  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());//current user database reference
                  databaseReference.child("Current home country").setValue(country);
                  databaseReference.child("Current home city").setValue(city);
                  databaseReference.child("Current home state").setValue(state);

                  databaseReference.child("Full Name").setValue(bundle.getString("fName"));
                  databaseReference.child("Blood Group").setValue(bundle.getString("BloodGroup"));
                  databaseReference.child("Date of birth").setValue(bundle.getString("dateOfBirth"));
                  databaseReference.child("Gender").setValue(bundle.getString("gender"));
                  databaseReference.child("Last donation date").setValue(bundle.getString("lastDonationDate"));
                  //databaseReference.child("Current home road").setValue(road);
                  //databaseReference.child("Full Name").setValue(fullName.getText().toString());
                  if (isLocationEnabled()){
                     Intent intent = new Intent(getActivity(),HomeActivity.class);
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
          }
      });

        return view;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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

                currentLocation = place.getAddress();
                placeNamee = place.getName();
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
