package com.example.wanandroid.contoller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.wanandroid.WebViewActivity;
import com.example.wanandroid.javabean.BannerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchong on 18-3-12.
 */
public class BannerAdapter extends PagerAdapter {
    private List<BannerBean.BannerItem> bannerItems;
    private List<View> views = new ArrayList<>();
    private Context context;

    public BannerAdapter(List<BannerBean.BannerItem> bannerItems, Context context)
    {
        this.bannerItems = bannerItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bannerItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        Glide.with(context)
                .load(bannerItems.get(position).getImagePath())
                .into(imageView);
        container.addView(imageView, params);
        views.add(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerBean.BannerItem bannerItem = bannerItems.get(position);
                String loadUrl = bannerItem.getUrl();
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("load_url", loadUrl);
                context.startActivity(intent);
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
