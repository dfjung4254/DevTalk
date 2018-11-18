package com.devjk.devtalk.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {

    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String profileUrl;
    private Map<String, Boolean> friendUidList;
    private Map<String, Boolean> friendedUidList;
    private String status;

    public UserModel(){
        //constructor
    }

    public UserModel(String uid, String email, String password, String nickName, String phone, String profileUrl){
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.profileUrl = profileUrl;
        this.status = "";
        friendUidList = new HashMap<>();
        friendedUidList = new HashMap<>();
    }

    //methods
    //setter and getter
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getProfileUrl() {
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
    public Map<String, Boolean> getFriendUidList() {
        return friendUidList;
    }
    public void setFriendUidList(Map<String, Boolean> friendUidList) {
        this.friendUidList = friendUidList;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Map<String, Boolean> getFriendedUidList() {
        return friendedUidList;
    }
    public void setFriendedUidList(Map<String, Boolean> friendedUidList) {
        this.friendedUidList = friendedUidList;
    }
}
