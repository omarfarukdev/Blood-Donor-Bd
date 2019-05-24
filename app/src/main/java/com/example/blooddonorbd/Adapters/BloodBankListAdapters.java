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

import com.example.blooddonorbd.Models.BloodBankInfo;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class BloodBankListAdapters extends ArrayAdapter<BloodBankInfo> {
    private ArrayList<BloodBankInfo> arrayList;
    private Context context;

    public BloodBankListAdapters(Context context, int resource,ArrayList<BloodBankInfo> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=convertView;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        v=layoutInflater.inflate(R.layout.search_result_listview_shape,null);
        TextView name=v.findViewById(R.id.name);
        TextView phoneno=v.findViewById(R.id.phonenumber);
        TextView address=v.findViewById(R.id.address);
        ImageButton call=v.findViewById(R.id.callimage);
        name.setText(arrayList.get(position).getName());
        phoneno.setText(arrayList.get(position).getPhonenumber());
        address.setText(arrayList.get(position).getAddress());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+arrayList.get(position).getPhonenumber()));
                context.startActivity(intent);
            }
        });

        return v;
    }
}
