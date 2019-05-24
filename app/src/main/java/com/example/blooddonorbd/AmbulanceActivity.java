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

import com.example.blooddonorbd.Adapters.AmbulanceListAdapters;
import com.example.blooddonorbd.Models.AmbulanceInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AmbulanceActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private ListView ambulList;
   private String city;
   private DatabaseReference databaseReference;
   private ArrayList<AmbulanceInfo> amblist;
   private AmbulanceListAdapters ambulanceListAdapters;
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
        amblist=new ArrayList<>();
        Log.d("oaaaa",""+city);
        ambulanceListAdapters=new AmbulanceListAdapters(this,0,amblist);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Ambulance");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("ooo",""+d.child("City").getValue().toString());
                    try{
                        if(d.child("City").getValue().toString().equals(city.trim())){
                            Log.d("ffff",""+city);
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
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem search = menu.findItem(R.id.searchMainMenu);
        SearchView searchView = (SearchView) search.getActionView();
        searchView = (android.support.v7.widget.SearchView) search.getActionView();
        SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);

       // SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        return true;
    }
}
