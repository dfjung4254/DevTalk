package com.devjk.devtalk.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class MessageModel {

    private Timestamp time;
    private String senderUserUid;
    private String chats;

    public MessageModel(){
        //constructor
    }

    public MessageModel(String senderUserUid, String chats){
        this.senderUserUid = senderUserUid;
        this.chats = chats;
        time = new Timestamp(new Date());
    }

    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public String getSenderUserUid() {
        return senderUserUid;
    }
    public void setSenderUserUid(String senderUserUid) {
        this.senderUserUid = senderUserUid;
    }
    public String getChats() {
        return chats;
    }
    public void setChats(String chats) {
        this.chats = chats;
    }
}
