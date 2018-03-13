package com.example.wanandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanandroid.R;
import com.example.wanandroid.contoller.BannerAdapter;
import com.example.wanandroid.javabean.BannerBean;
import com.example.wanandroid.view.ViewPagerIndex;

/**
 * Created by zhangchong on 18-3-12.
 */
public class FirstPageFragment extends Fragment {

    private ViewPager bannerVp;
    private ViewPagerIndex viewPagerIndex;

    private BannerBean bannerBean;

    private final String TAG = "FirstPageFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        bannerVp = (ViewPager)view.findViewById(R.id.vp_banner);
        bannerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerIndex.setIndexLocate(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerIndex = (ViewPagerIndex)view.findViewById(R.id.vp_index_banner);
    }

    public void setBannerBean(BannerBean bannerBean)
    {
        this.bannerBean = bannerBean;
        BannerAdapter adapter = new BannerAdapter(bannerBean.getData(), getActivity());
        bannerVp.setAdapter(adapter);
        viewPagerIndex.setIndexNum(bannerBean.getData().size());
    }
}
