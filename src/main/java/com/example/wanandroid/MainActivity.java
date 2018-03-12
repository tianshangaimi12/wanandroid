package com.example.wanandroid;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mainDrawerLayout;
    private RelativeLayout settingRlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainDrawerLayout = (DrawerLayout)findViewById(R.id.dl_main);
        settingRlayout = (RelativeLayout)findViewById(R.id.rl_setting);
    }
}
