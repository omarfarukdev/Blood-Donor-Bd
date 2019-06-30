package com.example.blooddonorbd;

import android.content.Intent;
import android.net.Uri;
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
    String name,birthdate,reciverTokenId;
    int birtday,birthmonth,birthyear,currentday,currentmonth,currentyear,age;
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
        nameTv.setText(name);
        dateTv.setText("Member since march 2018");
        genderTv.setText(intent.getStringExtra("gender"));
        birthdate=intent.getStringExtra("dateOfBirth");
        reciverTokenId = intent.getStringExtra("tokenId");

        String[] birthday=birthdate.split("/");
        birtday=Integer.parseInt(birthday[0]);
        birthmonth=Integer.parseInt(birthday[1]);
        birthyear=Integer.parseInt(birthday[2]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());
        String[] currentdate=currentDateandTime.split("/");
        Log.d("Currentday",""+currentDateandTime);
        currentyear=Integer.parseInt(currentdate[0]);
        currentmonth=Integer.parseInt(currentdate[1]);
        currentday=Integer.parseInt(currentdate[2]);

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
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        startActivity(intent);
    }

    public void msgBt(View view) {
        Intent intent=new Intent(this,ChatActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("phoneNo",phnNo);
        intent.putExtra("tokenId",reciverTokenId);
        startActivity(intent);
    }
}
