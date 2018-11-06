package com.devjk.devtalk;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

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
            if(checkValidate()){
                //회원가입.
                mAuth.createUserWithEmailAndPassword(
                        editText_email.getText().toString(),
                        editText_password.getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //성공
                            Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            //실패

                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "제대로 입력해라잉", Toast.LENGTH_SHORT).show();
            }
        }

        public boolean checkValidate(){
            boolean ret = true;
            //회원정보 입력 유효성 검사.

            return ret;
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
