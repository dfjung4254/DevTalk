package com.devjk.devtalk.activity;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.DatabaseController;
import com.devjk.devtalk.models.ChatModel;
import com.devjk.devtalk.models.MessageModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Button btn_menu;
    private RecyclerView recyclerView;
    private RelativeLayout chatBar;
    private EditText editText_chat;
    private ImageButton btn_send;
    private Button btn_backPress;
    private TextView txt_chatTitle;

    private int roomType;
    private ArrayList<String> participants;
    private ArrayList<String> participantsNickName;
    private ChatModel chatModel = new ChatModel();
    private String chatRoomUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_menu = (Button) findViewById(R.id.ChatActivity_Button_btnMenu);
        recyclerView = (RecyclerView) findViewById(R.id.ChatActivity_RecyclerView_recyclerView);
        chatBar = (RelativeLayout) findViewById(R.id.ChatActivity_RelativtLayout_chatBar);
        editText_chat = (EditText) findViewById(R.id.ChatActivity_EditText_sendMessage);
        btn_send = (ImageButton) findViewById(R.id.ChatActivity_Button_btnSend);
        btn_backPress = (Button) findViewById(R.id.ChatActivity_Button_btnBackPress);
        txt_chatTitle = (TextView) findViewById(R.id.ChatActivity_TextView_chatTitle);

        //방 정보 가져옴.
        roomType = getIntent().getIntExtra("roomType", ChatModel.ERRROOM);
        participants = getIntent().getStringArrayListExtra("participants");
        participantsNickName = getIntent().getStringArrayListExtra("participantsNickName");

        //현재 들어온 방의 모델 DB검색 후 불러오기.
        //불러온 정보를 바탕으로 UI세팅
        setChatRoomInformation();


    }

    public void setChatRoomInformation() {

        if(roomType == ChatModel.PRIVATEROOM){
            //개인룸일 경우.
            Map<String, String> tpRoomMap = AuthController.currentUser.getPrivateChatRoomList();
            boolean findRoom = false;
            for(String counterUid : tpRoomMap.keySet()){
                if(counterUid.equals(participants.get(1))){
                    findRoom = true;
                    chatRoomUid = tpRoomMap.get(counterUid);
                    break;
                }
            }
            if(findRoom){
                //기존 방이 존재함.
                //DB에서 방을 가져옴.
                DatabaseController.getInstance().getChatRoomModel(chatRoomUid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        chatModel = documentSnapshot.toObject(ChatModel.class);
                        //불러온 정보를 바탕으로 UI세팅
                        setChatRoomUI();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(MainActivity.MYTAG, "ChatRoom 정보가져오기 실패 : "+e.getMessage());
                    }
                });

            }else{
                //기존 방 없음
                //create new privateRoom
                final ChatModel tpModel = new ChatModel("", ChatModel.PRIVATEROOM, participants);
                DatabaseController.getInstance().makeNewChatRoom(tpModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        DatabaseController.getInstance().updateUserRoomInfo(tpModel, documentReference).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //방 생성
                                AuthController.getInstance().setCurrentUser().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        Log.d(MainActivity.MYTAG, "유저정보 갱신, 다시 setChatRoomInformation 루프돔");
                                        setChatRoomInformation();
                                        return;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }else if(roomType == ChatModel.GROUPROOM){

        }else{
            Log.d(MainActivity.MYTAG, "roomType 에러 : "+roomType);
        }

    }

    public void setChatRoomUI(){
        btn_backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //DrawerLayout 만들어야함.
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼모양 로딩중으로 바꿈. unable

                MessageModel message = new MessageModel(AuthController.currentUser.getUid(), editText_chat.getText().toString());
                DatabaseController.getInstance().sendMessages(message, chatRoomUid).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //recyclerview adapter의 notify 해주고
                        //버튼모양 원상태로 바꿈. enable
                        editText_chat.setText("");
                    }
                });

            }
        });
        if(roomType == ChatModel.PRIVATEROOM){
            txt_chatTitle.setText(participantsNickName.get(1));
        }else{
            txt_chatTitle.setText(chatModel.getChatRoomTitle());
        }

    }

}
