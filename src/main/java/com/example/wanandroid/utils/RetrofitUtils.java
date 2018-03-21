package com.example.wanandroid.utils;

import com.example.wanandroid.javabean.BannerBean;
import com.example.wanandroid.javabean.FamousWebsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.javabean.SystemBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhangchong on 18-3-12.
 */
public interface RetrofitUtils {

    @GET("banner/json")
    Observable<BannerBean> callBanner();

    @GET("article/list/{page}/json")
    Observable<PageNewsBean> callFirstPage(@Path("page") int page);

    @GET("friend/json")
    Observable<FamousWebsBean> callFamousWebs();

    @GET("tree/json")
    Observable<SystemBean> callSystem();

    /**
     * 搜索体系单个分支文章
     * @param page 页数
     * @param cid  分支courseId
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<PageNewsBean> callSystemItemData(@Path("page") int page, @Query("cid") int cid);

}
