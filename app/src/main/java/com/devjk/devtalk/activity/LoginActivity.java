package com.devjk.devtalk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devjk.devtalk.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_password;
    private Button btn_login;
    private Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_email = (EditText) findViewById(R.id.LoginActivity_EditText_email);
        editText_password = (EditText) findViewById(R.id.LoginActivity_EditText_password);
        btn_login = (Button) findViewById(R.id.LoginActivity_Button_login);
        btn_signUp = (Button) findViewById(R.id.LoginActivity_Button_signUp);

        btn_login.setOnClickListener(new LoginOnClickListener());
        btn_signUp.setOnClickListener(new SignUpOnClickListener());

    }

    public void checkAuthandLogin(String inputEmail, String inputPassword){

    }

    private class LoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //authCheck and go Main Page
            checkAuthandLogin(editText_email.getText().toString(), editText_password.getText().toString());
        }
    }

    private class SignUpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //go SignUp activity
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

}
