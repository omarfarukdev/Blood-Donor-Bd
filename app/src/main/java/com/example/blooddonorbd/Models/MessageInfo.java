package com.example.blooddonorbd.Models;

public class MessageInfo {
    String message,messageTime,sender,reciver,isSeen;
    public MessageInfo(){}
    public MessageInfo(String message, String messageTime, String reciver, String sender,String isSeen) {
        this.message = message;
        this.messageTime = messageTime;
        this.sender = sender;
        this.reciver = reciver;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public String getIsSeen() {
        return isSeen;
    }
}
