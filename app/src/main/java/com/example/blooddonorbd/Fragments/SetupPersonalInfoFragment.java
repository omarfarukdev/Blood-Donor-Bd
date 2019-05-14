package com.example.blooddonorbd.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.blooddonorbd.Models.UserInformation;
import com.example.blooddonorbd.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;


public class SetupPersonalInfoFragment extends Fragment {
    EditText fullName,dateOfBirth,lastDonationDate;
    Spinner bloodGroup,gender;
    String fName,bGroup,dBirth,g;
    UserInformation userInformation;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    Button nextBt;
    int AUTOCOMPLETE_REQUEST_CODE =1;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_setup_personal_info, container, false);
        //all views initializing here
        fullName = view.findViewById(R.id.fullNameEt);
        dateOfBirth = view.findViewById(R.id.dateOfBirthEt);
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
        //final Calendar calendar1 = Calendar.getInstance();
        final java.util.Calendar calendar1 = java.util.Calendar.getInstance();
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
                new DatePickerDialog(getActivity(), date, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();
            }

        });


        final Calendar calendar2 = Calendar.getInstance();

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                new DatePickerDialog(getActivity(), date, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupprofileBt(v);
            }
        });
        return view;
    }

    public void setupprofileBt(View view) {
        final Fragment[] fragment = new Fragment[1];
        int fName = fullName.getText().toString().length();
        int dateOfBt = dateOfBirth.getText().toString().length();
        if(fName == 0){
            fullName.setError("Enter full name.");
        }
        if(dateOfBt == 0){
            dateOfBirth.setError("Enter your date of birth.");
        }
        else if(fName!=0 && dateOfBt!=0){
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
                    fragment[0] = new SetupHomeAddressFragment();
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
                    fragmentTransaction.replace(R.id.fragment,fragment[0]);
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


}
