package com.example.blooddonorbd.Models;

import android.graphics.drawable.Drawable;

public class UserInformation {
    private String fName,address,dateOfBirth,bldGroup,phnNo,gender,token,joiningDate;
    private int activeStatus;

    public UserInformation(String fName, String address,String joiningDate, String dateOfBirth, String bldGroup, String phnNo, String gender, String token, int activeStatus) {
        this.fName = fName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bldGroup = bldGroup;
        this.phnNo = phnNo;
        this.gender = gender;
        this.token = token;
        this.activeStatus = activeStatus;
        this.joiningDate = joiningDate;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getfName() {
        return fName;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBldGroup() {
        return bldGroup;
    }

    public String getPhnNo() {
        return phnNo;
    }

    public String getGender() {
        return gender;
    }

    public String getToken() {
        return token;
    }

    public int getActiveStatus() {
        return activeStatus;
    }
}
