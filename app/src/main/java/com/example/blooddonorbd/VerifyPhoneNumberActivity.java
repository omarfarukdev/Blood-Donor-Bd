package com.example.blooddonorbd;

import android.arch.core.executor.TaskExecutor;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        getSupportActionBar().hide();

        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumber);

        ccp.registerCarrierNumberEditText(phoneNumber);//attaching contry code with phone number

    }

    public void nextBtOnverifyAc(View view) {
        //alert dialog for phone number confirmation
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Verification phone number is "+ccp.getFullNumberWithPlus()+". Do you want to edit the number ?");
        alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                phoneNumber.setCursorVisible(true);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumberWithPlus = ccp.getFullNumberWithPlus();

                if(phoneNumberWithPlus.isEmpty() || phoneNumberWithPlus.length() < 10){
                    phoneNumber.setError("Enter a valid mobile");
                    phoneNumber.requestFocus();
                    return;
                }
                Intent intent = new Intent(VerifyPhoneNumberActivity.this, PhoneNumberConfirmationActivity.class);
                //intent.putExtra()
                finish();
                intent.putExtra("phoneNumber",ccp.getFullNumberWithPlus());
                startActivity(intent);
            }
        });
        alertDialog.show();
    }

}