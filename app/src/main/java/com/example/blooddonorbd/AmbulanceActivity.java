package com.example.blooddonorbd;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.blooddonorbd.Adapters.AmbulanceListAdapters;
import com.example.blooddonorbd.Models.AmbulanceInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class AmbulanceActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private ListView ambulList;
   private String city,cityname,citypostcode;
   private DatabaseReference databaseReference,databaseReference2;
   ArrayList<AmbulanceInfo> amblist;
   private AmbulanceListAdapters ambulanceListAdapters;
   private MaterialSearchView materialSearchView;
   String [] city1;
   String [] district;
   String d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);
        toolbar=findViewById(R.id.toolbar);
        ambulList=findViewById(R.id.ambulancelist);
        toolbar.setTitle("Ambulance");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        city = getIntent().getStringExtra("city");

        district = new String[]{"Barguna","Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur","Bandarban","Brahmanbaria","Chandpur ",
                "Chittagong","Comilla","Cox's Bazar","Feni","Khagrachhari","Lakshmipur","Noakhali","Rangamati","Dhaka","Faridpur","Gazipur","Gopalganj"
                ,"Kishoreganj","Madaripur","Manikganj","Munshiganj","Narayanganj","Narsingdi","Rajbari","Shariatpur","Tangail","Bagerhat","Chuadanga",
                "Jessore","Jhenaidah","Khulna","Kushtia","Magura","Meherpur","Narail","Satkhira","Jamalpur","Mymensingh","Netrokona","Sherpur","Bogra"
                ,"Joypurhat","Naogaon","Natore","Chapainawabganj","Pabna","Rajshahi","Sirajganj","Dinajpur","Gaibandha","Kurigram","Lalmonirhat",
                "Nilphamari","Panchagarh","Rangpur","Thakurgaon","Habiganj","Moulvibazar","Sunamganj","Sylhet"};

        amblist=new ArrayList<>();
        ambulanceListAdapters=new AmbulanceListAdapters(this,0,amblist);
        try{
             city1=city.split(" ",3);
             cityname=city1[1].toString();
             citypostcode=city1[2];
        }
        catch (Exception e)
        {

        }
       // ambulenceList();
        Log.d("oaaaa",""+citypostcode);
        Log.d("offf",""+city);
        Log.d("fao",""+cityname);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Ambulance");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("ooo",""+d.child("City").getValue().toString().equals(cityname));
                    try{
                        if(d.child("City").getValue().toString().equals(cityname.trim())){
                            Log.d("MMMM",""+d.child("Phone").getValue().toString());
                            AmbulanceInfo ambulanceInfo = new AmbulanceInfo(d.child("Address").getValue().toString(),d.child("Phone").getValue().toString());
                            amblist.add(ambulanceInfo);
                            ambulList.setAdapter(ambulanceListAdapters);
                            ambulanceListAdapters.notifyDataSetChanged();
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
                //search(newText.toString());
                return false;
            }
        });
    }

    private void ambulenceList() {


    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        materialSearchView.setMenuItem(item);
       /* MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem search = menu.findItem(R.id.searchMainMenu);
        SearchView searchView = (SearchView) search.getActionView();
        searchView = (android.support.v7.widget.SearchView) search.getActionView();
        SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);*/

       // SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        return true;
    }
    public void search(final String query){
        amblist.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    //Log.d("ooo",""+d.child("City").getValue().toString());
                    try{
                        if(d.child("City").getValue().toString().equals(query.trim())){
                            // Log.d("ffff",""+city);
                            AmbulanceInfo ambulanceInfo = new AmbulanceInfo(d.child("Address").getValue().toString(),d.child("Phone").getValue().toString());
                            amblist.add(ambulanceInfo);
                            ambulList.setAdapter(ambulanceListAdapters);
                            ambulanceListAdapters.notifyDataSetChanged();
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
