package com.example.blooddonorbd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.example.blooddonorbd.Adapters.HospitalListAdapters;
import com.example.blooddonorbd.Models.BloodBankInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ListView hospitallist;
    DatabaseReference reference;
    ArrayList<BloodBankInfo> hospiList;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        hospitallist=findViewById(R.id.hospitallist);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Hospital");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hospiList=new ArrayList<>();
        city = getIntent().getStringExtra("city");
        final HospitalListAdapters hospitalListAdapters=new HospitalListAdapters(this,0,hospiList);
        reference= FirebaseDatabase.getInstance().getReference().child("Hospital");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                    try{
                        if(d.child("City").getValue().toString().equals(city.trim())){
                            Log.d("MMMM",""+d.child("PhoneNo").getValue().toString());
                            BloodBankInfo bloodBankInfo=new BloodBankInfo(d.child("Name").getValue().toString(),d.child("PhoneNo").getValue().toString(),d.child("Address").getValue().toString());
                            hospiList.add(bloodBankInfo);
                            hospitallist.setAdapter(hospitalListAdapters);
                            hospitalListAdapters.notifyDataSetChanged();
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
