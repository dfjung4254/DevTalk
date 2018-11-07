package com.devjk.devtalk.controller;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devjk.devtalk.models.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;

public class AuthController {

    private static AuthController instance;
    private FirebaseAuth firebaseAuth;

    public AuthController() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //계정 유효성 검사
    public boolean createAccountValidate(final AppCompatActivity parent,
                                     final String email, final String password, String cPassword,
                                     final String nickName, final String phone, final Uri profileUri){
        //check validation
        //1. 이메일 형식검사, 2-1. 패스워드 자릿수검사 2-2. 패스워드 일치검사 3. 닉네임 중복검사 4. 폰11자리 검사 '-'검사 5. 프로필설정 검사
        boolean validate = false;
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
        return validate;
    }
    //계정 생성.
    public void createAccount(final AppCompatActivity parent,
                              final String email, final String password, String cPassword,
                              final String nickName, final String phone, final Uri profileUri){

        if(createAccountValidate(parent, email, password, cPassword, nickName, phone, profileUri)){
            //auth create
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //계정Auth생성되면 사진을 Stroage에 저장.
                        final String uid = task.getResult().getUser().getUid();
                        StorageController.getInstance().uploadProfileUri(profileUri, uid).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    StorageController.getInstance().getDownloadURL(profileUri, uid).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if(task.isSuccessful()){
                                                Uri downloadUri = task.getResult();
                                                //Storage에 사진저장한 URL을 포함한 DB정보를 저장.
                                                DatabaseController.getInstance().addUserInfo(parent, new UserModel(
                                                        uid, email, password, nickName, phone, downloadUri.toString())
                                                .getMapModel()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(parent, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                                            parent.finish();
                                                        }else{
                                                            Toast.makeText(parent, "회원가입 실패 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(parent, "회원가입 실패 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(parent, "회원가입 실패 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(parent, "회원가입 실패 : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public static AuthController getInstance() {
        if(instance == null){
            instance = new AuthController();
        }
        return instance;
    }
}
