package com.example.wanandroid;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanandroid.contoller.MainFragmentAdapter;
import com.example.wanandroid.fragment.FirstPageFragment;
import com.example.wanandroid.fragment.NavigationFragment;
import com.example.wanandroid.fragment.SettingFragment;
import com.example.wanandroid.fragment.SystemFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout mainDrawerLayout;
    private Toolbar mainToolbar;
    private ViewPager mainViewPager;
    private LinearLayout firstPageLL;
    private LinearLayout systemLL;
    private LinearLayout navigationLL;
    private FrameLayout settingFl;
    private ImageView firstPageImg;
    private ImageView systemImg;
    private ImageView navigationImg;
    private TextView firstPageTxt;
    private TextView systemTxt;
    private TextView navigationTxt;

    private FirstPageFragment firstPageFragment;
    private SystemFragment systemFragment;
    private NavigationFragment navigationFragment;
    private SettingFragment settingFragment;
    private MainFragmentAdapter mainFragmentAdapter;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initToolBar();
    }

    private void initView()
    {
        firstPageLL = (LinearLayout)findViewById(R.id.ll_bottom_first_page);
        firstPageImg = (ImageView)findViewById(R.id.img_bottom_first_page);
        firstPageTxt = (TextView)findViewById(R.id.txt_bottom_first_page);
        systemLL = (LinearLayout)findViewById(R.id.ll_bottom_system);
        systemImg = (ImageView)findViewById(R.id.img_bottom_system);
        systemTxt = (TextView)findViewById(R.id.txt_bottom_system);
        navigationLL = (LinearLayout)findViewById(R.id.ll_bottom_navigation);
        navigationImg = (ImageView)findViewById(R.id.img_bottom_navigation);
        navigationTxt = (TextView)findViewById(R.id.txt_bottom_navigation);
        mainDrawerLayout = (DrawerLayout)findViewById(R.id.dl_main);
        settingFl = (FrameLayout) findViewById(R.id.fl_setting);
        mainViewPager = (ViewPager)findViewById(R.id.vp_main);
        mainViewPager.setAdapter(mainFragmentAdapter);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    firstPageImg.setImageResource(R.drawable.ic_first_page_select);
                    firstPageTxt.setTextColor(getResources().getColor(R.color.colorMain));
                    systemImg.setImageResource(R.drawable.ic_system);
                    systemTxt.setTextColor(Color.parseColor("#8a000000"));
                    navigationImg.setImageResource(R.drawable.ic_navigation);
                    navigationTxt.setTextColor(Color.parseColor("#8a000000"));
                    mainToolbar.setTitle(R.string.first_index);
                }
                else if(position == 1)
                {
                    firstPageImg.setImageResource(R.drawable.ic_first_page);
                    firstPageTxt.setTextColor(Color.parseColor("#8a000000"));
                    systemImg.setImageResource(R.drawable.ic_system_select);
                    systemTxt.setTextColor(getResources().getColor(R.color.colorMain));
                    navigationImg.setImageResource(R.drawable.ic_navigation);
                    navigationTxt.setTextColor(Color.parseColor("#8a000000"));
                    mainToolbar.setTitle(R.string.second_index);
                }
                else if(position == 2)
                {
                    firstPageImg.setImageResource(R.drawable.ic_first_page);
                    firstPageTxt.setTextColor(Color.parseColor("#8a000000"));
                    systemImg.setImageResource(R.drawable.ic_system);
                    systemTxt.setTextColor(Color.parseColor("#8a000000"));
                    navigationImg.setImageResource(R.drawable.ic_navigation_select);
                    navigationTxt.setTextColor(getResources().getColor(R.color.colorMain));
                    mainToolbar.setTitle(R.string.third_index);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        firstPageLL.setOnClickListener(this);
        systemLL.setOnClickListener(this);
        navigationLL.setOnClickListener(this);
    }

    private void initToolBar()
    {
        mainToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mainToolbar.setTitle(R.string.first_index);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitleTextColor(Color.WHITE);
        mainToolbar.setTitleMarginStart(310);
        mainToolbar.setNavigationIcon(R.drawable.ic_menu);
        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_search:
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_bottom_first_page:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.ll_bottom_system:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.ll_bottom_navigation:
                mainViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    public void initData()
    {
        List<Fragment> fragments = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        firstPageFragment = new FirstPageFragment();
        systemFragment = new SystemFragment();
        navigationFragment = new NavigationFragment();
        settingFragment = new SettingFragment();
        fragments.add(firstPageFragment);
        fragments.add(systemFragment);
        fragments.add(navigationFragment);
        mainFragmentAdapter = new MainFragmentAdapter(fm, fragments);
        fm.beginTransaction().add(R.id.fl_setting, settingFragment).commit();
    }


    public void setLoginUserName(String userName)
    {
        if(settingFragment != null)
        {
            settingFragment.setUserName(userName);
        }
    }
}

