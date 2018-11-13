package com.devjk.devtalk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;

public class MainActivity extends AppCompatActivity {

    public static String MYTAG = "MY_DEBUGGING_TAG ********** : ";

    private TextView txt_email;
    private TextView txt_password;
    private TextView txt_nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_email = (TextView) findViewById(R.id.MainActivity_TextView_myEmail);
        txt_password = (TextView) findViewById(R.id.MainActivity_TextView_myPassword);
        txt_nickName = (TextView) findViewById(R.id.MainActivity_TextView_myNickName);

        txt_email.setText(AuthController.getInstance().getCurrentUser().getEmail());
        txt_nickName.setText(AuthController.getInstance().getCurrentUser().getNickName());
        txt_password.setText(AuthController.getInstance().getCurrentUser().getPassword());
    }
}
