package com.example.blooddonorbd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.example.blooddonorbd.Adapters.BloodOrganizationListAdaptrs;
import com.example.blooddonorbd.Models.BloodBankInfo;
import com.example.blooddonorbd.Models.BloodOrganizationInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BloodOrganizationActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private ListView bloodorga;
   private String city;
   private ArrayList<BloodOrganizationInfo> bloodorgalist;
   private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_organization);
        toolbar= findViewById(R.id.toolbar);
        bloodorga=findViewById(R.id.bloodorganization);
        toolbar.setTitle("Blood organization");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bloodorgalist=new ArrayList<>();
        city = getIntent().getStringExtra("city");
        final BloodOrganizationListAdaptrs bloodOrganizationListAdaptrs=new BloodOrganizationListAdaptrs(this,0,bloodorgalist);
        reference= FirebaseDatabase.getInstance().getReference().child("Blood Organization");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                try{
                    if(d.child("City").getValue().toString().equals(city.trim())){
                        Log.d("MMMM",""+d.child("Phone").getValue().toString());
                        BloodOrganizationInfo bloodOrganizationInfo=new BloodOrganizationInfo(d.child("Name").getValue().toString(),d.child("Phone").getValue().toString());
                        bloodorgalist.add(bloodOrganizationInfo);
                        bloodorga.setAdapter(bloodOrganizationListAdaptrs);
                        bloodOrganizationListAdaptrs.notifyDataSetChanged();
                    }
                }
                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        return true;
    }
}
