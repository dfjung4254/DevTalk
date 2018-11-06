package com.devjk.devtalk.models;

import java.util.HashMap;
import java.util.Map;

public class UserModel {

    Map<String, Object> mapInstance;

    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String profileUrl;

    public UserModel(){
        //constructor
    }

    public UserModel(String uid, String email, String password, String nickName, String phone){
        mapInstance = new HashMap<>();
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        mapInstance.put("uid", uid);
        mapInstance.put("email", email);
        mapInstance.put("password", password);
        mapInstance.put("nickName", nickName);
        mapInstance.put("phone", phone);
    }

    //methods
    public Map<String, Object> getMapModel(){
        return mapInstance;
    }

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

}
