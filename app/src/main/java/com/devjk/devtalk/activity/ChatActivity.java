package com.devjk.devtalk.activity;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devjk.devtalk.Dialog.ProfileDialog;
import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.DatabaseController;
import com.devjk.devtalk.models.ChatModel;
import com.devjk.devtalk.models.MessageModel;
import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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

    private ChatRecyclerViewAdapter adapter;
    ArrayList<MessageModel> messageList;
    Map<String, UserModel> userParticipantsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageList = new ArrayList<>();
        userParticipantsMap = new HashMap<>();

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
                        //메세지 불러오기
                        adapter = new ChatRecyclerViewAdapter();
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        recyclerView.setAdapter(adapter);
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
            //그룹 방








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

    private class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final int MYMESSAGE = 0;
        private final int COUNTERMESSAGE = 1;

        public ChatRecyclerViewAdapter(){
            //constructor DataSetting

            //DB조회 채팅메세지 리스너 달기.
            DatabaseController.getInstance().getMessages(chatRoomUid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(MainActivity.MYTAG, "Listen failed.", e);
                        return;
                    }
                    messageList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        messageList.add(doc.toObject(MessageModel.class));
                    }
                    //유저정보 가져오기 , 개인방 or 단체방
                    DatabaseController.getInstance().getChatUserInformation(chatModel, chatRoomUid)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    //유저맵까지 갱신
                                    userParticipantsMap.clear();
                                    if(roomType == ChatModel.PRIVATEROOM){
                                        userParticipantsMap.put(AuthController.currentUser.getUid(), AuthController.currentUser);
                                    }
                                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                        UserModel tp = doc.toObject(UserModel.class);
                                        userParticipantsMap.put(tp.getUid(), tp);
                                        Log.d(MainActivity.MYTAG, "유저맵 갱신 : "+tp.getEmail());
                                    }
                                    adapter.notifyDataSetChanged();
                                    Log.d(MainActivity.MYTAG, "메세지갱신 : "+messageList.toString());
                                }
                            });
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            if(messageList.get(position).getSenderUserUid().equals(AuthController.currentUser.getUid())){
                //me
                return MYMESSAGE;
            }else{
                //counter
                return COUNTERMESSAGE;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == MYMESSAGE){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_chat_me, parent, false);
                return new MyViewHolder(view);
            }else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_chat_counter, parent, false);
                return new CounterViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if(holder instanceof MyViewHolder){
                //me
                ((MyViewHolder)holder).sendTime.setText(messageList.get(position).getTime().toDate().toString());
                ((MyViewHolder)holder).message.setText(messageList.get(position).getChats());
            }else{
                //counter;
                final UserModel thisUserModel = userParticipantsMap.get(messageList.get(position).getSenderUserUid());
                Glide.with(ChatActivity.this)
                        .load(thisUserModel.getProfileUrl())
                        .apply(new RequestOptions().circleCrop())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ((CounterViewHolder)holder).sendTime.setText(messageList.get(position).getTime().toDate().toString());
                                ((CounterViewHolder)holder).message.setText(messageList.get(position).getChats());
                                ((CounterViewHolder)holder).nickName.setText(thisUserModel.getNickName());
                                return false;
                            }
                        })
                        .into(((CounterViewHolder)holder).profile);
                ((CounterViewHolder)holder).profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ProfileDialog(ChatActivity.this, thisUserModel, ProfileDialog.BUTTONONLYCLOSE).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        //내부 뷰홀더 클래스 - 상대방
        class CounterViewHolder extends RecyclerView.ViewHolder{

            private TextView nickName;
            private ImageView profile;
            private TextView sendTime;
            private TextView readCount;
            private TextView message;

            public CounterViewHolder(View itemView) {
                super(itemView);
                nickName = (TextView) itemView.findViewById(R.id.ItemListChatCounter_TextView_nickName);
                profile = (ImageView) itemView.findViewById(R.id.ItemListChatCounter_ImageView_profile);
                sendTime = (TextView) itemView.findViewById(R.id.ItemListChatCounter_TextView_clock);
                readCount = (TextView) itemView.findViewById(R.id.ItemListChatCounter_TextView_readCount);
                message = (TextView) itemView.findViewById(R.id.ItemListChatCounter_TextView_message);
            }
        }
        //내부 뷰홀더 클래스 - 나
        class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView sendTime;
            private TextView readCount;
            private TextView message;

            public MyViewHolder(View itemView) {
                super(itemView);
                sendTime = (TextView) itemView.findViewById(R.id.ItemListChatMe_TextView_clock);
                readCount = (TextView) itemView.findViewById(R.id.ItemListChatMe_TextView_readCount);
                message = (TextView) itemView.findViewById(R.id.ItemListChatMe_TextView_message);
            }
        }


    }

}
