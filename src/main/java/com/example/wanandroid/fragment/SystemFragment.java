package com.example.wanandroid.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wanandroid.MainApplication;
import com.example.wanandroid.R;
import com.example.wanandroid.WebViewActivity;
import com.example.wanandroid.contoller.FirstPageNewsAdapter;
import com.example.wanandroid.contoller.ItemClickListener;
import com.example.wanandroid.contoller.SystemItemAdapter;
import com.example.wanandroid.javabean.NewsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.javabean.SystemBean;
import com.example.wanandroid.javabean.SystemItemBean;
import com.example.wanandroid.utils.BroadCastUtils;
import com.example.wanandroid.utils.ImgUtils;
import com.example.wanandroid.utils.RetrofitUtils;
import com.example.wanandroid.view.AutoLinefeedLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangchong on 18-3-12.
 */
public class SystemFragment extends Fragment {

    private RecyclerView itemRecyclerView;
    private RecyclerView itemDataRecyclerView;
    private AutoLinefeedLayout autoLinefeedLayout;

    private RetrofitUtils retrofitUtils;
    private List<NewsBean> newsBeanList;
    private FirstPageNewsAdapter firstPageNewsAdapter;
    private SystemItemAdapter systemItemAdapter;
    private SystemBean mainSystemBean;
    private int pageIndex;
    private boolean autoLayoutExpanded;
    private Receiver receiver;
    private IntentFilter intentFilter;
    private int courseId;
    private int page;

    private final String TAG = "SystemFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new Receiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtils.ACTION_NOTIFY_DATA_CHANGED);
        getActivity().registerReceiver(receiver, intentFilter);
        page = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, container, false);
        itemRecyclerView = (RecyclerView)view.findViewById(R.id.rlv_system_item);
        itemDataRecyclerView = (RecyclerView)view.findViewById(R.id.rlv_system_item_data);
        autoLinefeedLayout = (AutoLinefeedLayout)view.findViewById(R.id.auto_layout_subject_item);
        itemRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        itemDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsBeanList = new ArrayList<>();
        firstPageNewsAdapter = new FirstPageNewsAdapter(getActivity(), newsBeanList);
        itemDataRecyclerView.setAdapter(firstPageNewsAdapter);
        firstPageNewsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String loadUrl = newsBeanList.get(position).getLink();
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("load_url", loadUrl);
                getActivity().startActivity(intent);
            }
        });
        itemDataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(firstPageNewsAdapter != null){
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        //判断是当前layoutManager是否为LinearLayoutManager
                        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                        if (layoutManager instanceof LinearLayoutManager) {
                            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                            //获取最后一个可见view的位置
                            int lastItemPosition = linearManager.findLastVisibleItemPosition();
                            Log.d(TAG, "last:"+lastItemPosition+"");
                            if(lastItemPosition == firstPageNewsAdapter.getItemCount()-1){
                                page++;
                                getSystemItemData(page, courseId);
                            }
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofitUtils = MainApplication.retrofitUtils;
        getSystemItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null)
        {
            getActivity().unregisterReceiver(receiver);
        }
    }

    /**
     * 获取体系子项
     */
    public void getSystemItem()
    {
        Observable<SystemBean> observable = retrofitUtils.callSystem();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SystemBean>() {
                    @Override
                    public void accept(final SystemBean systemBean) throws Exception {
                        if(systemBean.getErrorCode() == 0)
                        {
                            mainSystemBean = systemBean;
                            systemBean.setIndex(0);
                            systemItemAdapter = new SystemItemAdapter(getActivity(), systemBean);
                            itemRecyclerView.setAdapter(systemItemAdapter);
                            autoLayoutExpanded = false;
                            pageIndex = 0;
                            courseId = systemBean.getData().get(0).getChildren().get(0).getId();
                            getSystemItemData(0, systemBean.getData().get(0).getChildren().get(0).getId());
                            systemItemAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    courseId = systemBean.getData().get(position).getChildren().get(0).getId();
                                    if(autoLayoutExpanded == true)
                                    {
                                        autoLinefeedLayout.setVisibility(View.INVISIBLE);
                                        autoLinefeedLayout.removeAllViews();
                                        autoLayoutExpanded = false;
                                    }
                                    else if(autoLayoutExpanded == false && position == pageIndex)
                                    {
                                        showAutoLayout(pageIndex);
                                        autoLayoutExpanded = true;
                                    }
                                    else
                                    {
                                        pageIndex = position;
                                        systemBean.setIndex(position);
                                        systemItemAdapter.notifyDataSetChanged();
                                        newsBeanList.clear();
                                        page = 0;
                                        firstPageNewsAdapter.notifyDataSetChanged();
                                        getSystemItemData(0, courseId);
                                    }
                                }
                            });
                        }
                        else
                        {
                            Log.d(TAG, systemBean.getErrorMsg());
                        }
                    }
                });
    }

    /**
     * 获取体系子项对应的新闻
     * @param page 页数
     * @param courseId 子项cid
     */
    public void getSystemItemData(final int page , int courseId)
    {
        Observable<PageNewsBean> observable = retrofitUtils.callSystemItemData(page, courseId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageNewsBean>() {
                    @Override
                    public void accept(final PageNewsBean pageNewsBean) throws Exception {
                        if(pageNewsBean.getErrorCode() == 0)
                        {
                            newsBeanList.addAll(pageNewsBean.getData().getDatas());
                            firstPageNewsAdapter.notifyDataSetChanged();
                            if(page > 0 && pageNewsBean.getData().getDatas().size() > 0){
                                Toast.makeText(getActivity(), R.string.loading_next_page, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Log.d(TAG, pageNewsBean.getErrorMsg());
                        }
                    }
                });
    }

    /**
     * 显示子项的子菜单
     * @param pageIndex 子项的序号
     */
    public void showAutoLayout(final int pageIndex)
    {
        autoLinefeedLayout.setVisibility(View.VISIBLE);
        List<SystemItemBean> systemItemBeanList = mainSystemBean.getData().get(pageIndex).getChildren();
        for(int i=0;i<systemItemBeanList.size();i++)
        {
            final SystemItemBean systemItemBean = systemItemBeanList.get(i);
            Button button = new Button(getActivity());
            button.setText(systemItemBean.getName());
            button.setTextColor(Color.DKGRAY);
            button.setTextSize(14);
            button.setPadding(10, ImgUtils.dip2Pix(getActivity(), 2), 10 ,ImgUtils.dip2Pix(getActivity(), 2));
            button.setBackgroundResource(R.drawable.shape_btn_system_item);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsBeanList.clear();
                    firstPageNewsAdapter.notifyDataSetChanged();
                    page = 0;
                    courseId = systemItemBean.getId();
                    getSystemItemData(page, systemItemBean.getId());
                    autoLinefeedLayout.setVisibility(View.INVISIBLE);
                    autoLinefeedLayout.removeAllViews();
                    autoLayoutExpanded = false;
                }
            });
            autoLinefeedLayout.addView(button, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ImgUtils.dip2Pix(getActivity(), 35)));
        }
    }

    class Receiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "receive action: "+action);
            if(action.equals(BroadCastUtils.ACTION_NOTIFY_DATA_CHANGED))
            {
                getSystemItemData(0, courseId);
            }
        }
    }

}
