package com.devjk.devtalk.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devjk.devtalk.R;
import com.devjk.devtalk.activity.AddFriendActivity;
import com.devjk.devtalk.activity.MainActivity;
import com.devjk.devtalk.activity.SplashActivity;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.DatabaseController;
import com.devjk.devtalk.models.UserModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private FrameLayout mainlayout;
    public FrameLayout searchLayout;
    private EditText editText_search;
    private Button btn_search;
    private RecyclerView recyclerView;
    private FloatingActionButton btn_email;
    private FloatingActionButton btn_phone;
    private FloatingActionButton btn_nickname;

    private FriendListRecyclerViewAdapter adapter;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mainlayout = (FrameLayout) view.findViewById(R.id.UsersFragment_FrameLayout_mainView);
        mainlayout.setBackgroundColor(Color.parseColor(SplashActivity.THEME_COLOR));
        searchLayout = (FrameLayout) view.findViewById(R.id.UsersFragment_FrameLayout_searchLayout);
        editText_search = (EditText) view.findViewById(R.id.UsersFragment_EditText_txtSearch);
        btn_search = (Button) view.findViewById(R.id.UsersFragment_Button_btnSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.UsersFragment_RecyclerView_recyclerView);
        btn_email = (FloatingActionButton) view.findViewById(R.id.UsersFragment_FloatingActionButton_findEmail);
        btn_phone = (FloatingActionButton) view.findViewById(R.id.UsersFragment_FloatingActionButton_findPhone);
        btn_nickname = (FloatingActionButton) view.findViewById(R.id.UsersFragment_FloatingActionButton_findNickName);
        addFabClickListener();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FriendListRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void addFabClickListener() {
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                intent.putExtra("type", "email");
                startActivity(intent);
            }
        });
        btn_phone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                intent.putExtra("type", "phone");
                startActivity(intent);
            }
        });
        btn_nickname.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                intent.putExtra("type", "nickName");
                startActivity(intent);
            }
        });
    }

    private class FriendListRecyclerViewAdapter extends RecyclerView.Adapter<FriendListRecyclerViewAdapter.FriendListViewHolder>{

        private ArrayList<UserModel> mItems;

        public FriendListRecyclerViewAdapter(){
            //constructor
            //데이터 세팅
            this.mItems = new ArrayList<>();

        }

        @Override
        public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.itemlist_list_friend, parent, false);
            return new FriendListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FriendListViewHolder holder, int position) {
            final UserModel friend = mItems.get(position);

            Glide.with(UsersFragment.this)
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
                            holder.txt_status.setText(friend.getStatus());
                            return false;
                        }
                    })
                    .into(holder.imgv_profile);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class FriendListViewHolder extends RecyclerView.ViewHolder{

            private TextView txt_nickName;
            private ImageView imgv_profile;
            private TextView txt_status;

            public FriendListViewHolder(View itemView) {
                super(itemView);
                txt_nickName = (TextView) itemView.findViewById(R.id.ItemListListFriend_TextView_nickName);
                imgv_profile = (ImageView) itemView.findViewById(R.id.ItemListListFriend_ImageView_profile);
                txt_status = (TextView) itemView.findViewById(R.id.ItemListListFriend_TextView_status);
            }
        }
    }

}
