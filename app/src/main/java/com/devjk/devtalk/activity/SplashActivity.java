package com.devjk.devtalk.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devjk.devtalk.BuildConfig;
import com.devjk.devtalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    TextView sentence;
    TextView version;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_splash);
        //SplashActivity 작성 2018.10.29 정근화
        sentence = (TextView)findViewById(R.id.SplashActivity_TextView_sentence);
        version = (TextView)findViewById(R.id.SplashActivity_TextView_version);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //디버깅모드 설정
        //원래 서버의 과부화를 막기 위해 1분에 3번이상 요청하지
        //못하고 fail이 뜨는데 fail을 막는 코드.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        //RemoteConfig 디폴트 설정.및 fetch firebase에서 정보 가져오기
        //fetch() 안의 0은 몇초마다 요청하는지 텀이다.
        //0으로 설정해놓으면 접속할때마다 요청함.
        //만약 3600으로 해놓으면 1시간마다 요청을 함.
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                        }
                        //fetch가 성공적으로 되면 가져온 값들을 적용.
                        showValuesAndLoading();
                    }
                });

    }

    void showValuesAndLoading(){

        String str_sentence1 = mFirebaseRemoteConfig.getString("splash_sentence1");
        String str_sentence2 = mFirebaseRemoteConfig.getString("splash_sentence2");
        String str_version = mFirebaseRemoteConfig.getString("splash_version");
        Boolean caps = mFirebaseRemoteConfig.getBoolean("splash_cap");
        String theme_color = mFirebaseRemoteConfig.getString("theme_color");
        String str_fixing = mFirebaseRemoteConfig.getString("splash_fixing");
        sentence.setText(str_sentence1+"\n"+str_sentence2);
        version.setText(str_version);
        getWindow().setStatusBarColor(Color.parseColor(theme_color));

        if(caps){
            //진입가능 로딩 구현
            Handler hd = new Handler();
            hd.postDelayed(new GoMainHandler(), 2000);

        }else{
            //서버점검 등으로 진입 막음
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("서버점검중");
            builder.setMessage(str_fixing);
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                        }
                    });
            builder.show();
        }


    }

    private class GoMainHandler implements Runnable{
        @Override
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,0);
    }
}