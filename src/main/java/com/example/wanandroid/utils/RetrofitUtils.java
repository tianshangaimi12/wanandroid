package com.example.wanandroid.utils;

import com.example.wanandroid.javabean.BannerBean;
import com.example.wanandroid.javabean.FamousWebsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.javabean.SystemBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @GET("article/list/{page}/json")
    Observable<PageNewsBean> callSystemItemData(@Path("page") int page, @Query("cid") int cid);

    //注册
    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> registerCall(@FieldMap Map<String, String> params);

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> loginCall(@Field("username") String username, @Field("password") String password);

    //收藏站内文章
    @FormUrlEncoded
    @POST("lg/collect/{id}/json")
    Call<ResponseBody> collectInMessage(@Path("id") int id);

}
