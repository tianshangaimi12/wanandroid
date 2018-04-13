package com.example.wanandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wanandroid.javabean.SearchWordsBean;
import com.example.wanandroid.utils.BroadCastUtils;
import com.example.wanandroid.utils.ImgUtils;
import com.example.wanandroid.utils.RetrofitUtils;
import com.example.wanandroid.view.EditTextWithDelete;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangchong on 18-4-13.
 */
public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FlexboxLayout autoLinefeedLayout;
    private EditTextWithDelete edtSearch;
    private ImageView imgSearch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        toolbar.setTitle(R.string.search_menu);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginStart(310);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
        autoLinefeedLayout = (FlexboxLayout) findViewById(R.id.auto_layout_search_hot_words);
        edtSearch = (EditTextWithDelete) findViewById(R.id.edt_search_word);
        imgSearch = (ImageView) findViewById(R.id.img_start_search);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = edtSearch.getText().toString();
                if (TextUtils.isEmpty(searchWord)) {
                    Toast.makeText(SearchActivity.this, R.string.search_hint, Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra(BroadCastUtils.EXTRA_SEARCH_WORD, searchWord);
                    startActivity(intent);
                }
            }
        });
        getSearchHotWords();
    }

    public void getSearchHotWords() {
        RetrofitUtils retrofitUtils = MainApplication.retrofitUtils;
        Observable<SearchWordsBean> getWords = retrofitUtils.getSearchWords();
        getWords.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchWordsBean>() {
                    @Override
                    public void accept(SearchWordsBean searchWordsBean) throws Exception {
                        if (searchWordsBean.getErrorCode() == 0) {
                            List<SearchWordsBean.SearchWordBean> searchWordBeanList = searchWordsBean.getData();
                            for (int i = 0; i < searchWordBeanList.size(); i++) {
                                final SearchWordsBean.SearchWordBean searchWordBean = searchWordBeanList.get(i);
                                Button button = new Button(SearchActivity.this);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                int margin = ImgUtils.dip2Pix(SearchActivity.this, 8);
                                layoutParams.setMargins(margin, margin, margin, margin);
                                button.setText(searchWordBean.getName());
                                //button.setBackgroundResource(R.drawable.shape_search_hot_word);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        edtSearch.setText(searchWordBean.getName());
                                    }
                                });
                                autoLinefeedLayout.addView(button, layoutParams);
                            }
                        } else {
                            Toast.makeText(SearchActivity.this, searchWordsBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
