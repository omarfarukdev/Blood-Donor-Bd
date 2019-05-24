package com.example.blooddonorbd.Models;

public class AmbulanceInfo {
    String address;
    String phonenumber;

    public AmbulanceInfo(String address, String phonenumber) {
        this.address = address;
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }


}
