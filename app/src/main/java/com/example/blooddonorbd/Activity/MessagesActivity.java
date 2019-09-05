package com.example.blooddonorbd.Activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.blooddonorbd.Adapters.MessageHistoryAdapters;
import com.example.blooddonorbd.Models.MessageHistoryInfo;
import com.example.blooddonorbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {
    ArrayList<String> arrayList;
    ArrayList<MessageHistoryInfo> messageHistoryInfoArrayList;
    ArrayList<MessageHistoryInfo> messageHistoryInfoArrayListTemp;
    private MessageHistoryInfo messageHistoryInfo;
    private MessageHistoryAdapters messageHistoryAdapters;
    ArrayList<Integer> chatChildrenCount;
    RecyclerView recyclerView;
    String lastMsg;
    SharedPreferences.Editor editor;
    int c = 0,trueCount = 0,falseCount = 0;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
//        getSupportActionBar().hide();
        arrayList = new ArrayList<>();
        messageHistoryInfoArrayList = new ArrayList<>();
        messageHistoryInfoArrayListTemp = new ArrayList<>();
        chatChildrenCount = new ArrayList<>();

        //recyclerViewEmptyTxt = findViewById(R.id.recyclerViewEmptyMsgTv);

        recyclerView = findViewById(R.id.recyclerView);
        messageHistoryAdapters = new MessageHistoryAdapters(this,0,messageHistoryInfoArrayListTemp);

        readUser();



        Log.d("lllll","true"+arrayList.size());

    }

    private void readUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Log.d("dddd", String.valueOf(dataSnapshot.getChildren()));
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    //Log.d("dddd", String.valueOf(d.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())));
                    try{
                        if (d.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){
                            //lastMessage((String) d.child("PhoneNo2").getValue(),"ttt");
                            redUserDetailes((String) d.child("PhoneNo2").getValue());
                        }
                        else if (d.child("PhoneNo2").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){
                           // lastMessage((String) d.child("PhoneNo1").getValue(),"ttt");
                            redUserDetailes((String) d.child("PhoneNo1").getValue());
                        }
                    }catch (Exception e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void redUserDetailes(final String key) {//getting oposit user name
        messageHistoryInfoArrayListTemp.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageHistoryInfo = new MessageHistoryInfo(key, (String) dataSnapshot.child("Full Name").getValue(), "false", "df", "123");
               /* messageHistoryInfo = new MessageHistoryInfo(key, (String) dataSnapshot.child("Full Name").getValue(),messageHistoryInfoArrayList.get(messageHistoryInfoArrayList.size()-1).getIsSeen(),
                        messageHistoryInfoArrayList.get(messageHistoryInfoArrayList.size()-1).getMessage());*/
                messageHistoryInfoArrayListTemp.add(messageHistoryInfo);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
                recyclerView.setAdapter(messageHistoryAdapters);
                //pgsBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void backBt(View view) {
        finish();
    }
}
