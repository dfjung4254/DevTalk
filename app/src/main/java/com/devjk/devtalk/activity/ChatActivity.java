package com.devjk.devtalk.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.devjk.devtalk.R;

public class ChatActivity extends AppCompatActivity {

    private Button btn_menu;
    private RecyclerView recyclerView;
    private RelativeLayout chatBar;
    private EditText editText_chat;
    private ImageButton btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



    }
}
