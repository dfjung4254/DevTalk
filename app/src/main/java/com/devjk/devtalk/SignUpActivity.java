package com.devjk.devtalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_exit;
    private ImageView imgv_profile;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_passwordConfirm;
    private EditText editText_nickName;
    private EditText editText_phone;
    private Button btn_join;

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
