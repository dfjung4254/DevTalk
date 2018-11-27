package com.devjk.devtalk.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devjk.devtalk.R;
import com.devjk.devtalk.activity.ChatActivity;
import com.devjk.devtalk.activity.MainActivity;
import com.devjk.devtalk.activity.SplashActivity;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.DatabaseController;
import com.devjk.devtalk.models.ChatModel;
import com.devjk.devtalk.models.MessageModel;
import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChatListFragment extends Fragment {

    private FrameLayout mainlayout;
    private RecyclerView recyclerView;

    private ChatListRecyclerViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);

        mainlayout = (FrameLayout) view.findViewById(R.id.ChatListFragment_FrameLayout_mainView);
        mainlayout.setBackgroundColor(Color.parseColor(SplashActivity.THEME_COLOR));

        recyclerView = (RecyclerView) view.findViewById(R.id.ChatListFragment_RecyclerView_recyclerView);
        adapter = new ChatListRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    private class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<ChatModel> chatModels = new ArrayList<>();

        public ChatListRecyclerViewAdapter(){
            //constructor
            //DB검색, ChatModel 변경사항 감지.
            DatabaseController.getInstance().searchMyChatRoomList().addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.d(MainActivity.MYTAG, "Listen failed.", e);
                        return;
                    }
                    chatModels.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        chatModels.add(doc.toObject(ChatModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.itemlist_chatroom, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if(chatModels.get(position).getChatRoomStatus() == ChatModel.PRIVATEROOM){
                //개인방일때
                String counterUid = "";
                for(String uids : chatModels.get(position).getChatParticipantsUid().keySet()){
                    if(!uids.equals(AuthController.currentUser.getUid())){
                        //상대방 uid
                        counterUid = uids;
                    }
                }
                DatabaseController.getInstance().getUserInfoWithUid(counterUid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        final UserModel tp = snapshot.toObject(UserModel.class);
                        Glide.with(getContext()).load(tp.getProfileUrl()).apply(new RequestOptions().circleCrop())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        ((ChatViewHolder)holder).roomTitle.setText(tp.getNickName());
                                        ((ChatViewHolder)holder).lastMessage.setText(chatModels.get(position).getLastMessage());
                                        ((ChatViewHolder)holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getContext(), ChatActivity.class);
                                                ArrayList<String> tpUid = new ArrayList<>();
                                                tpUid.add(AuthController.currentUser.getUid());
                                                tpUid.add(tp.getUid());
                                                ArrayList<String> tpNickName = new ArrayList<>();
                                                tpNickName.add(AuthController.currentUser.getNickName());
                                                tpNickName.add(tp.getNickName());
                                                intent.putStringArrayListExtra("participants", tpUid);
                                                intent.putStringArrayListExtra("participantsNickName", tpNickName);
                                                intent.putExtra("roomType", ChatModel.PRIVATEROOM);
                                                getContext().startActivity(intent);
                                            }
                                        });
                                        return false;
                                    }
                                }).into(((ChatViewHolder)holder).profile);
                    }
                });

            }else{
                //단체방일때
                Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/devtalk-24410.appspot.com/o/Users%2FProfiles%2Ficon_users.png?alt=media&token=b8da457d-81c3-4c4b-aaf8-4c6d223784c7")
                        .apply(new RequestOptions().circleCrop())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ((ChatViewHolder)holder).roomTitle.setText(chatModels.get(position).getChatRoomTitle());
                                ((ChatViewHolder)holder).lastMessage.setText(chatModels.get(position).getLastMessage());
                                ((ChatViewHolder)holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getContext(), ChatActivity.class);
                                        ArrayList<String> tpUid = new ArrayList<>();
                                        tpUid.add(AuthController.currentUser.getUid());
                                        for(String key : chatModels.get(position).getChatParticipantsUid().keySet()){
                                            tpUid.add(key);
                                        }
                                        ArrayList<String> tpNickName = new ArrayList<>();
                                        tpNickName.add(AuthController.currentUser.getNickName());
                                        tpNickName.add(tp.getNickName());
                                        intent.putStringArrayListExtra("participants", tpUid);
                                        intent.putStringArrayListExtra("participantsNickName", tpNickName);
                                        intent.putExtra("roomType", ChatModel.PRIVATEROOM);
                                        getContext().startActivity(intent);
                                    }
                                });
                                return false;
                            }
                        }).into(((ChatViewHolder)holder).profile);

            }
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        class ChatViewHolder extends RecyclerView.ViewHolder{

            private RelativeLayout relativeLayout;
            private ImageView profile;
            private TextView roomTitle;
            private TextView lastMessage;
            private TextView unReadCount;

            public ChatViewHolder(View itemView) {
                super(itemView);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.ItemListChatRoom_RelativeLayout_layout);
                profile = (ImageView) itemView.findViewById(R.id.ItemListChatRoom_ImageView_profile);
                roomTitle = (TextView) itemView.findViewById(R.id.ItemListChatRoom_TextView_chatRoomTitle);
                lastMessage = (TextView) itemView.findViewById(R.id.ItemListChatRoom_TextView_lastMessage);
                unReadCount = (TextView) itemView.findViewById(R.id.ItemListChatRoom_TextView_unReadCount);
            }
        }

    }

}
