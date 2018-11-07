package com.devjk.devtalk.activity;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.controller.LoadController;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_exit;
    private ImageView imgv_profile;
    private TextView text_profileName;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_passwordConfirm;
    private EditText editText_nickName;
    private EditText editText_phone;
    private Button btn_join;

    private Uri profileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        btn_exit = (Button) findViewById(R.id.SignUpActivity_Button_exit);
        imgv_profile = (ImageView) findViewById(R.id.SignUpActivity_ImageView_profile);
        text_profileName = (TextView) findViewById(R.id.SignUpActivity_TextView_profileName);
        editText_email = (EditText) findViewById(R.id.SignUpActivity_EditText_email);
        editText_password = (EditText) findViewById(R.id.SignUpActivity_EditText_password);
        editText_passwordConfirm = (EditText) findViewById(R.id.SignUpActivity_EditText_passwordConfirm);
        editText_nickName = (EditText) findViewById(R.id.SignUpActivity_EditText_email);
        editText_phone = (EditText) findViewById(R.id.SignUpActivity_EditText_phone);
        btn_join = (Button) findViewById(R.id.SignUpActivity_Button_join);

        btn_exit.setOnClickListener(new ExitOnClickListener());
        btn_join.setOnClickListener(new JoinOnClickListener());
        imgv_profile.setOnClickListener(new ProfileClickListener());

    }

    private class ProfileClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, LoadController.CODE_SELECT_IMAGE);
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LoadController.CODE_SELECT_IMAGE){
            //사진 받아올때. 사진 이미지 수정, 텍스트 제목 수정.
            if(resultCode == Activity.RESULT_OK){
                profileUri = data.getData();
                imgv_profile.setImageURI(profileUri);
                String[] proj = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(this, profileUri, proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Uri pathUri = Uri.parse(cursor.getString(column_index));
                String imgName = pathUri.getLastPathSegment().toString();
                cursor.close();
                text_profileName.setText(imgName);
            }
        }
    }
}
