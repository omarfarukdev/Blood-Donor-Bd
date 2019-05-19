package com.example.blooddonorbd.Models;

public class MessageInfo {
    String message,messageTime,sender,reciver;

    public MessageInfo(String message, String messageTime, String reciver, String sender) {
        this.message = message;
        this.messageTime = messageTime;
        this.sender = sender;
        this.reciver = reciver;
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
}
