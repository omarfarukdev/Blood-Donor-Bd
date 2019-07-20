package com.example.blooddonorbd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blooddonorbd.Models.UserInformation;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class UserListAdapters extends ArrayAdapter {
    private Context context;
    private ArrayList<UserInformation> userInformationArrayList;
    public UserListAdapters(Context context, ArrayList<UserInformation> userInformationArrayList) {
        super(context,R.layout.listview_shape, userInformationArrayList);
        this.context = context;
        this.userInformationArrayList = userInformationArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null){
            view = (View) LayoutInflater.from(context).inflate(R.layout.listview_shape,parent,false);
        }
        TextView nameTv = view.findViewById(R.id.textView12);
        TextView addName = view.findViewById(R.id.addressTv);
        ImageView imageView =  view.findViewById(R.id.activeImg);;

        nameTv.setText(userInformationArrayList.get(position).getfName());
        addName.setText(userInformationArrayList.get(position).getAddress());
        imageView.setImageResource(userInformationArrayList.get(position).getActiveStatus());
        return view;
    }
}
