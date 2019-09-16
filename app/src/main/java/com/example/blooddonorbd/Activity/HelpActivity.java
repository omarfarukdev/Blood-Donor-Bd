package com.example.blooddonorbd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import com.example.blooddonorbd.Adapters.HelpListAdapters;
import com.example.blooddonorbd.Models.HelpInfo;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    GridView helplist;
    ArrayList<HelpInfo> imagrList=new ArrayList<>();
    String city;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helplist=findViewById(R.id.gridview);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
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

        imagrList.add(new HelpInfo("Blood Organization",R.drawable.blood));
        imagrList.add(new HelpInfo("Blood Bank",R.drawable.blood_bank));
        imagrList.add(new HelpInfo("Ambulance",R.drawable.ambulance));
        imagrList.add(new HelpInfo("Hospital",R.drawable.hospital));
        imagrList.add(new HelpInfo("Emergency Call",R.drawable.communications));

        HelpListAdapters adapters=new HelpListAdapters(this,R.layout.help_listview_shape,imagrList);
        helplist.setAdapter(adapters);

        helplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Omar","oma"+position);
                if (position==0){
                    Intent intent=new Intent(HelpActivity.this,BloodOrganizationActivity.class);
                    intent.putExtra("city",city);
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent=new Intent(HelpActivity.this,BloodBankActivity.class);
                    intent.putExtra("city",city);
                    startActivity(intent);
                }
                else if(position==2){
                    Intent intent=new Intent(HelpActivity.this,AmbulanceActivity.class);
                    intent.putExtra("city",city);
                    startActivity(intent);
                }
                else if(position==3){
                    Intent intent=new Intent(HelpActivity.this,HospitalActivity.class);
                    intent.putExtra("city",city);
                    startActivity(intent);
                }
                else if(position==4){
                    Intent intent=new Intent(HelpActivity.this,EmergencyCallActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void backbut(View view) {finish();
    }
}
