package com.example.blooddonorbd.Models;

public class BloodBankInfo {
   private String name;
   private String phonenumber;
   private String address;

    public BloodBankInfo(String name, String phonenumber, String address) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }
}
