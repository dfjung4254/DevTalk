package com.devjk.devtalk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devjk.devtalk.R;
import com.devjk.devtalk.activity.SplashActivity;

public class ScheduleFragment extends Fragment {

    private LinearLayout mainlayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        mainlayout = (LinearLayout) view.findViewById(R.id.ScheduleFragment_LinearLayout_mainView);
        mainlayout.setBackgroundColor(Color.parseColor(SplashActivity.THEME_COLOR));

        return view;
    }

}
