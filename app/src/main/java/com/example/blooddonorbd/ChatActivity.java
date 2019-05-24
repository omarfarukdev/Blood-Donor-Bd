package com.example.blooddonorbd;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Adapters.MessageListAdapters;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private Button backbt;
    private TextView name,phoneno;
    private ImageView sendBt;
    private  EditText editmessage;
    private ListView messagelist;
    private String recivername,recivernumber,currentnumber;
    ArrayList <Integer> t;
    private MessageListAdapters arrayAdapter;
    private ArrayList<MessageInfo>  mesList;
    private  ArrayList<String> conversationList = new ArrayList<>();
    private   int c=0,cc=0,ccc = 0,p=0;
    private MessageInfo messageInfo;
    String reciver,isSeen;
    DataSnapshot dd;
    ArrayList<DataSnapshot> dataSnapshotsList;
    private LinearLayout linearLayout;

    private boolean checkState = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        backbt = findViewById(R.id.backbt);
        name = findViewById(R.id.recivername);
        phoneno = findViewById(R.id.reciverphone);
        sendBt = findViewById(R.id.sentbt);
        editmessage = findViewById(R.id.editText);
        messagelist = findViewById(R.id.messagelist);
        linearLayout = findViewById(R.id.chatActivity);

        Intent intent=getIntent();
        recivername=intent.getStringExtra("name");
        recivernumber=intent.getStringExtra("phoneNo");

        name.setText(recivername);
        phoneno.setText(recivernumber);
        currentnumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        mesList = new ArrayList<>();
        t=new ArrayList<>();
        dataSnapshotsList = new ArrayList<>();

        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editmessage.getText().toString().length()!=0){
                    setup();
                }
                else {
                    Toast.makeText(ChatActivity.this,"Empty Message",Toast.LENGTH_SHORT).show();
                }
            }
        });
        try{
            arrayAdapter=new MessageListAdapters(this,0,mesList);
        }
        catch (Exception e){

        }
        setup();

    }
    private void setup(){
        try{
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getChildrenCount()==0){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        if (editmessage.getText().toString().length()!=0){
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Chat").push();
                            databaseReference1.child("PhoneNo1").setValue(currentnumber);
                            databaseReference1.child("PhoneNo2").setValue(recivernumber);
                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Chat").child(databaseReference1.getKey()).push();
                            cc = 1;
                           // if (recivernumber.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                            messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber,"false");
                            reference2.setValue(messageInfo);
                            editmessage.setText("");
                           setup();
                        }
                    }
                    else {
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            //Log.d("chiled",String.valueOf(dataSnapshot.getChildrenCount()));
                            DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                            checkingRef(databaseReference2, d, (int) dataSnapshot.getChildrenCount());
                            checkingRefForReadingMessage(databaseReference2, d);
                        }
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


    private void checkingRef(final DatabaseReference databaseReference2, final DataSnapshot d,int childrenCount) {
        //Log.d("check", d.getKey());
        //int funcCallCount;

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if((dataSnapshot.child("PhoneNo1").getValue().equals(currentnumber) && dataSnapshot.child("PhoneNo2").getValue().equals(recivernumber))||
                            (dataSnapshot.child("PhoneNo1").getValue().equals(recivernumber) && dataSnapshot.child("PhoneNo2").getValue().equals(currentnumber))){

                        //Log.d("currr",(dataSnapshot.child("PhoneNo1").getValue().equals(currentnumber)+" recc"+(dataSnapshot.child("PhoneNo2").getValue().equals(recivernumber))+" "+t.size()));
                        cc = 1;
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()).push();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        if(editmessage.getText().toString().length()!=0){
                            messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber,"false");
                            reference.setValue(messageInfo);
                            editmessage.setText("");
                        }
                        else {

                        }

                        c++;
                        if(c==1){
                            conversation(FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()));
                            //readMessage(d.getKey());
                        }

                    }else {
                        //addList(0);
                    }


                }

                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (cc!=1){
            NewId();
        }

    }

    private void checkingRefForReadingMessage(DatabaseReference databaseReference2, final DataSnapshot d) {

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if((dataSnapshot.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo2").getValue().equals(recivernumber))
                            || (dataSnapshot.child("PhoneNo2").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo1").getValue().equals(recivernumber) )){
                        ccc++;
                       // if (ccc==1){
                            readMessage(d.getKey());
                      //  }
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

    public void readMessage(final String chatKey) {
        Log.d("uuuuu", "called");
        final int temp = c;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshotsList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                if (!d.getKey().equals("PhoneNo1") && !d.getKey().equals("PhoneNo2")) {

                    MessageInfo messageInf = d.getValue(MessageInfo.class);
                    reciver = messageInf.getReciver();
                    isSeen = messageInf.getIsSeen();
                    if (messageInf.getIsSeen().equals("false") && messageInf.getReciver().equals(currentnumber) && !messageInf.getSender().equals(currentnumber) && linearLayout.getVisibility() == View.VISIBLE ) {
                        p++;
                        //Log.d("vvv", ""+checkState);
/*
                        //if (p == 1) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", "true");
                        d.getRef().updateChildren(hashMap);

                        arrayAdapter.notifyDataSetChanged();
                        //}*/
                        dataSnapshotsList.add(d);
                    }
                }
                }

                Log.d("uuuuu", ""+dataSnapshotsList.size());
                for (int i=0;i<dataSnapshotsList.size();i++){
                    //if (reciver.equals(currentnumber)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", "true");
                        dataSnapshotsList.get(i).getRef().updateChildren(hashMap);
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void NewId() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.d("count", String.valueOf(cc!=1));
            if(editmessage.getText().toString().length()!=0){
            DatabaseReference reference1=database.child("Chat").push();
            reference1.child("PhoneNo1").setValue(currentnumber);
            reference1.child("PhoneNo2").setValue(recivernumber);
            DatabaseReference reference2=database.child("Chat").child(reference1.getKey()).push();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber,"false");
            reference2.setValue(messageInfo);

            editmessage.setText("");
            setup();

            cc++;
        }

    }
private void conversation(DatabaseReference databaseReference){
   // Log.d("msg","1");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String msg = null, time = null, sender = null, reciver = null;

                    if (!dataSnapshot.getKey().equals("PhoneNo1") && !dataSnapshot.getKey().equals("PhoneNo2")) {
                        if (d.getKey().equals("message")) {
                            msg = d.getValue().toString();
                            if (msg != null) {
                                conversationList.add(msg);
                            }
                        }
                        if (d.getKey().equals("messageTime")) {
                            time = d.getValue().toString();
                            if (time != null) {
                                conversationList.add(time);
                            }
                        }
                        if (d.getKey().equals("reciver")) {
                            reciver = d.getValue().toString();
                            if (reciver != null) {
                                conversationList.add(reciver);
                            }
                        }
                        if (d.getKey().equals("sender")) {
                            sender = d.getValue().toString();
                            if (sender != null) {
                                conversationList.add(sender);
                            }
                        }
                    }
                   // Log.d("msg", msg);
                    //Log.d("msg2", time);
                    //Log.d("msg3", sender);
                    int n = conversationList.size();
                    if (n == 4) {
                        //Log.d("Sender",conversationList.get(3));

                        //if (checkState)
                        messageInfo = new MessageInfo(conversationList.get(0), conversationList.get(1), conversationList.get(2), conversationList.get(3),"false");
                        mesList.add(messageInfo);
                        messagelist.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        Log.d("conversa","1");
                        conversationList.clear();
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
    public void backBt(View view) {
        finish();
        //System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkState = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //System.exit(0);
    }
}
