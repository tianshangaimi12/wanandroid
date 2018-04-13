package com.example.wanandroid.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanandroid.MainApplication;
import com.example.wanandroid.R;
import com.example.wanandroid.WebViewActivity;
import com.example.wanandroid.javabean.FamousWebsBean;
import com.example.wanandroid.utils.ImgUtils;
import com.example.wanandroid.utils.RetrofitUtils;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangchong on 18-3-12.
 */
public class NavigationFragment extends Fragment {

    private LinearLayout linearLayout;

    private List<FamousWebsBean.WebBean> webBeens;
    private RetrofitUtils retrofitUtils;
    private int textSize;
    private int marginTop;

    private final String TAG = "NavigationFragment";
    private final int[] TV_COLOR = {Color.RED, Color.BLUE, Color.parseColor("#FFD700"), Color.GREEN, Color.DKGRAY
    , Color.BLACK, Color.MAGENTA, Color.parseColor("#EE82EE"), Color.parseColor("#AEEEEE"), Color.parseColor("#8A2BE2")};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textSize = ImgUtils.dip2Pix(getActivity(), 20);
        marginTop = ImgUtils.dip2Pix(getActivity(), 4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        linearLayout = (LinearLayout)view.findViewById(R.id.ll_fragment_navigation_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofitUtils = MainApplication.retrofitUtils;
        getFamousWebs();
    }

    /**
     * 获取常用网站
     */
    public void getFamousWebs()
    {
        Observable<FamousWebsBean> observable = retrofitUtils.callFamousWebs();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FamousWebsBean>() {
                    @Override
                    public void accept(FamousWebsBean famousWebsBean) throws Exception {
                        if(famousWebsBean.getErrorCode() == 0)
                        {
                            webBeens = famousWebsBean.getData();
                            generateTextView();
                        }
                        else Log.d(TAG, famousWebsBean.getErrorMsg());
                    }
                });
    }

    public void generateTextView()
    {
        for(int i=0;i<webBeens.size();i++)
        {
            final FamousWebsBean.WebBean webBean = webBeens.get(i);
            TextView textView = new TextView(getActivity());
            textView.setText(webBean.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loadUrl = webBean.getLink();
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("load_url", loadUrl);
                    getActivity().startActivity(intent);
                }
            });
            Random random = new Random();
            int number = random.nextInt(10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, marginTop, 0, marginTop);
            textView.setMaxLines(1);
            textView.setTextSize(20);
            textView.setTextColor(TV_COLOR[number]);
            linearLayout.addView(textView, params);
        }
    }
}
