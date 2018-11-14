package com.devjk.devtalk.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.devjk.devtalk.R;
import com.devjk.devtalk.activity.SplashActivity;

public class UsersFragment extends Fragment {

    private FrameLayout mainlayout;
    public FrameLayout searchLayout;
    private EditText editText_search;
    private Button btn_search;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mainlayout = (FrameLayout) view.findViewById(R.id.UsersFragment_FrameLayout_mainView);
        mainlayout.setBackgroundColor(Color.parseColor(SplashActivity.THEME_COLOR));
        searchLayout = (FrameLayout) view.findViewById(R.id.UsersFragment_FrameLayout_searchLayout);
        editText_search = (EditText) view.findViewById(R.id.UsersFragment_EditText_txtSearch);
        btn_search = (Button) view.findViewById(R.id.UsersFragment_Button_btnSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.UsersFragment_RecyclerView_recyclerView);

        btn_search.setBackgroundResource(R.drawable.icon_search);

        return view;
    }



}
