package com.devjk.devtalk.controller;

import android.support.v7.app.AppCompatActivity;

import com.devjk.devtalk.models.ChatModel;
import com.devjk.devtalk.models.MessageModel;
import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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
    //로그인유저 정보 갱신
    public DocumentReference updateUserInfoListen(String uid){
        return FirebaseFirestore.getInstance().collection("Users")
                .document(uid);
    }
    //친구리스트 추가업데이트
    public Task<Void> addFriendUid(String friendUid){
        AuthController.currentUser.getFriendUidList().put(friendUid, true);
        String myUid = AuthController.currentUser.getUid();
        return firebaseFirestore.collection("Users")
                .document(myUid).set(AuthController.currentUser);
    }
    //친구당한 리스트 추가업데이트
    public Task<Void> addFriendedUid(String friendUid){
        return firebaseFirestore.collection("Users")
                .document(friendUid).update("friendedUidList."+AuthController.getInstance().getCurrentUser().getUid(), true);
    }
    //유저 검색 결과 정보 불러오기
    public Task<QuerySnapshot> getUserInfoQuery(String queryField, String query){
        return firebaseFirestore.collection("Users")
                .whereEqualTo(queryField, query)
                .get();
    }
    //내 친구 정보 모두 불러오기
    public Task<QuerySnapshot> getMyFriendsQuery(String queryField){
        return firebaseFirestore.collection("Users")
                .whereEqualTo(queryField+"."+AuthController.getInstance().getCurrentUser().getUid(), true)
                .get();
    }
    //내 친구 정보 모두 불러오기 변경 리스너
    public Query getMyFriendsQueryListen(String queryField){
        return firebaseFirestore.collection("Users")
                .whereEqualTo(queryField+"."+AuthController.getInstance().getCurrentUser().getUid(), true);
    }
    //대화방 생성.
    public Task<DocumentReference> makeNewChatRoom(final ChatModel chatModel){
        return FirebaseFirestore.getInstance().collection("ChatRooms")
                .add(chatModel);
    }
    //대화방 생성시 유저정보갱신
    public Task<Void> updateUserRoomInfo(ChatModel chatModel, DocumentReference documentReference){
        String tpRoomUid = documentReference.getId();
        Map<String, Boolean> participants = chatModel.getChatParticipantsUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        String query;
        if(chatModel.getChatRoomStatus() == ChatModel.PRIVATEROOM){
            //개인룸일때.
            query = "privateChatRoomList";
            String myUid = AuthController.currentUser.getUid();
            String counterUid = "";
            for(String uids : participants.keySet()){
                if(!uids.equals(AuthController.currentUser.getUid())){
                    //uids == 상대방
                    counterUid = uids;
                }
            }
            DocumentReference ref = db.collection("Users")
                    .document(myUid);
            batch.update(ref, query+"."+counterUid, tpRoomUid);
            ref = db.collection("Users")
                    .document(counterUid);
            batch.update(ref, query+"."+myUid, tpRoomUid);
        }else{
            //단체룸일때.
            query = "groupChatRoomList";
            for(String uids : participants.keySet()){
                DocumentReference ref = db.collection("Users")
                        .document(uids);
                batch.update(ref, query+"."+tpRoomUid, true);
            }
        }
        return batch.commit();
    }
    //RoomUid로 대화방 모델 가져옴.
    public Task<DocumentSnapshot> getChatRoomModel(String roomUid){
        return FirebaseFirestore.getInstance().collection("ChatRooms")
                .document(roomUid).get();
    }
    //메세지 보내기
    public Task<Void> sendMessages(MessageModel message, String chatRoomUid){
        return FirebaseFirestore.getInstance().collection("ChatRooms")
                .document(chatRoomUid)
                .collection("Messages")
                .document("time : "+message.getTime().getSeconds())
                .set(message);
    }

}
