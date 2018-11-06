package com.devjk.devtalk.controller;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class DatabaseController {

    private static DatabaseController instance;
    private FirebaseFirestore firebaseFirestore;

    public DatabaseController(){
        //constructor
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static DatabaseController getInstance() {
        if(instance == null){
            instance = new DatabaseController();
        }
        return instance;
    }

    public boolean searchNickName(String nickName){
        boolean ret = true;

        return ret;
    }

    public boolean addUserInfo(Map<String, Object> newUser){
        return firebaseFirestore.collection("Users")
                .add(newUser).isSuccessful();
    }

}
