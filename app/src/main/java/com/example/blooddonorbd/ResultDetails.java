package com.example.blooddonorbd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultDetails extends AppCompatActivity {
    TextView nameTv,dateTv,genderTv,ageTv,phoneNo;
    String phnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        getSupportActionBar().hide();

        nameTv = findViewById(R.id.nameTv);
        dateTv = findViewById(R.id.dateTv);
        genderTv = findViewById(R.id.genderTv);
        ageTv = findViewById(R.id.ageTv);
        phoneNo = findViewById(R.id.textView10);

        Intent intent = getIntent();
        nameTv.setText(intent.getStringExtra("name"));
        dateTv.setText("Member since march 2018");
        genderTv.setText(intent.getStringExtra("gender"));
        ageTv.setText(intent.getStringExtra("dateOfBirth"));

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
        Intent intent=new Intent(this,MessagesActivity.class);
        startActivity(intent);
    }
}
