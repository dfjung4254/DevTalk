package com.devjk.devtalk.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.ValidateCheck;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_exit;
    private ImageView imgv_profile;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_passwordConfirm;
    private EditText editText_nickName;
    private EditText editText_phone;
    private Button btn_join;

    private String profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        btn_exit = (Button) findViewById(R.id.SignUpActivity_Button_exit);
        imgv_profile = (ImageView) findViewById(R.id.SignUpActivity_ImageView_profile);
        editText_email = (EditText) findViewById(R.id.SignUpActivity_EditText_email);
        editText_password = (EditText) findViewById(R.id.SignUpActivity_EditText_password);
        editText_passwordConfirm = (EditText) findViewById(R.id.SignUpActivity_EditText_passwordConfirm);
        editText_nickName = (EditText) findViewById(R.id.SignUpActivity_EditText_email);
        editText_phone = (EditText) findViewById(R.id.SignUpActivity_EditText_phone);
        btn_join = (Button) findViewById(R.id.SignUpActivity_Button_join);

        btn_exit.setOnClickListener(new ExitOnClickListener());
        btn_join.setOnClickListener(new JoinOnClickListener());

    }

    private class JoinOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AuthController.getInstance().createAccount(
                    SignUpActivity.this,
                    editText_email.getText().toString(),
                    editText_password.getText().toString(),
                    editText_passwordConfirm.getText().toString(),
                    editText_nickName.getText().toString(),
                    editText_phone.getText().toString(),
                    profileUri
            );
        }
    }

    private class ExitOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
