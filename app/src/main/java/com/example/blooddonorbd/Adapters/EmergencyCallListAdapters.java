package com.example.blooddonorbd.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class EmergencyCallListAdapters extends ArrayAdapter<String> {
    ArrayList<String> arrayList;
    Context context;
    public EmergencyCallListAdapters(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.arrayList=objects;
        this.context=context;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        View v=convertView;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        v=layoutInflater.inflate(R.layout.emergency_call_list,null);
        TextView textView=v.findViewById(R.id.number);
        ImageButton callbutton=v.findViewById(R.id.callimage);
        textView.setText(arrayList.get(position));
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+arrayList.get(position)));
                context.startActivity(intent);
            }
        });
        return v;
    }

}
