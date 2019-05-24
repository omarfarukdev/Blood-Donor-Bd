package com.example.blooddonorbd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blooddonorbd.Adapters.MessageHistoryAdapters;
import com.example.blooddonorbd.Models.MessageHistoryInfo;
import com.example.blooddonorbd.Models.MessageInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    ProgressBar pgsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
//        getSupportActionBar().hide();
        arrayList = new ArrayList<>();
        messageHistoryInfoArrayList = new ArrayList<>();
        messageHistoryInfoArrayListTemp = new ArrayList<>();
        chatChildrenCount = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        pgsBar = (ProgressBar)findViewById(R.id.pBar);
        //pgsBar.setVisibility(View.GONE);
        messageHistoryAdapters = new MessageHistoryAdapters(this,0,messageHistoryInfoArrayListTemp);

        /*Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readUser();
            }
        };*/
        readUser();
        //thread.start();

        //if(listView.getVisibility() == View.VISIBLE){
            Log.d("lllll","true"+arrayList.size());

       // }else {
            pgsBar.setVisibility(View.VISIBLE);
            //Log.d("lllll","false");
       // }

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessagesActivity.this,ChatActivity.class);

                intent.putExtra("name",messageHistoryInfoArrayListTemp.get(position).getName());
                intent.putExtra("phoneNo",messageHistoryInfoArrayListTemp.get(position).getPhoneNum());
                startActivity(intent);
            }
        });*/

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
    public void redUserDetailes(final String key){//getting oposit user name
        messageHistoryInfoArrayListTemp.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("arrSize",""+messageHistoryInfoArrayList.size());


                messageHistoryInfo = new MessageHistoryInfo(key,(String) dataSnapshot.child("Full Name").getValue(),"false","df","123");
               /* messageHistoryInfo = new MessageHistoryInfo(key, (String) dataSnapshot.child("Full Name").getValue(),messageHistoryInfoArrayList.get(messageHistoryInfoArrayList.size()-1).getIsSeen(),
                        messageHistoryInfoArrayList.get(messageHistoryInfoArrayList.size()-1).getMessage());*/
                messageHistoryInfoArrayListTemp.add(messageHistoryInfo);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
                recyclerView.setAdapter(messageHistoryAdapters);
                pgsBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
   /* public void lastMessage(final String userPhNo, final String userName){
        Log.d("cccc", String.valueOf(userName));
        try{
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            //Log.d("chiled",String.valueOf(dataSnapshot.getChildrenCount()));
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                            checkingRef(databaseReference2, d, (int) d.getChildrenCount(),userPhNo,userName);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e){

        }
    }

    private void checkingRef(DatabaseReference databaseReference2, final DataSnapshot d, final int childrenCount, final String userPhNo, final String userName) {
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if((dataSnapshot.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo2").getValue().equals(userPhNo))
                    || (dataSnapshot.child("PhoneNo2").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo1").getValue().equals(userPhNo) )){
                        c++;
                        chatChildrenCount.add(childrenCount);
                        if (c==1){
                            readMessage(d.getKey(),userName,userPhNo,childrenCount);
                        }
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
    public void readMessage(final String chatKey, final String userName, final String userPhnNo, int childrenCount) {
        //Log.d("oooo",""+childrenCount);
        final int temp = c;
        //int a = 0,b=0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Log.d("chhhhh",""+dataSnapshot.getChildrenCount());
                if(dataSnapshot.getKey().equals(chatKey)){
                    Log.d("chhhhh",""+dataSnapshot.getChildrenCount());
                }
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    //messageHistoryInfoArrayList.clear();
                    if (!d.getKey().equals("PhoneNo1") && !d.getKey().equals("PhoneNo2")) {
                        MessageInfo messageInfo = d.getValue(MessageInfo.class);
                        if (messageInfo.getIsSeen().equals("false") && messageInfo.getReciver().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {

                                messageHistoryInfo = new MessageHistoryInfo(userPhnNo,userName,"false",messageInfo.getMessage(),chatKey);
                                messageHistoryInfoArrayList.add(messageHistoryInfo);

                        }else {
                                messageHistoryInfo = new MessageHistoryInfo(userPhnNo,userName,"true",messageInfo.getMessage(),chatKey);
                                messageHistoryInfoArrayList.add(messageHistoryInfo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    public void backBt(View view) {
        finish();
    }
}
