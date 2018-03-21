package com.example.wanandroid.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.example.wanandroid.MainActivity;
import com.example.wanandroid.R;
import com.example.wanandroid.WebViewActivity;
import com.example.wanandroid.contoller.FirstPageNewsAdapter;
import com.example.wanandroid.contoller.ItemClickListener;
import com.example.wanandroid.contoller.SystemItemAdapter;
import com.example.wanandroid.javabean.NewsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.javabean.SystemBean;
import com.example.wanandroid.javabean.SystemItemBean;
import com.example.wanandroid.utils.ImgUtils;
import com.example.wanandroid.utils.RetrofitUtils;
import com.example.wanandroid.view.AutoLinefeedLayout;

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

    private final String TAG = "SystemFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, container, false);
        itemRecyclerView = (RecyclerView)view.findViewById(R.id.rlv_system_item);
        itemDataRecyclerView = (RecyclerView)view.findViewById(R.id.rlv_system_item_data);
        autoLinefeedLayout = (AutoLinefeedLayout)view.findViewById(R.id.auto_layout_subject_item);
        itemRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        itemDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        retrofitUtils = activity.retrofitUtils;
        getSystemItem();
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
                            getSystemItemData(0, systemBean.getData().get(0).getChildren().get(0).getId());
                            systemItemAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
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
                                        firstPageNewsAdapter.notifyDataSetChanged();
                                        getSystemItemData(0, systemBean.getData().get(position).getChildren().get(0).getId());
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
    public void getSystemItemData(int page , int courseId)
    {
        Observable<PageNewsBean> observable = retrofitUtils.callSystemItemData(page, courseId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageNewsBean>() {
                    @Override
                    public void accept(final PageNewsBean pageNewsBean) throws Exception {
                        if(pageNewsBean.getErrorCode() == 0)
                        {
                            newsBeanList = pageNewsBean.getData().getDatas();
                            firstPageNewsAdapter = new FirstPageNewsAdapter(getActivity(), newsBeanList);
                            itemDataRecyclerView.setAdapter(firstPageNewsAdapter);
                            firstPageNewsAdapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String loadUrl = pageNewsBean.getData().getDatas().get(position).getLink();
                                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                    intent.putExtra("load_url", loadUrl);
                                    getActivity().startActivity(intent);
                                }
                            });
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
            button.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_btn_system_item));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsBeanList.clear();
                    firstPageNewsAdapter.notifyDataSetChanged();
                    getSystemItemData(0, systemItemBean.getId());
                    autoLinefeedLayout.setVisibility(View.INVISIBLE);
                    autoLinefeedLayout.removeAllViews();
                    autoLayoutExpanded = false;
                }
            });
            autoLinefeedLayout.addView(button, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ImgUtils.dip2Pix(getActivity(), 35)));
        }
    }

}
