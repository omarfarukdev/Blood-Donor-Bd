package com.example.blooddonorbd.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.blooddonorbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    EditText birthdate,fullname,lastdonationdate;
    String name,birthday,currentnumber,gender,bloodgroup,donationdate,bloodGroupSp,genderSp,donatedate,lastdat,lastdondate,time;
    Spinner bloodgroupsp,gendersp;
    DatabaseReference databaseReference;
    String [] lastdate;
    String [] birdate;
    String [] lastdateofdonation;
    ArrayList<String> dondate=new ArrayList<>();
    final java.util.Calendar calendar = java.util.Calendar.getInstance();
    final java.util.Calendar calendar1 = java.util.Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener mDatasetListener,bDatasetListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        birthdate=findViewById(R.id.dateOfBirthEt);
        fullname=findViewById(R.id.fullNameEt);
        lastdonationdate=findViewById(R.id.lastDonationDateEt);
        bloodgroupsp=findViewById(R.id.bloodGroup);
        gendersp=findViewById(R.id.gender);

        ArrayAdapter<String> adapter_option=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroupOnEdit));
        adapter_option.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroupsp.setAdapter(adapter_option);
        bloodgroupsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupSp=(String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.gender));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gendersp.setAdapter(arrayAdapter);
        gendersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSp=(String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currentnumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

         databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(currentnumber);

         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot d:dataSnapshot.getChildren()){
                     if (d.getKey().equals("Full Name")){
                         name=d.getValue().toString();
                         fullname.setText(name);
                     }
                     if (d.getKey().equals("Date of birth")){
                         birthday=d.getValue().toString();
                         String [] date=birthday.split("/");
                         calendar1.set(Calendar.YEAR,Integer.parseInt(date[2]));
                         calendar1.set(Calendar.MONTH,(Integer.parseInt(date[1])-1));
                         calendar1.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[0]));
                         birthdate.setText(birthday);
                     }
                     if (d.getKey().equals("Gender")){
                         gender=d.getValue().toString();
                         Gender(gender);
                     }
                     if (d.getKey().equals("Blood Group")){
                         bloodgroup=d.getValue().toString();
                         BloodGroup(bloodgroup);

                     }
                     if(d.getKey().equals("Last donation date")){
                         donationdate=d.getValue().toString();
                         dondate.add(donationdate);
                         lastdondate=donationdate;
                         String [] date=donationdate.split(" ");
                         String [] date1=date[0].split("/");
                        // Log.d("oaaaa",""+lastdondate);
                         calendar.set(Calendar.YEAR,Integer.parseInt(date1[2]));
                         calendar.set(Calendar.MONTH,(Integer.parseInt(date1[0])-1));
                         calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date1[1]));
                         lastdonationdate.setText(date1[1]+"/"+date1[0]+"/"+date1[2]+" "+date[1]);
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
        final java.util.Calendar calendar2 = java.util.Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final SimpleDateFormat formatterd = new SimpleDateFormat("dd");
        final SimpleDateFormat formatterm = new SimpleDateFormat("MM");
        final Date date = new Date();
        final String d=formatter.format(date);
        Log.d("oaaaa",""+d);
        calendar2.set(Calendar.DAY_OF_MONTH,Integer.parseInt(formatterd.format(date)));
        calendar2.set(Calendar.MONTH,(Integer.parseInt(formatterm.format(date))-1));
        calendar2.set(Calendar.YEAR,Integer.parseInt(formatter.format(date)));
        lastdonationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(EditProfileActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDatasetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(calendar2.getTimeInMillis());
                dialog.show();
            }
        });
        mDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(calendar.getTime());
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                lastdat=(month+1) + "/"+dayOfMonth+"/"+year+" "+currentTime;
                String date=dayOfMonth+"/"+(month+1) + "/"+year;
                lastdonationdate.setText(date);
            }
        };

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year=calendar1.get(Calendar.YEAR);
                int month=calendar1.get(Calendar.MONTH);
                int day=calendar1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(EditProfileActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,bDatasetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(calendar2.getTimeInMillis());
                dialog.show();
            }
        });
        bDatasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String date=dayOfMonth+"/"+(month+1) + "/"+year;
                birthdate.setText(date);
            }
        };

    }

    private void BloodGroup(String bloodgroup) {
        if (bloodgroup.equals("A+")){
            bloodgroupsp.setSelection(0);
        }else if(bloodgroup.equals("A-")){
            bloodgroupsp.setSelection(1);
        } else if(bloodgroup.equals("B+")){
            bloodgroupsp.setSelection(2);
        }else if(bloodgroup.equals("B-")){
            bloodgroupsp.setSelection(3);
        }else if(bloodgroup.equals("O+")){
            bloodgroupsp.setSelection(4);
        }else if(bloodgroup.equals("O-")){
            bloodgroupsp.setSelection(5);
        }else if(bloodgroup.equals("AB+")){
            bloodgroupsp.setSelection(6);
        }else if(bloodgroup.equals("AB-")){
            bloodgroupsp.setSelection(7);
        }
    }

    private void Gender(String gender) {
        if (gender.equals("Male")){
            gendersp.setSelection(0);
        }
        else if (gender.equals("Female")){
            gendersp.setSelection(1);

        }

    }

    public void save(View view) {
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("User").child(currentnumber);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Log.d("Omar",""+d.getKey());
                    if (d.getKey().equals("Blood Group")){
                        reference.child(d.getKey()).setValue(bloodGroupSp);
                    }
                    if(d.getKey().equals("Full Name")){
                        reference.child(d.getKey()).setValue(fullname.getText().toString());
                    }
                    if (d.getKey().equals("Date of birth")){
                        reference.child(d.getKey()).setValue(birthdate.getText().toString());
                    }
                    if (d.getKey().equals("Gender")){
                        reference.child(d.getKey()).setValue(genderSp);
                    }
                    if (d.getKey().equals("Last donation date")){
                        reference.child(d.getKey()).setValue(lastdat);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        finish();
    }

    public void backBt(View view) {
        finish();
    }
}