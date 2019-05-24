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

import com.example.blooddonorbd.Adapters.BloodBankListAdapters;
import com.example.blooddonorbd.Models.BloodBankInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BloodBankActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView bloodlist;
    private String city;
    private ArrayList<BloodBankInfo> bloodBanklist;
    private BloodBankListAdapters bloodBankListAdapters;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        toolbar=findViewById(R.id.toolbar);
        bloodlist=findViewById(R.id.bloodbanklist);
        toolbar.setTitle("Blood Bank");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bloodBanklist=new ArrayList<>();
        city = getIntent().getStringExtra("city");
        bloodBankListAdapters=new BloodBankListAdapters(this,0,bloodBanklist);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Blood Bank");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("OOOO",""+d.child("City").getValue().toString());
                    try{
                        if(d.child("City").getValue().toString().equals(city.trim())){
                            Log.d("MMMM",""+d.child("Phone").getValue().toString());
                            BloodBankInfo bloodBankInfo=new BloodBankInfo(d.child("Name").getValue().toString(),d.child("Phone").getValue().toString(),d.child("Address").getValue().toString());
                            bloodBanklist.add(bloodBankInfo);
                            bloodlist.setAdapter(bloodBankListAdapters);
                            bloodBankListAdapters.notifyDataSetChanged();
                        }
                    }
                    catch (Exception e){

                    }
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
