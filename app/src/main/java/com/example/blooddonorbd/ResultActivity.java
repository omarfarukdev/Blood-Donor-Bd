package com.example.blooddonorbd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Adapters.UserListAdapters;
import com.example.blooddonorbd.Models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private String country,city,state,fullAddress,bloodGroup;
    UserInformation userInformation;
    UserListAdapters userListAdapters;
    ArrayList<UserInformation> userList;
    ListView listView;
    TextView bloodGrpTv,noOfDonorTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().hide();

        listView = findViewById(R.id.userListListView);
        bloodGrpTv = findViewById(R.id.bloodgrpTv);
        noOfDonorTv = findViewById(R.id.noOfDonotTv);

        fullAddress = getIntent().getStringExtra("fullAddress");
        state = getIntent().getStringExtra("state");
        city = getIntent().getStringExtra("city");
        country = getIntent().getStringExtra("country");
        bloodGroup = getIntent().getStringExtra("bloodGroup");

        bloodGrpTv.setText(bloodGroup);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        userList = new ArrayList<>();

        //adapters
        userListAdapters = new UserListAdapters(this,userList);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int c = 0;
                //Toast.makeText(HomeActivity.this, ""+dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).getKey(), Toast.LENGTH_SHORT).show();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    try {
                        //Toast.makeText(ResultActivity.this, "", Toast.LENGTH_SHORT).show();
                        String fName,address,dateOfBirth,gender,bldGroup,phnNo;
                        String statee = d.child("State").getValue().toString();
                        String city = d.child("City").getValue().toString();

                        String[] stateList = statee.split(" ");
                        String[] cityList = city.split(" ");

                        String[] deviceState = state.split(" ");
                        String[] deviceCity = city.split(" ");

                        if (!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())&& d.child("Blood Group").getValue().equals(bloodGroup) &&(d.child("Location").getValue().equals("On")) && (d.child("Full address").getValue().equals(fullAddress))){
                            Toast.makeText(ResultActivity.this, "blood group find "+d.child("Blood Group").getValue().equals(bloodGroup), Toast.LENGTH_SHORT).show();
                            fName = d.child("Full Name").getValue().toString();
                            address = d.child("Full address").getValue().toString();
                            dateOfBirth = d.child("Date of birth").getValue().toString();
                            bldGroup = d.child("Blood Group").getValue().toString();
                            phnNo = d.getKey();
                            gender = d.child("Gender").getValue().toString();
                            userInformation = new UserInformation(fName,address,dateOfBirth,bldGroup,phnNo,gender);
                            userList.add(userInformation);
                            listView.setAdapter(userListAdapters);
                            c++;
                        }else if(!d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())&& (d.child("Location").getValue().equals("On"))&&d.child("Blood Group").getValue().equals(bloodGroup) &&
                                (checkString(stateList,deviceState) == true) && (checkString(cityList,deviceCity) == true)){
                            fName = d.child("Full Name").getValue().toString();
                            address = d.child("Full address").getValue().toString();
                            dateOfBirth = d.child("Date of birth").getValue().toString();
                            bldGroup = d.child("Blood Group").getValue().toString();
                            phnNo = d.getKey();
                            gender = d.child("Gender").getValue().toString();
                            userInformation = new UserInformation(fName,address,dateOfBirth,bldGroup,phnNo,gender);
                            userList.add(userInformation);
                            listView.setAdapter(userListAdapters);
                            c++;
                        }

                    }catch (Exception e){
                        Toast.makeText(ResultActivity.this, ""+e+" "+c, Toast.LENGTH_SHORT).show();
                    }

                }
                noOfDonorTv.setText("We found "+String.valueOf(c)+" donors available");
               // donotNumberTv.setText(String.valueOf(c));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this,ResultDetails.class);
                intent.putExtra("name",userList.get(position).getfName());
                intent.putExtra("dateOfBirth",userList.get(position).getDateOfBirth());
                intent.putExtra("gender",userList.get(position).getGender());
                intent.putExtra("phoneNo",userList.get(position).getPhnNo());
                startActivity(intent);
            }
        });
        
    }

    boolean checkString(String[] arr1,String[] arr2){
        for (String s:arr1){
            for (String s2:arr2){
                if(s.equals(s2)){
                    return true;
                }
            }
        }
        return false;
    }

    public void image(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    public void backBt(View view) {
        finish();
    }
}
