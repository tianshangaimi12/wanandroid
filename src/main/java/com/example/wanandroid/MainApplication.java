package com.example.wanandroid;

import android.app.Application;
import android.util.Log;

import com.example.wanandroid.utils.RetrofitUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangchong on 18-4-12.
 */
public class MainApplication extends Application{
    public Retrofit builder;
    public static RetrofitUtils retrofitUtils;

    private final String BASE_URL = "http://www.wanandroid.com/";
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Log.d(TAG, "saveFromResponse: "+cookies);
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        Log.d(TAG, "loadForRequest: "+cookies);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitUtils = builder.create(RetrofitUtils.class);
    }
}
