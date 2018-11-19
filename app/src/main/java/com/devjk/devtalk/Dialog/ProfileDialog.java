package com.devjk.devtalk.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.devjk.devtalk.models.UserModel;

import org.w3c.dom.Text;

public class ProfileDialog extends Dialog {

    private Context context;
    private UserModel thisUser;

    private TextView txt_nickName;
    private TextView txt_status;
    private TextView txt_email;
    private ImageView imgv_profile;
    private ImageButton btn_privateChat;
    private ImageButton btn_groupChat;
    private ImageButton btn_backPress;

    public ProfileDialog(@NonNull Context context, UserModel userModel) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        this.context = context;
        this.thisUser = userModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(lpWindow);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_profile);

        txt_nickName = (TextView) findViewById(R.id.ProfileDialog_TextView_nickName);
        txt_status = (TextView) findViewById(R.id.ProfileDialog_TextView_status);
        txt_email = (TextView) findViewById(R.id.ProfileDialog_TextView_email);
        imgv_profile = (ImageView) findViewById(R.id.ProfileDialog_ImageView_profile);
        btn_privateChat = (ImageButton) findViewById(R.id.ProfileDialog_Button_privateChat);
        btn_groupChat = (ImageButton) findViewById(R.id.ProfileDialog_Button_groupChat);
        btn_backPress = (ImageButton) findViewById(R.id.ProfileDialog_Button_backPress);

        setValues();
        setOnClickListeners();

    }
    public void setOnClickListeners(){
        btn_privateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1:1대화방으로 간다
                getContext().startActivity(new Intent(getContext(), ChatActivity.class));
                ProfileDialog.this.dismiss();
                /*
                *
                *
                * 이 부분으로 대화방 생성 및 채팅의 시작.
                *
                *
                */

            }
        });
        btn_groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //그룹초대, 현재 내가 속한 단체대화방 목록을 보여준다

                /*
                 *
                 *
                 * 이 부분으로 대화방 목록보여주고 중간에 유저 추가 가능.
                 *
                 *
                 */

            }
        });
        btn_backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //나가기
                onBackPressed();
            }
        });
    }

    public void setValues(){
        Glide.with(getContext())
                .load(thisUser.getProfileUrl())
                .apply(new RequestOptions().circleCrop())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(MainActivity.MYTAG, "이미지 로드실패 : "+e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        txt_nickName.setText(thisUser.getNickName());
                        txt_status.setText(thisUser.getStatus());
                        txt_email.setText(thisUser.getEmail());
                        return false;
                    }
                })
                .into(imgv_profile);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
