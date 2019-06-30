package com.example.blooddonorbd.Models;

public class MessageInfo {
    String message,messageTime,sender,reciver,reciverTokenId,senderTokenId,senderName,isSeen;
    public MessageInfo(){}
    public MessageInfo(String message, String messageTime, String reciver,String reciverTokenId, String sender,String senderTokenId,String senderName,String isSeen) {
        this.message = message;
        this.messageTime = messageTime;
        this.sender = sender;
        this.reciver = reciver;
        this.isSeen = isSeen;
        this.reciverTokenId = reciverTokenId;
        this.senderTokenId = senderTokenId;
        this.senderName = senderName;
    }
    public MessageInfo(String message, String messageTime, String reciver, String sender,String isSeen) {
        this.message = message;
        this.messageTime = messageTime;
        this.sender = sender;
        this.reciver = reciver;
        this.isSeen = isSeen;
        this.reciverTokenId = reciverTokenId;
        this.senderTokenId = senderTokenId;
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

    public String getReciverTokenId() {
        return reciverTokenId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderTokenId() {
        return senderTokenId;
    }
}
