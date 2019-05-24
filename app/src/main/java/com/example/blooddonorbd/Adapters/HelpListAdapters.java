package com.example.blooddonorbd.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blooddonorbd.Models.HelpInfo;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class HelpListAdapters extends ArrayAdapter<HelpInfo> {
    ArrayList<HelpInfo> arrayList;
    Context context;
    public HelpListAdapters(Context context, int resource, ArrayList<HelpInfo> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v=convertView;
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.help_listview_shape,null);
        TextView textView=v.findViewById(R.id.helptext);
        ImageView imageView=v.findViewById(R.id.helpimage);
        textView.setText(arrayList.get(position).getHelptype());
        imageView.setImageResource(arrayList.get(position).getHeplImage());
        return v;
    }
}
