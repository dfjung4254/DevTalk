package com.devjk.devtalk.controller;

import android.net.Uri;

public class ValidateCheck {

    private static ValidateCheck instance;

    public static final int VALIDATE_PASS = 0;
    public static final int VALIDATE_EMAIL_FORM = 1;
    public static final int VALIDATE_PWD_CHARLESS = 2;
    public static final int VALIDATE_PWD_NONCONFIRM = 3;
    public static final int VALIDATE_NICKNAME_EXISTS = 4;
    public static final int VALIDATE_PHONE_FORM = 5;
    public static final int VALIDATE_PHONE_BAR = 6;
    public static final int VALIDATE_PROFILE_NON = 7;

    public ValidateCheck(){
        //constructor
    }

    public static ValidateCheck getInstance() {
        if(instance == null){
            instance = new ValidateCheck();
        }
        return instance;
    }

    public int checkEmail(String email){
        int find1 = 0;
        int find2 = 0;
        for(int i = 0; i < email.length(); i++){
            if(email.charAt(i) == '@'){
                if(find1 == 0){
                    find1 = i;
                }else{
                    return VALIDATE_EMAIL_FORM;
                }
            }
            if(email.charAt(i) == '.'){
                if(find2 == 0 && find1 < i){
                    find2 = i;
                }else{
                    return VALIDATE_EMAIL_FORM;
                }
            }
        }
        if(find1 == 0 || find2 == 0){
            return VALIDATE_EMAIL_FORM;
        }
        return VALIDATE_PASS;
    }
    public int checkPassword(String password){
        return (password.length() >= 6) ? VALIDATE_PASS:VALIDATE_PWD_CHARLESS;
    }
    public int checkPasswordConfirm(String password, String cPassword){
        return (password.equals(cPassword)) ? VALIDATE_PASS:VALIDATE_PWD_NONCONFIRM;
    }
    public int checkNickName(String nickName){
        return DatabaseController.getInstance().searchNickName(nickName) ? VALIDATE_PASS:VALIDATE_NICKNAME_EXISTS;
    }
    public int checkPhone(String phone){
        if(phone.length() != 11){
            return VALIDATE_PHONE_FORM;
        }
        for(int i = 0; i < phone.length(); i++){
            if(phone.charAt(i) == '-'){
                return VALIDATE_PHONE_BAR;
            }
        }
        return VALIDATE_PASS;
    }
    public int checkProfile(Uri profileUri) {
        return (profileUri != null) ? VALIDATE_PASS:VALIDATE_PROFILE_NON;
    }

    public int checkAccount(String email, String password, String cPassword, String nickName, String phone, Uri profileUri){
        int ret = VALIDATE_PASS;
        ret = checkEmail(email);
        if(ret != VALIDATE_PASS) return ret;
        ret = checkPassword(password);
        if(ret != VALIDATE_PASS) return ret;
        ret = checkPasswordConfirm(password, cPassword);
        if(ret != VALIDATE_PASS) return ret;
        ret = checkNickName(nickName);
        if(ret != VALIDATE_PASS) return ret;
        ret = checkPhone(phone);
        if(ret != VALIDATE_PASS) return ret;
        ret = checkProfile(profileUri);
        if(ret != VALIDATE_PASS) return ret;
        return ret;
    }

}
