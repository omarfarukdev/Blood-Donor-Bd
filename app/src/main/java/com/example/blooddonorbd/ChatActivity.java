package com.example.blooddonorbd;

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
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private Button backbt;
    private TextView name,phoneno;
    private ImageView sendBt;
    private  EditText editmessage;
    private ListView messagelist;
    private String recivername,recivernumber,currentnumber;
    private ArrayList <Integer> t=new ArrayList<>();
    private MessageListAdapters arrayAdapter;
    private ArrayList<MessageInfo>  mesList;
    private  ArrayList<String> conversationList = new ArrayList<>();
    private   int c=0;
    private MessageInfo messageInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
       // getSupportActionBar().hide();
        backbt=findViewById(R.id.backbt);
        name=findViewById(R.id.recivername);
        phoneno=findViewById(R.id.reciverphone);
        sendBt=findViewById(R.id.sentbt);
        editmessage=findViewById(R.id.editText);
        messagelist=findViewById(R.id.messagelist);

        Intent intent=getIntent();
        recivername=intent.getStringExtra("name");
        recivernumber=intent.getStringExtra("phoneNo");
        name.setText(recivername);
        phoneno.setText(recivernumber);
        currentnumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        mesList = new ArrayList<>();
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

                            messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber);
                            reference2.setValue(messageInfo);
                            editmessage.setText("");
                        }
                    }
                    else {
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                            checkingRef(databaseReference2, d);
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

    private void checkingRef(final DatabaseReference databaseReference2, final DataSnapshot d) {
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    if((dataSnapshot.child("PhoneNo1").getValue().equals(currentnumber)&&dataSnapshot.child("PhoneNo2").getValue().equals(recivernumber))||(dataSnapshot.child("PhoneNo1").getValue().equals(recivernumber)&&dataSnapshot.child("PhoneNo2").getValue().equals(currentnumber))){
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()).push();
                        t.add(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        if(editmessage.getText().toString().length()!=0){

                            messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber);
                            reference.setValue(messageInfo);
                            editmessage.setText("");
                        }
                        else {

                        }

                        c++;
                        if(c==1){
                            conversation(FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey()));
                        }
                    }
                    else {
                        t.add(0);
                    }

                }

                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (t.contains(1)){

        }
        else {
            NewId();
        }

    }

    private void NewId() {
        if(t.contains(0)){
            if(editmessage.getText().toString().length()!=0){
            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Chat").push();
            reference1.child("PhoneNo1").setValue(currentnumber);
            reference1.child("PhoneNo2").setValue(recivernumber);
            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Chat").child(reference1.getKey()).push();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

                messageInfo = new MessageInfo(editmessage.getText().toString(),currentDateandTime,recivernumber,currentnumber);
            reference2.setValue(messageInfo);

            editmessage.setText("");
            t.add(0);}

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
                        Log.d("Sender",conversationList.get(3));
                        messageInfo = new MessageInfo(conversationList.get(0), conversationList.get(1), conversationList.get(2), conversationList.get(3));
                        mesList.add(messageInfo);
                        //messagelist.setAdapter(arrayAdapter);
                        // arrayAdapter.notifyDataSetChanged();
                        messagelist.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
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
    }
}
