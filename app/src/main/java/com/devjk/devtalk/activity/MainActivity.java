package com.devjk.devtalk.activity;

import android.graphics.drawable.Icon;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.devjk.devtalk.Adapter.TabPageAdapter;
import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.fragment.ChatListFragment;
import com.devjk.devtalk.fragment.MyAccountFragment;
import com.devjk.devtalk.fragment.ScheduleFragment;
import com.devjk.devtalk.fragment.UsersFragment;

public class MainActivity extends AppCompatActivity {

    public static String MYTAG = "MY_DEBUGGING_TAG ********** : ";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txt_title;

    private int[] tabIcons = {
            R.drawable.icon_friends,
            R.drawable.icon_chat,
            R.drawable.icon_schedule,
            R.drawable.icon_account
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.MainActivity_TabLayout_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.MainActivity_ViewPager_viewPager);
        txt_title = (TextView) findViewById(R.id.MainActivity_TextView_title);

        //add toolbar to the activity
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    //뷰페이저 설정.
    private void setupViewPager(ViewPager viewPager){
        TabPageAdapter adpapter = new TabPageAdapter(getSupportFragmentManager());
        adpapter.addFragment(new UsersFragment(), "Users");
        adpapter.addFragment(new ChatListFragment(), "Chatting");
        adpapter.addFragment(new ScheduleFragment(), "Schedule");
        adpapter.addFragment(new MyAccountFragment(), "MyAccount");
        viewPager.setAdapter(adpapter);
    }
    //탭 아이콘 설정
    private void setupTabIcons(){
        ImageView tab1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setImageResource(tabIcons[0]);
        tab1.setPadding(25,25,25,25);
        tabLayout.getTabAt(0).setCustomView(tab1);
        ImageView tab2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setImageResource(tabIcons[1]);
        tab2.setPadding(25,25,25,25);
        tabLayout.getTabAt(1).setCustomView(tab2);
        ImageView tab3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab3.setImageResource(tabIcons[2]);
        tab3.setPadding(25,25,25,25);
        tabLayout.getTabAt(2).setCustomView(tab3);
        ImageView tab4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab4.setImageResource(tabIcons[3]);
        tab4.setPadding(25,25,25,25);
        tabLayout.getTabAt(3).setCustomView(tab4);
    }

}
