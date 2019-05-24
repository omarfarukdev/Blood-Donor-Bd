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
import com.example.blooddonorbd.Models.BloodOrganizationInfo;
import com.example.blooddonorbd.R;

import java.util.ArrayList;

public class BloodOrganizationListAdaptrs extends ArrayAdapter<BloodOrganizationInfo> {
    ArrayList<BloodOrganizationInfo> list;
    Context context;

    public BloodOrganizationListAdaptrs(Context context, int resource,ArrayList<BloodOrganizationInfo> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=convertView;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        v=layoutInflater.inflate(R.layout.blood_organization_list,null);
        TextView name=v.findViewById(R.id.name);
        TextView phoneno=v.findViewById(R.id.phonenumber);
        TextView address=v.findViewById(R.id.address);
        ImageButton call=v.findViewById(R.id.callimage);
        name.setText(list.get(position).getName());
        phoneno.setText(list.get(position).getPhoneNumber());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+list.get(position).getPhoneNumber()));
                context.startActivity(intent);
            }
        });

        return v;
    }
}
