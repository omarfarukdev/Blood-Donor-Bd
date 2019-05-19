package com.example.blooddonorbd.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonorbd.Models.MessageInfo;
import com.example.blooddonorbd.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageListAdapters extends ArrayAdapter<MessageInfo> {
    Context context;
    ArrayList<MessageInfo> arrayList;

    public MessageListAdapters(Context context, int userlist, ArrayList<MessageInfo> arrayList) {
        super(context, 0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View view = convertView;
        Log.d( "arrrrr",String.valueOf(arrayList.get(position).getSender().trim().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim())));
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        if (arrayList.get(position).getReciver().equals(currentUser)){
            view = layoutInflater.inflate(R.layout.reciver_message_list,null);

            TextView msg=view.findViewById(R.id.recivemessage);
            TextView date=view.findViewById(R.id.reciverdate);
            msg.setText(arrayList.get(position).getMessage());
            date.setText(arrayList.get(position).getMessageTime());
        }
        else if(arrayList.get(position).getSender().equals(currentUser)){
            view = layoutInflater.inflate(R.layout.sender_message_list,null);

            TextView msg=view.findViewById(R.id.sendermessage);
            TextView date=view.findViewById(R.id.sendermessagedate);
            msg.setText(arrayList.get(position).getMessage());
            date.setText(arrayList.get(position).getMessageTime());
        }

        return view;
    }
}
