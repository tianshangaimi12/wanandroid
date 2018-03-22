package com.example.wanandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wanandroid.R;
import com.example.wanandroid.view.CircleCrop;

/**
 * Created by zhangchong on 18-3-22.
 */
public class SettingFragment extends Fragment {

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        return view;
    }

    public void init(View view)
    {
        imageView = (ImageView)view.findViewById(R.id.img_setting_user);
        Glide.with(getActivity())
                .load(R.drawable.user)
                .transform(new CircleCrop(getActivity()))
                .into(imageView);
    }
}
