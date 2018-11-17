package com.devjk.devtalk.activity;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devjk.devtalk.Dialog.LoadingDialog;
import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.DatabaseController;
import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    private TextView txt_searchGuide;
    private EditText editText_search;
    private Button btn_search;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayout emptyLayout;

    private String sqlQuery;
    private ArrayList<UserModel> searchUsers;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        loadingDialog = new LoadingDialog(this);
        searchUsers = new ArrayList<>();

        txt_searchGuide = (TextView) findViewById(R.id.AddFriendActivity_TextView_searchGuideText);
        editText_search = (EditText) findViewById(R.id.AddFriendActivity_EditText_txtSearch);
        btn_search = (Button) findViewById(R.id.AddFriendActivity_Button_btnSearch);
        recyclerView = (RecyclerView) findViewById(R.id.AddFriendActivity_RecyclerView_recyclerView);
        emptyLayout = (LinearLayout) findViewById(R.id.AddFriendActivity_LinearLayout_viewEmpty);
        emptyLayout.setVisibility(View.GONE);

        String type = getIntent().getStringExtra("type");
        setSearchTypeActivity(type);

        //검색 RecyclerView 구현.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendRecyclerAdapter(searchUsers);
        recyclerView.setAdapter(adapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //디비 검색한 후 검색 결과를 recyclerview에 띄워준다.
                String query = editText_search.getText().toString().trim();
                if(query.equals("")){
                    //아무것도 입력하지 않았을때.
                    return;
                }
                loadingDialog.show();
                DatabaseController.getInstance().getUserInfoQuery(sqlQuery, query).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //DB검색 성공.
                            searchUsers.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                UserModel tp = document.toObject(UserModel.class);
                                Log.d(MainActivity.MYTAG, "DB검색 -> "+tp.getNickName());
                                searchUsers.add(tp);
                            }
                            adapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }else{
                            //검색 실패
                            loadingDialog.dismiss();
                            Toast.makeText(AddFriendActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void setSearchTypeActivity(String type){
        switch (type){
            case "email":
                txt_searchGuide.setText("추가할 친구의 이메일을 검색하세요");
                editText_search.setHint("이메일을 입력해주세요");
                sqlQuery = "email";
                break;
            case "phone":
                txt_searchGuide.setText("추가할 친구의 전화번호를 검색하세요");
                editText_search.setHint(" - 없이 입력해주세요");
                sqlQuery = "phone";
                break;
            case "nickName":
                txt_searchGuide.setText("추가할 친구의 닉네임을 검색하세요");
                editText_search.setHint("닉네임을 입력해주세요");
                sqlQuery = "nickName";
                break;
            default:
                break;
        }
    }

    //RecyclerView Adapter 내부 클래스 구현
    private class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ItemViewHolder>{

        ArrayList<UserModel> mItems;

        public FriendRecyclerAdapter(ArrayList<UserModel> items){
            //constructor
            mItems = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_add_friend, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            final UserModel friend = mItems.get(position);

            Glide.with(AddFriendActivity.this)
                    .load(friend.getProfileUrl())
                    .apply(new RequestOptions().circleCrop())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d(MainActivity.MYTAG, "이미지 로드 실패 : "+friend.getEmail());
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //이미지 로딩이 완료되고나서 place홀더에 넣는다.
                            holder.txt_nickName.setText(friend.getNickName());
                            holder.txt_email.setText(friend.getEmail());
                            boolean isFriend = false;
                            for(String friendUid : AuthController.currentUser.getFriendUidList()){
                                //이 유저가 친구로 등록된 유저인지 아닌지판단
                                if(friendUid.equals(friend.getUid())){
                                    isFriend = true;
                                    break;
                                }
                            }
                            if(friend.getUid().equals(AuthController.currentUser.getUid()) || isFriend){
                                //이미 등록된 친구이거나 나 자신일때 버튼 설정
                                holder.btn_add.setBackgroundResource(R.drawable.icon_done);
                                holder.btn_add.setClickable(false);
                            }else{
                                holder.btn_add.setBackgroundResource(R.drawable.icon_add);
                                holder.btn_add.setClickable(true);
                            }
                            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseController.getInstance().addFriendUid(friend.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //친구 추가 성공 시 상대방 friendedUidList에 유저 Uid등록 버튼 이미지를 바꾸고 버튼을 비활성화 시킨다. 토스트도 띄움
                                            DatabaseController.getInstance().addFriendedUid(friend.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    holder.btn_add.setBackgroundResource(R.drawable.icon_done);
                                                    holder.btn_add.setClickable(false);
                                                    Toast.makeText(AddFriendActivity.this, "친구를 추가하였습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //친구 추가 실패.
                                                    Toast.makeText(AddFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //친구 추가 실패.
                                            Toast.makeText(AddFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            return false;
                        }
                    })
                    .into(holder.imgv_profile);
        }

        @Override
        public int getItemCount() {
            if(mItems.size() == 0){
                emptyLayout.setVisibility(View.VISIBLE);
            }else{
                emptyLayout.setVisibility(View.GONE);
            }
            return mItems.size();
        }

        //뷰홀더 클래스
        class ItemViewHolder extends RecyclerView.ViewHolder{

            private ImageView imgv_profile;
            private TextView txt_nickName;
            private TextView txt_email;
            private Button btn_add;

            public ItemViewHolder(View itemView) {
                super(itemView);
                imgv_profile = (ImageView) itemView.findViewById(R.id.ItemListAddFriend_ImageView_profile);
                txt_nickName = (TextView) itemView.findViewById(R.id.ItemListAddFriend_TextView_nickName);
                txt_email = (TextView) itemView.findViewById(R.id.ItemListAddFriend_TextView_email);
                btn_add = (Button) itemView.findViewById(R.id.ItemListAddFriend_Button_addFriend);
            }
        }

    }

}
