package com.example.blooddonorbd.Models;

public class HelpInfo {
    String helptype;
    int heplImage;

    public HelpInfo(String helptype, int heplImage) {
        this.helptype = helptype;
        this.heplImage = heplImage;
    }

    public String getHelptype() {
        return helptype;
    }

    public int getHeplImage() {
        return heplImage;
    }
}
