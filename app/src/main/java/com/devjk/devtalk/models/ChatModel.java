package com.devjk.devtalk.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ChatModel {

    public static final int PRIVATEROOM = 0;
    public static final int GROUPROOM = 1;
    public static final int ERRROOM = -1;

    private String chatRoomTitle;
    private int chatRoomStatus;
    private Map<String, Boolean> chatParticipantsUid;
    private String lastMessage;

    public ChatModel(){
        //constructor
    }

    public ChatModel(String chatRoomTitle, int chatRoomStatus, ArrayList<String> participantsUid){
        //constructor
        //단체룸일때.;
        this.chatRoomTitle = chatRoomTitle;
        this.chatRoomStatus = chatRoomStatus;
        chatParticipantsUid = new HashMap<>();
        this.lastMessage = "";

        int size = participantsUid.size();
        for(int i = 0; i < size; i++){
            chatParticipantsUid.put(participantsUid.get(i), true);
        }
    }

    public String getChatRoomTitle() {
        return chatRoomTitle;
    }
    public void setChatRoomTitle(String chatRoomTitle) {
        this.chatRoomTitle = chatRoomTitle;
    }
    public int getChatRoomStatus() {
        return chatRoomStatus;
    }
    public void setChatRoomStatus(int chatRoomStatus) {
        this.chatRoomStatus = chatRoomStatus;
    }
    public Map<String, Boolean> getChatParticipantsUid() {
        return chatParticipantsUid;
    }
    public void setChatParticipantsUid(Map<String, Boolean> chatParticipantsUid) {
        this.chatParticipantsUid = chatParticipantsUid;
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
