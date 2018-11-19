package com.devjk.devtalk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.devjk.devtalk.R;
import com.devjk.devtalk.controller.AuthController;
import com.devjk.devtalk.fragment.ChatListFragment;
import com.devjk.devtalk.fragment.MyAccountFragment;
import com.devjk.devtalk.fragment.ScheduleFragment;
import com.devjk.devtalk.fragment.UsersFragment;
import com.devjk.devtalk.models.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    public static String MYTAG = "MY_DEBUGGING_TAG ********** : ";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txt_title;
    private ImageView icon_title;

    private UsersFragment usersFragment;
    private ChatListFragment chatListFragment;
    private ScheduleFragment scheduleFragment;
    private MyAccountFragment myAccountFragment;

    private TabPageAdapter adapter;
    private LinearLayout barLayout[] = new LinearLayout[4];
    private int[] tabIcons = {
            R.drawable.icon_friends_1,
            R.drawable.icon_chat_1,
            R.drawable.icon_schedule_1,
            R.drawable.icon_account_1
    };
    private int[] tabIcons_unSelected = {
            R.drawable.icon_friends_2,
            R.drawable.icon_chat_2,
            R.drawable.icon_schedule_2,
            R.drawable.icon_account_2
    };
    private int[] tabIcons_white = {
            R.drawable.icon_friends_3,
            R.drawable.icon_chat_3,
            R.drawable.icon_schedule_3,
            R.drawable.icon_account_3
    };
    private String[] tabTitles = {
            "Users", "Chats", "Schedule", "MyAccount"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor(SplashActivity.THEME_COLOR));
        setContentView(R.layout.activity_main);

        //set currentUser Information
        //계속해서 현재 유저정보에 변화가 있으면 AuthController의 currentUser Model을 갱신한다.
        AuthController.getInstance().listenCurrentUserUpdate().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(MainActivity.MYTAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    AuthController.currentUser = snapshot.toObject(UserModel.class);
                    Log.d(MainActivity.MYTAG, "CurrentUser 정보 갱신: ");
                } else {
                    Log.d(MainActivity.MYTAG, "Current data: null");
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.MainActivity_TabLayout_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.MainActivity_ViewPager_viewPager);
        txt_title = (TextView) findViewById(R.id.MainActivity_TextView_title);
        icon_title = (ImageView) findViewById(R.id.MainActivity_ImageView_titleIcon);
        barLayout[0] = (LinearLayout) findViewById(R.id.MainActivity_LinearLayout_usersBarLayout);
        barLayout[1] = (LinearLayout) findViewById(R.id.MainActivity_LinearLayout_chatsBarLayout);
        barLayout[2] = (LinearLayout) findViewById(R.id.MainActivity_LinearLayout_scheduleBarLayout);
        barLayout[3] = (LinearLayout) findViewById(R.id.MainActivity_LinearLayout_myAccountBarLayout);

        //set Fragments
        usersFragment = new UsersFragment();
        chatListFragment = new ChatListFragment();
        scheduleFragment = new ScheduleFragment();
        myAccountFragment = new MyAccountFragment();

        //set theme color
        toolbar.setBackgroundColor(Color.parseColor(SplashActivity.THEME_COLOR));

        //add toolbar to the activity
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                txt_title.setText(tabTitles[pos]);
                icon_title.setImageResource(tabIcons_white[pos]);
                tabLayout.getTabAt(pos).setIcon(tabIcons[pos]);
                barLayout[pos].setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                tabLayout.getTabAt(pos).setIcon(tabIcons_unSelected[pos]);
                barLayout[pos].setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //뷰페이저 설정.
    private void setupViewPager(ViewPager viewPager){
        adapter = new TabPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
    //탭 아이콘 설정
    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_unSelected[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_unSelected[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons_unSelected[3]);
        txt_title.setText(tabTitles[0]);
        icon_title.setImageResource(tabIcons_white[0]);
        setUsersTabIcons();
        setChatsTabIcons();
        setScheduleTabIcons();
        setMyAccountTabIcons();
        barLayout[0].setVisibility(View.VISIBLE);
        barLayout[1].setVisibility(View.GONE);
        barLayout[2].setVisibility(View.GONE);
        barLayout[3].setVisibility(View.GONE);
    }
    private void setUsersTabIcons(){
        Button btn_search = (Button) findViewById(R.id.MainActivity_Button_btnSearch);
        btn_search.setBackgroundResource(R.drawable.icon_search_white);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //페이드인 검색바 등장.
                if(usersFragment.searchLayout.getVisibility() == View.GONE){
                    usersFragment.searchLayout
                            .animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    usersFragment.searchLayout.setVisibility(View.VISIBLE);
                                    usersFragment.searchLayout.setFocusable(true);
                                }
                            });
                }else{
                    usersFragment.searchLayout
                            .animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    usersFragment.searchLayout.setVisibility(View.GONE);
                                }
                            });

                }

            }
        });
    }
    private void setChatsTabIcons(){

    }
    private void setScheduleTabIcons(){

    }
    private void setMyAccountTabIcons(){

    }
    //탭 페이지 어댑터
    private class TabPageAdapter extends FragmentPagerAdapter {

        private final int TABSIZE = 4;

        public final Fragment[] mFragmentList = new Fragment[TABSIZE];
        public final String[] mFragmentTitleList = new String[TABSIZE];

        public TabPageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentList[0] = usersFragment;
            mFragmentTitleList[0] = "Users";
            mFragmentList[1] = chatListFragment;
            mFragmentTitleList[1] = "Chats";
            mFragmentList[2] = scheduleFragment;
            mFragmentTitleList[2] = "Schedule";
            mFragmentList[3] = myAccountFragment;
            mFragmentTitleList[3] = "MyAccount";
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList[position];
        }

        @Override
        public int getCount() {
            return TABSIZE;
        }
    }


}
