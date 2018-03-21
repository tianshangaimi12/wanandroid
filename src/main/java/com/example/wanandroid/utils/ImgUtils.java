package com.example.wanandroid.utils;

import android.content.Context;

/**
 * Created by zhangchong on 18-3-20.
 */
public class ImgUtils {

    public static int dip2Pix(Context context, int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp +0.5f);
    }
}
