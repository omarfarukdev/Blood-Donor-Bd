package com.example.blooddonorbd.Models;

public class MessageHistoryInfo {
    private String phoneNum,name,isSeen,message,chatKey;

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getName() {
        return name;
    }

    public MessageHistoryInfo(String phoneNum, String name,String isSeen,String message,String chatKey) {
        this.phoneNum = phoneNum;
        this.name = name;
        this.isSeen = isSeen;
        this.message = message;
        this.chatKey = chatKey;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public String getMessage() {
        return message;
    }

    public String getChatKey() {
        return chatKey;
    }
}
