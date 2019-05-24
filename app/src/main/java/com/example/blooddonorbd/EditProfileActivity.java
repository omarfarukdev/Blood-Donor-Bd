package com.example.blooddonorbd;

import android.app.DatePickerDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    EditText birthdate,phoneno,fullname,lastdonationdate;
    String name,birthday,currentnumber,gender,bloodgroup,donationdate,bloodGroupSp,genderSp;
    Spinner bloodgroupsp,gendersp;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //getSupportActionBar().hide();

        birthdate=findViewById(R.id.dateOfBirthEt);
        phoneno=findViewById(R.id.phonenumber);
        fullname=findViewById(R.id.fullNameEt);
        lastdonationdate=findViewById(R.id.lastDonationDateEt);
        bloodgroupsp=findViewById(R.id.bloodGroup);
        gendersp=findViewById(R.id.gender);

        ArrayAdapter<String> adapter_option=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bloodgroup));
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
        phoneno.setText(currentnumber);

         databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(currentnumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if (d.getKey().equals("Full Name")){
                        name=d.getValue().toString();
                        fullname.setText(name);
                    }
                    if (d.getKey().equals("Date of birth")){
                        birthday=d.getValue().toString();
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
                        lastdonationdate.setText(donationdate);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        lastdonationdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        lastdonationdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                };
                new DatePickerDialog(EditProfileActivity.this, date, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        birthdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                };
                new DatePickerDialog(EditProfileActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                        reference.child(d.getKey()).setValue(lastdonationdate.getText().toString());
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