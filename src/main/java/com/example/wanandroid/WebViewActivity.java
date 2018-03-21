package com.example.wanandroid;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by zhangchong on 18-3-13.
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private String loadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }

    public void init()
    {
        loadUrl = getIntent().getStringExtra("load_url");
        webView = (WebView)findViewById(R.id.webview_load_url);
        toolbar = (Toolbar)findViewById(R.id.toolbar_webview);
        progressBar = (ProgressBar)findViewById(R.id.pb_web_loading);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView全屏加载
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebChromeClient(new WebChromeClient()
        {
            /* 获得网页的加载进度 */
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    if(progressBar.getVisibility() == View.INVISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(loadUrl);
        toolbar.setTitle(R.string.webview_title);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginStart(310);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.this.finish();
            }
        });
    }
}
