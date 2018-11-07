package com.devjk.devtalk.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

    public Task<DocumentReference> addUserInfo(final AppCompatActivity parent, Map<String, Object> newUser){
        //다음과 같이 Task로 처리해버리면 바로 addUserInfo를 호출한 본래 함수에서 바로 Listener들을 호출할 수 있다.
        return firebaseFirestore.collection("Users")
                .add(newUser);
    }

}
