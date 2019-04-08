package com.example.blooddonorbd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonorbd.Models.UserInformation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetupProfileActivity extends AppCompatActivity {
    EditText fullName,dateOfBirth;
    Spinner bloodGroup,gender;
    String fName,bGroup,dBirth,g;
    UserInformation userInformation;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        getSupportActionBar().hide();//hiding actionbar here

        //all views initializing here
        fullName = findViewById(R.id.fullNameEt);
        dateOfBirth = findViewById(R.id.dateOfBirthEt);
        bloodGroup = findViewById(R.id.bloodGroupSp);
        gender = findViewById(R.id.genderSP);

        //fName = fullName.getText().toString();
        //dBirth = dateOfBirth.getText().toString();

        userInformation = new UserInformation(fullName.getText().toString(),bGroup,dateOfBirth.getText().toString(),g);

        // Creating adapter for spinner
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroup));
        bloodGroup.setAdapter(bloodAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.gender));
        gender.setAdapter(genderAdapter);

        //bGroup = String.valueOf();//selected item from spinner
        //g = String.valueOf();

        //google map api client
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    }
    public void Birthdate(View view) {

    }

    public void setupprofileBt(View view) {
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
                Intent intent = new Intent(SetupProfileActivity.this,DiscoverableActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
       // Toast.makeText(this, ""++" "+, Toast.LENGTH_SHORT).show();
        if(fullName.getText().toString().length() == 0){
            fullName.setError("Enter full name.");
        }
        if(dateOfBirth.getText().toString().length() == 0){
            dateOfBirth.setError("Enter your date of birth.");
        }
    }
}
