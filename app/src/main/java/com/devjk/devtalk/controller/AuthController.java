package com.devjk.devtalk.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthController {

    private static AuthController instance;
    private FirebaseAuth firebaseAuth;

    public AuthController() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(final AppCompatActivity parent,
                              final String email, final String password, String cPassword,
                              final String nickName, final String phone, String profileUri){
        boolean validate = false;
        //check validation
        //1. 이메일 형식검사, 2-1. 패스워드 자릿수검사 2-2. 패스워드 일치검사 3. 닉네임 중복검사 4. 폰11자리 검사 '-'검사 5. 프로필설정 검사
        int ret = ValidateCheck.getInstance().checkAccount(email, password, cPassword, nickName, phone, profileUri);
        switch (ret){
            case ValidateCheck.VALIDATE_PASS:
                validate = true;
                break;
            case ValidateCheck.VALIDATE_EMAIL_FORM:
                Toast.makeText(parent, "올바른 이메일 양식을 입력하세요", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_PWD_CHARLESS:
                Toast.makeText(parent, "비밀번호는 6자리 이상으로 설정해주세요", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_PWD_NONCONFIRM:
                Toast.makeText(parent, "비밀번호와 확인이 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_NICKNAME_EXISTS:
                Toast.makeText(parent, "일치하는 닉네임이 있습니다", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_PHONE_FORM:
                Toast.makeText(parent, "전화번호 11자리를 입력해주세요", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_PHONE_BAR:
                Toast.makeText(parent, "'-'를 제외한 11자리를 입력해주세요", Toast.LENGTH_SHORT).show();
                break;
            case ValidateCheck.VALIDATE_PROFILE_NON:
                Toast.makeText(parent, "프로필을 설정해주세요", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //
        if(validate){
            //auth create
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //계정Auth생성되면 DB에 저장.
                        String uid = task.getResult().getUser().getUid();
                        boolean isSaved = DatabaseController.getInstance().addUserInfo(new UserModel(
                                uid, email, password, nickName, phone).getMapModel()
                        );
                        if(isSaved){
                            //DB에 저장 성공.
                            //Profile사진 저장.
                            Toast.makeText(parent, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            parent.finish();
                        }
                    }else{
                        Toast.makeText(parent, "회원가입 실패 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //profile storage upload

            //db upload

            //Toast and finish();
            //parent.finish();
        }
    }


    public static AuthController getInstance() {
        if(instance == null){
            instance = new AuthController();
        }
        return instance;
    }
}
