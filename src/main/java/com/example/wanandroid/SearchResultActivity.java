package com.example.wanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wanandroid.contoller.FirstPageNewsAdapter;
import com.example.wanandroid.contoller.ItemClickListener;
import com.example.wanandroid.javabean.NewsBean;
import com.example.wanandroid.javabean.PageNewsBean;
import com.example.wanandroid.utils.BroadCastUtils;
import com.example.wanandroid.utils.RetrofitUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangchong on 18-4-13.
 */
public class SearchResultActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private final String TAG = "SearchResultActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
        initData();
    }

    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultActivity.this.finish();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rlv_search_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initData(){
        String searchWord = getIntent().getStringExtra(BroadCastUtils.EXTRA_SEARCH_WORD);
        RetrofitUtils retrofitUtils = MainApplication.retrofitUtils;
        Observable<PageNewsBean> getSearchResults = retrofitUtils.getSearchResult(0, searchWord);
        getSearchResults.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PageNewsBean>() {
                    @Override
                    public void accept(PageNewsBean pageNewsBean) throws Exception {
                        if(pageNewsBean.getErrorCode() == 0)
                        {
                            Log.d(TAG, "news size: "+pageNewsBean.getData().getDatas().size());
                            final List<NewsBean> newsBeanList = pageNewsBean.getData().getDatas();
                            FirstPageNewsAdapter adapter= new FirstPageNewsAdapter(SearchResultActivity.this, newsBeanList);
                            recyclerView.setAdapter(adapter);
                            adapter.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String loadUrl = newsBeanList.get(position).getLink();
                                    Intent intent = new Intent(SearchResultActivity.this, WebViewActivity.class);
                                    intent.putExtra("load_url", loadUrl);
                                    startActivity(intent);
                                }

                            });
                        }
                        else {
                            Toast.makeText(SearchResultActivity.this, pageNewsBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
