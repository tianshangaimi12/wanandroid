package com.example.wanandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zhangchong on 18-3-13.
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;

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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setInitialScale(150);
        //webView全屏加载
        //webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(loadUrl);
        toolbar = (Toolbar)findViewById(R.id.toolbar_webview);
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
