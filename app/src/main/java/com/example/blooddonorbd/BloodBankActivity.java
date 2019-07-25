package com.example.blooddonorbd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.blooddonorbd.Adapters.BloodBankListAdapters;
import com.example.blooddonorbd.Models.AmbulanceInfo;
import com.example.blooddonorbd.Models.BloodBankInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class BloodBankActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView bloodlist;
    private String city,cityname,citypostcode;
    private ArrayList<BloodBankInfo> bloodBanklist;
    private BloodBankListAdapters bloodBankListAdapters;
    private DatabaseReference databaseReference;
    private MaterialSearchView materialSearchView;
    String [] city1;
    String [] district;
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
        district = new String[]{"Barguna","Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur","Bandarban","Brahmanbaria","Chandpur ",
                "Chittagong","Comilla","Cox's Bazar","Feni","Khagrachhari","Lakshmipur","Noakhali","Rangamati","Dhaka","Faridpur","Gazipur","Gopalganj"
                ,"Kishoreganj","Madaripur","Manikganj","Munshiganj","Narayanganj","Narsingdi","Rajbari","Shariatpur","Tangail","Bagerhat","Chuadanga",
                "Jessore","Jhenaidah","Khulna","Kushtia","Magura","Meherpur","Narail","Satkhira","Jamalpur","Mymensingh","Netrokona","Sherpur","Bogra"
                ,"Joypurhat","Naogaon","Natore","Chapainawabganj","Pabna","Rajshahi","Sirajganj","Dinajpur","Gaibandha","Kurigram","Lalmonirhat",
                "Nilphamari","Panchagarh","Rangpur","Thakurgaon","Habiganj","Moulvibazar","Sunamganj","Sylhet"};

        bloodBanklist=new ArrayList<>();
        city = getIntent().getStringExtra("city");
        try{
            city1=city.split(" ");
            cityname=city1[1];
            citypostcode=city1[2];
        }
        catch (Exception e)
        {

        }
        Log.d("ofar",""+cityname);

        bloodBankListAdapters=new BloodBankListAdapters(this,0,bloodBanklist);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Blood Bank");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("OOOO",""+d.child("City").getValue().toString());
                    try{
                        if(d.child("City").getValue().toString().equals(cityname.trim())){
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
        materialSearchView = (MaterialSearchView)findViewById(R.id.mysearch);
        materialSearchView.clearFocus();
        materialSearchView.setSuggestions(district);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Log.d("Omar",""+query);
                search(query.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        materialSearchView.setMenuItem(item);
        return true;
    }
    public void search(final String query){
        bloodBanklist.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    //Log.d("ooo",""+d.child("City").getValue().toString());
                    try{
                        if(d.child("City").getValue().toString().equals(query.trim())){
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
}
