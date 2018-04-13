package com.example.wanandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wanandroid.contoller.CollectItemClickListener;
import com.example.wanandroid.contoller.CollectPageAdapter;
import com.example.wanandroid.javabean.CollectListBean;
import com.example.wanandroid.javabean.CollectNewsBean;
import com.example.wanandroid.utils.RetrofitUtils;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangchong on 18-4-3.
 */
public class CollectActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private final String TAG = "CollectActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        toolbar = (Toolbar)findViewById(R.id.toolbar_collect);
        toolbar.setTitle(R.string.my_collected);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleMarginStart(270);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectActivity.this.finish();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rlv_collect_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCollectNews(0);
    }

    public void getCollectNews(int page)
    {
        final RetrofitUtils retrofitUtils = MainApplication.retrofitUtils;
        Observable<CollectListBean> collectListBeanCall = retrofitUtils.getCollectListNews(page);
        collectListBeanCall.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CollectListBean>() {
                    @Override
                    public void accept(final CollectListBean collectListBean) throws Exception {
                        if(collectListBean.getErrorCode() == 0)
                        {
                            Log.d(TAG, "news size: "+collectListBean.getData().getDatas().size());
                            final List<CollectNewsBean> collectNewsBeanList = collectListBean.getData().getDatas();
                            final CollectPageAdapter adapter = new CollectPageAdapter(CollectActivity.this, collectNewsBeanList);
                            recyclerView.setAdapter(adapter);
                            adapter.setItemClickListener(new CollectItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String loadUrl = collectNewsBeanList.get(position).getLink();
                                    Intent intent = new Intent(CollectActivity.this, WebViewActivity.class);
                                    intent.putExtra("load_url", loadUrl);
                                    startActivity(intent);
                                }

                                @Override
                                public void onImgClick(View view, final int position) {
                                    CollectNewsBean collectNewsBean = collectNewsBeanList.get(position);
                                    int originId = collectNewsBean.getOriginId() == 0?-1:collectNewsBean.getOriginId();
                                    Call<ResponseBody> disCollectInCollect = retrofitUtils.disCollectInCollect(
                                            collectNewsBean.getId(), originId
                                    );
                                    disCollectInCollect.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            try {
                                                String responseString = response.body().string();
                                                Log.d(TAG, "response:"+responseString);
                                                JSONObject jsonObject = new JSONObject(responseString);
                                                int errorCode = jsonObject.optInt("errorCode");
                                                String errorMsg = jsonObject.optString("errorMsg");
                                                if(errorCode == 0)
                                                {
                                                    Toast.makeText(CollectActivity.this, R.string.toast_disCollect_success, Toast.LENGTH_SHORT).show();
                                                    collectNewsBeanList.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                else
                                                {
                                                    Toast.makeText(CollectActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d(TAG, "error:"+t.getMessage());
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            Log.d(TAG, collectListBean.getErrorMsg());
                            Toast.makeText(CollectActivity.this, collectListBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
