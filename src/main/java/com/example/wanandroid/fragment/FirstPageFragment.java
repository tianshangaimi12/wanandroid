package com.example.wanandroid.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanandroid.MainApplication;
import com.example.wanandroid.R;
import com.example.wanandroid.WebViewActivity;
import com.example.wanandroid.contoller.BannerAdapter;
import com.example.wanandroid.contoller.FirstPageNewsAdapter;
import com.example.wanandroid.contoller.ItemClickListener;
import com.example.wanandroid.javabean.BannerBean;
import com.example.wanandroid.javabean.NewsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.utils.BroadCastUtils;
import com.example.wanandroid.utils.RetrofitUtils;
import com.example.wanandroid.view.ViewPagerIndex;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangchong on 18-3-12.
 */
public class FirstPageFragment extends Fragment {

    private ViewPager bannerVp;
    private ViewPagerIndex viewPagerIndex;
    private RecyclerView recyclerView;

    private RetrofitUtils retrofitUtils;
    private List<NewsBean> newsBeens;
    private FirstPageNewsAdapter firstPageNewsAdapter;
    private Context context;
    private Receiver receiver;
    private IntentFilter intentFilter;

    private final String TAG = "FirstPageFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new Receiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtils.ACTION_NOTIFY_DATA_CHANGED);
        context.registerReceiver(receiver, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofitUtils = MainApplication.retrofitUtils;
        getBanner();
        getFirstPageNews(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null)
        {
            context.unregisterReceiver(receiver);
        }
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
        newsBeens = new ArrayList<>();
        firstPageNewsAdapter = new FirstPageNewsAdapter(getActivity(), newsBeens);
        recyclerView = (RecyclerView)view.findViewById(R.id.rlv_first_page_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firstPageNewsAdapter);
    }

    /**
     * 获取首页Banner
     */
    public void getBanner()
    {
        Observable<BannerBean> observableBanner = retrofitUtils.callBanner();
        observableBanner.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BannerBean>() {
                    @Override
                    public void accept(BannerBean bannerBean) throws Exception {
                        if (bannerBean.getErrorCode() == 0)
                        {
                            Log.d(TAG, "banner size: "+bannerBean.getData().size());
                            BannerAdapter adapter = new BannerAdapter(bannerBean.getData(), context);
                            bannerVp.setAdapter(adapter);
                            viewPagerIndex.setIndexNum(bannerBean.getData().size());
                        }
                        else Log.d(TAG, bannerBean.getErrorMsg());
                    }
                });
    }


    /**
     * 获取首页新闻列表
     * @param page 页数
     */
    public void getFirstPageNews(int page)
    {
        Observable<PageNewsBean> observableFirstPage = retrofitUtils.callFirstPage(page);
        observableFirstPage.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageNewsBean>() {
                    @Override
                    public void accept(PageNewsBean firstPageNewsBean) throws Exception {
                        if(firstPageNewsBean.getErrorCode() == 0)
                        {
                            Log.d(TAG, "news size: "+firstPageNewsBean.getData().getDatas().size());
                            newsBeens = firstPageNewsBean.getData().getDatas();
                            firstPageNewsAdapter = new FirstPageNewsAdapter(getActivity(), newsBeens);
                            recyclerView.setAdapter(firstPageNewsAdapter);
                            firstPageNewsAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String loadUrl = newsBeens.get(position).getLink();
                                    Intent intent = new Intent(context, WebViewActivity.class);
                                    intent.putExtra("load_url", loadUrl);
                                    context.startActivity(intent);
                                }

                            });
                        }
                        else Log.d(TAG, firstPageNewsBean.getErrorMsg());
                    }
                });
    }


    class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "receive action: "+action);
            if(action.equals(BroadCastUtils.ACTION_NOTIFY_DATA_CHANGED))
            {
                getFirstPageNews(0);
            }
        }
    }

}
