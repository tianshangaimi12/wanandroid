package com.example.wanandroid.utils;

import com.example.wanandroid.javabean.BannerBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zhangchong on 18-3-12.
 */
public interface RetrofitUtils {

    @GET("/banner/json")
    Observable<BannerBean> callBanner();

}
