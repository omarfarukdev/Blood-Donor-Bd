package com.example.blooddonorbd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blooddonorbd.Activity.ChatActivity;
import com.example.blooddonorbd.Models.MessageHistoryInfo;
import com.example.blooddonorbd.Models.MessageInfo;
import com.example.blooddonorbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageHistoryAdapters extends RecyclerView.Adapter<MessageHistoryAdapters.ViewHolder> {
    int c = 0;
    Context context;
    ArrayList<MessageHistoryInfo> arrayList;
    String lastMessage,recever,messageTime,isSeen;

    int countNotification = 0;

    public MessageHistoryAdapters(Context context, int resource, ArrayList<MessageHistoryInfo> arrayList) {
        //super(context, 0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @Override
    public MessageHistoryAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.listview_messages, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHistoryAdapters.ViewHolder viewHolder, int i) {
        //viewHolder.name.setText(arrayList.get(i).getName()+" "+arrayList.size());
        final MessageHistoryInfo messageHistoryInfo = arrayList.get(i);
        lastMessage(messageHistoryInfo.getPhoneNum(),viewHolder.message,viewHolder.name,viewHolder.time,messageHistoryInfo.getName(),i);
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",messageHistoryInfo.getName());
                intent.putExtra("phoneNo",messageHistoryInfo.getPhoneNum());
                context.startActivity(intent);
            }
        });
       // if (arrayList.get(i).getIsSeen().equals("false") && arrayList.get(i))
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView message;
        public TextView time,emptyMessageTv;
        public ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.username);
            this.message = itemView.findViewById(R.id.messageTv);
            this.time = itemView.findViewById(R.id.timeanddate);

            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);


        }

}

    public void lastMessage(final String phnNo, final TextView addName, final TextView name, final TextView time, final String nameValue, final int position) {
        try {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        //Log.d("chiled",String.valueOf(dataSnapshot.getChildrenCount()));
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(d.getKey());
                        checkingRef(databaseReference2, d, phnNo,addName,name,time,nameValue,position);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }
    }

    private void checkingRef(DatabaseReference databaseReference2, final DataSnapshot d, final String phnNo, final TextView addName, final TextView name, final TextView time, final String nameValue, final int position) {
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if ((dataSnapshot.child("PhoneNo1").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo2").getValue().equals(phnNo))
                            || (dataSnapshot.child("PhoneNo2").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && dataSnapshot.child("PhoneNo1").getValue().equals(phnNo) )) {
                        //c++;
                        //if (c == 1) {
                            readMessage(d.getKey(),addName,name,time,nameValue,position);
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

    public void readMessage(final String chatKey, final TextView addName, final TextView name, final TextView time, final String nameValue, final int position) {

        final int temp = c;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                   try{
                       if (!d.getKey().equals("PhoneNo1") && !d.getKey().equals("PhoneNo2")) {
                           MessageInfo messageInfo = d.getValue(MessageInfo.class);
                           lastMessage  = messageInfo.getMessage();
                           recever = messageInfo.getReciver();
                           isSeen = messageInfo.getIsSeen();
                           messageTime = messageInfo.getMessageTime();
                       }
                   }catch (Exception e){}
                }
                try{
                    if (recever.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) && isSeen.equals("false")){
                        name.setText(nameValue);
                        time.setText(messageTime);
                        addName.setText(lastMessage);
                        name.setTextColor(Color.BLACK);
                        addName.setTextColor(Color.BLACK);
                        time.setTextColor(Color.BLACK);
                    }else {
                        name.setText(nameValue);
                        addName.setText(lastMessage);
                        time.setText(messageTime);
                        name.setTextColor(Color.GRAY);
                        addName.setTextColor(Color.GRAY);
                        time.setTextColor(Color.GRAY);
                    }
                }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
