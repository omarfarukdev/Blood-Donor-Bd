package com.example.blooddonorbd.Models;

public class UserInformation {
    private String fName,address,dateOfBirth,bldGroup,phnNo,gender;

    public UserInformation(String fName, String address, String dateOfBirth, String bldGroup, String phnNo, String gender) {
        this.fName = fName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bldGroup = bldGroup;
        this.phnNo = phnNo;
        this.gender = gender;
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
}
