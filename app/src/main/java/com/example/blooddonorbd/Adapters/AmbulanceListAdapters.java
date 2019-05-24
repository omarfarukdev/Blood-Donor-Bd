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

import com.example.blooddonorbd.Models.AmbulanceInfo;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class AmbulanceListAdapters extends ArrayAdapter<AmbulanceInfo> {
    ArrayList<AmbulanceInfo> arrayList;
    Context context;

    public AmbulanceListAdapters(Context context, int resource, ArrayList<AmbulanceInfo> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.ambulance_list,null);
        TextView phnNo=view.findViewById(R.id.ambulancePhnNo);
        TextView address=view.findViewById(R.id.ambulanceaddress);
        ImageButton call=view.findViewById(R.id.ambulancecall);
        phnNo.setText(arrayList.get(position).getPhonenumber());
        address.setText(arrayList.get(position).getAddress());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+arrayList.get(position).getPhonenumber()));
                context.startActivity(intent);
            }
        });
        return view;
    }
}
