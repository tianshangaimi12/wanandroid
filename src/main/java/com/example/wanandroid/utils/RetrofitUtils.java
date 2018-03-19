package com.example.wanandroid.utils;

import com.example.wanandroid.javabean.BannerBean;
import com.example.wanandroid.javabean.FirstPageNewsBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zhangchong on 18-3-12.
 */
public interface RetrofitUtils {

    @GET("banner/json")
    Observable<BannerBean> callBanner();

    @GET("article/list/{page}/json")
    Observable<FirstPageNewsBean> callFirstPage(@Path("page") int page);

}
