package com.example.blooddonorbd.Models;

public class BloodOrganizationInfo {
    String name;
    String phoneNumber;

    public BloodOrganizationInfo(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
