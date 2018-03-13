package com.example.wanandroid.contoller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //viewpager会保存三个Fragment，从第一个到第三个Fragment时会重新加载
        //注释掉取消重新加载
        //super.destroyItem(container, position, object);
    }
}
