package com.example.wanandroid.contoller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhangchong on 18-3-12.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments != null)
        {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if(fragments != null)
        {
            return fragments.size();
        }
        return 0;
    }
}
