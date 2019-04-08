package com.example.blooddonorbd.Models;

public class UserInformation {
    private String fullName,blodGroup,dateOfBirth,gender;

    public UserInformation(String fullName, String blodGroup, String dateOfBirth, String gender) {
        this.fullName = fullName;
        this.blodGroup = blodGroup;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBlodGroup() {
        return blodGroup;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
}
