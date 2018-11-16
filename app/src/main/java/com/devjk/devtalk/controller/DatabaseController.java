package com.devjk.devtalk.controller;

import android.support.v7.app.AppCompatActivity;

import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    //신규유저 정보 DB저장
    public Task<Void> addUserInfo(final AppCompatActivity parent, UserModel newUser){
        //다음과 같이 Task로 처리해버리면 바로 addUserInfo를 호출한 본래 함수에서 바로 Listener들을 호출할 수 있다.
        return firebaseFirestore.collection("Users")
                .document(newUser.getUid()).set(newUser);
    }
    //로그인유저 정보 불러오기
    public Task<DocumentSnapshot> getUserInfoWithUid(String uid){
        return firebaseFirestore.collection("Users")
                .document(uid).get();
    }
    //친구리스트 추가업데이트
    public Task<Void> addFriendUid(String friendUid){
        AuthController.currentUser.getFriendUidList().add(friendUid);
        String myUid = AuthController.currentUser.getUid();
        return firebaseFirestore.collection("Users")
                .document(myUid).set(AuthController.currentUser);
    }
    //친구 찾기 검색 결과 정보 불러오기
    public Task<QuerySnapshot> getUserInfoQuery(String queryField, String query){
        return firebaseFirestore.collection("Users")
                .whereEqualTo(queryField, query)
                .get();
    }

}
