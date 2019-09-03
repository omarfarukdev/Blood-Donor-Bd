package com.example.blooddonorbd.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.example.blooddonorbd.Adapters.EmergencyCallListAdapters;
import com.example.blooddonorbd.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyCallActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    ArrayList<String> list=new ArrayList<>();
    EmergencyCallListAdapters adapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        toolbar=findViewById(R.id.toolbar);
        listView=findViewById(R.id.numberlist);
        toolbar.setTitle("Emergency Call");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapters=new EmergencyCallListAdapters(this,0,list);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Emergency Call");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("ooooo",""+d.child("PhoneNo").getValue().toString());
                    list.add(d.child("PhoneNo").getValue().toString());
                    listView.setAdapter(adapters);
                    adapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

   /* public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        return true;
    }*/
}
