package com.example.blooddonorbd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ResultDetails extends AppCompatActivity {
    TextView nameTv,dateTv,genderTv,ageTv,phoneNo;
    String phnNo;
    String name,birthdate,reciverTokenId,joiningDate;
    int birtday,birthmonth,birthyear,currentday,currentmonth,currentyear,age;
    static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private android.app.AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        //getSupportActionBar().hide();

        nameTv = findViewById(R.id.nameTv);
        dateTv = findViewById(R.id.dateTv);
        genderTv = findViewById(R.id.genderTv);
        ageTv = findViewById(R.id.ageTv);
        phoneNo = findViewById(R.id.textView10);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        joiningDate = intent.getStringExtra("joiningDate");
        nameTv.setText(name);
        birthdate=intent.getStringExtra("dateOfBirth");
        reciverTokenId = intent.getStringExtra("tokenId");

        String d=null,m=null,y=null;
        String[] joiningDateSp = joiningDate.split("/");
        if (joiningDateSp.length>0){
            try {
                d = joiningDateSp[0];
            }catch (Exception e){}
            try {
                m = joiningDateSp[1];
            }catch (Exception e){}
            try {
                y = joiningDateSp[2];
            }catch (Exception e){}
        }
        dateTv.setText("Member since "+m+" "+y);
        genderTv.setText(intent.getStringExtra("gender"));

        String[] birthday=birthdate.split("/");
        try{
            birtday=Integer.parseInt(birthday[0]);
        }catch (Exception e){}
        try{
            birthmonth=Integer.parseInt(birthday[1]);
        }catch (Exception e){}

        try{
            birthyear=Integer.parseInt(birthday[2]);
        }catch (Exception e){}


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());
        String[] currentdate=currentDateandTime.split("/");
        try{
            currentyear=Integer.parseInt(currentdate[0]);
        }catch (Exception e){}
        try{
            currentmonth=Integer.parseInt(currentdate[1]);
        }catch (Exception e){}
        try{
            currentday=Integer.parseInt(currentdate[2]);
        }catch (Exception e){}

        age=currentyear-birthyear;
        if(birthmonth>currentmonth){
            age--;
        }
        else if(birthmonth==currentmonth){
           if (birtday>currentday){
               age--;
           }
        }
        ageTv.setText(String.valueOf(age));
        phnNo = intent.getStringExtra("phoneNo");
        phoneNo.setText(phnNo);
    }

    public void backBt(View view) {
        finish();
    }

    public void callBt(View view) {
        checkForPhonePermissionJustCall();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                checkForPhonePermissionJustCall();
            }
        }
        else Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
    }

    private void checkForPhonePermissionJustCall() {
        builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Do you want to call on "+phnNo+" ?");

        final String phone = phnNo;
        if (phone.length() > 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Permission not yet granted. Use requestPermissions().
                // Log.d(Tag, getString(R.string.permission_not_grante));
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        //Intent intentChooser = Intent.createChooser(callIntent,"Choose your application");
                        startActivity(callIntent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.show();
                    }
                });
                builder.show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter number", Toast.LENGTH_SHORT).show();
        }
    }

    public void msgBt(View view) {
        Intent intent=new Intent(this,ChatActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("phoneNo",phnNo);
        intent.putExtra("tokenId",reciverTokenId);
        startActivity(intent);
    }
}
