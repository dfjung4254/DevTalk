package com.devjk.devtalk.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devjk.devtalk.Dialog.LoadingDialog;
import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_password;
    private Button btn_login;
    private Button btn_signUp;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this);

        editText_email = (EditText) findViewById(R.id.LoginActivity_EditText_email);
        editText_password = (EditText) findViewById(R.id.LoginActivity_EditText_password);
        btn_login = (Button) findViewById(R.id.LoginActivity_Button_login);
        btn_signUp = (Button) findViewById(R.id.LoginActivity_Button_signUp);

        btn_login.setOnClickListener(new LoginOnClickListener());
        btn_signUp.setOnClickListener(new SignUpOnClickListener());

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(AuthController.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
    }

    private class LoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //authCheck and go Main Page
            loadingDialog.show();
            AuthController.getInstance().checkAuthandLogin(getParent(), editText_email.getText().toString(), editText_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                AuthController.getInstance().setCurrentUser().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        AuthController.currentUser = documentSnapshot.toObject(UserModel.class);
                                        loadingDialog.dismiss();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        LoginActivity.this.finish();
                                    }
                                });
                            }else{
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private class SignUpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //go SignUp activity
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
