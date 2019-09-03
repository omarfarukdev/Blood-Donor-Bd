package com.example.blooddonorbd.Classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Activity.HomeActivity;
import com.example.blooddonorbd.Models.MessageInfo;
import com.example.blooddonorbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadMessage {


    String recever,isSeen;
    int messageCount = 0;
    TextView countTv;
    Context context;
    int key;
    TextView txtView;
    TextView txtViewProfile;
    public ReadMessage(Context context,int key){
        this.context = context;
        this.key = key;
        //this.countTv = countTv;
    }

    public void readUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    lastMessage(d.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void lastMessage(final String phnnNo) {
        try {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        //Log.d("chiled",String.valueOf(dataSnapshot.getChildrenCount()));
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                        checkingRef(databaseReference2, d,phnnNo);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }
    }

    private void checkingRef(DatabaseReference databaseReference2, final DataSnapshot d, final String phnNo) {
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if ((dataSnapshot.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo2").getValue().equals(phnNo))
                            || (dataSnapshot.child("PhoneNo2").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo1").getValue().equals(phnNo) )) {
                        //c++;
                        //if (c == 1) {
                        readMessage(d.getKey());
                        //}
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int readMessage(String chatKey) {
        // final int temp = c;
        if (key == 1){
           txtView = (TextView) ((Activity)context).findViewById(R.id.badge);
        }else if (key == 2){
           txtViewProfile = (TextView) ((Activity)context).findViewById(R.id.badgeOnProfile);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    try{
                        if (!d.getKey().equals("PhoneNo1") && !d.getKey().equals("PhoneNo2")) {
                            MessageInfo messageInfo = d.getValue(MessageInfo.class);
                            //lastMessage  = messageInfo.getMessage();
                            recever = messageInfo.getReciver();
                            isSeen = messageInfo.getIsSeen();
                            //messageTime = messageInfo.getMessageTime();
                        }
                    }catch (Exception e){}
                }
                try{
                    if (recever.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && isSeen.equals("false")){
                       /* name.setText(nameValue);
                        time.setText(messageTime);
                        addName.setText(lastMessage);
                        name.setTextColor(Color.BLACK);
                        addName.setTextColor(Color.BLACK);
                        time.setTextColor(Color.BLACK);*/
                        messageCount++;
                       if (key == 1){
                           txtView.setVisibility(View.VISIBLE);
                           txtView.setText(String.valueOf(messageCount));
                       }
                       else if (key == 2){
                           txtViewProfile.setVisibility(View.VISIBLE);
                           //return messageCount;
                           txtViewProfile.setText(String.valueOf(messageCount));
                           //Toast.makeText(HomeActivity.this, ""+messageCount, Toast.LENGTH_SHORT).show();
                       }

                    }else {
                        /*name.setText(nameValue);
                        addName.setText(lastMessage);
                        time.setText(messageTime);
                        name.setTextColor(Color.GRAY);
                        addName.setTextColor(Color.GRAY);
                        time.setTextColor(Color.GRAY);*/
                    }
                }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return 0;
    }
}
