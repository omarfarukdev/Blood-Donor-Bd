package com.example.blooddonorbd.Activity;

import android.app.Dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.blooddonorbd.R;
import com.hbb20.CountryCodePicker;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        //getSupportActionBar().hide();

        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumber);

        ccp.registerCarrierNumberEditText(phoneNumber);//attaching contry code with phone number

    }

    public void nextBtOnverifyAc(View view) {
        //alert dialog for phone number confirmation
       /* final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
        alertDialog.show();*/
       phoneNumConfirmtionDilog();
    }

    private void phoneNumConfirmtionDilog(){
        final Dialog phnNumConfirmtiondialog = new Dialog(VerifyPhoneNumberActivity.this);
        phnNumConfirmtiondialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        phnNumConfirmtiondialog.setContentView(R.layout.phn_num_confirmtion_dialog);

        Button editBt = (Button) phnNumConfirmtiondialog.findViewById(R.id.dilog_editBt);
        Button okBt = phnNumConfirmtiondialog.findViewById(R.id.dilog_okBt);
        ImageButton cancelIm = phnNumConfirmtiondialog.findViewById(R.id.cancelImageButton);
        TextView desTex = phnNumConfirmtiondialog.findViewById(R.id.desText_dialog);

        String text = "Your verification phone number is "+"<b>"+ccp.getFullNumberWithPlus()+"</b>"+".Is this OK, or would you  like to edit the number ?";
        desTex.setText(Html.fromHtml(text));

        cancelIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phnNumConfirmtiondialog.dismiss();
            }
        });
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phnNumConfirmtiondialog.dismiss();
                phoneNumber.setCursorVisible(true);
            }
        });

        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                phnNumConfirmtiondialog.dismiss();
                //finish();
            }
        });

        phnNumConfirmtiondialog.show();
    }

}