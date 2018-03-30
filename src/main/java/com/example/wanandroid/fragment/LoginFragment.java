package com.example.wanandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanandroid.MainActivity;
import com.example.wanandroid.R;
import com.example.wanandroid.utils.RetrofitUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by zhangchong on 18-3-26.
 */
public class LoginFragment extends DialogFragment {

    private TextView tvTitle;
    private Button btnLogin;
    private LinearLayout linearLayout;
    private EditText edtUserName;
    private EditText edtPwd;
    private EditText edtRePwd;

    private int type;

    private final String TAG = "LoginFragment";
    private final int TYPE_REGISTER = 0;
    private final int TYPE_LOGIN = 1;
    private final String BASE_URL = "http://www.wanandroid.com/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        tvTitle = (TextView)view.findViewById(R.id.txt_login_dialog_title);
        linearLayout = (LinearLayout)view.findViewById(R.id.ll_login_fragment_commit_pwd);
        edtUserName = (EditText)view.findViewById(R.id.edt_login_username);
        edtPwd = (EditText)view.findViewById(R.id.edt_login_pwd);
        edtRePwd = (EditText)view.findViewById(R.id.edt_login_re_pwd);
        btnLogin = (Button)view.findViewById(R.id.btn_login_send);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyCommit();
            }
        });
        initView();
        return view;
    }

    /**
     *
     * @param type 0表示注册，1表示登录
     */
    public void setMode(int type)
    {
        this.type = type;

    }

    public void initView()
    {
        if(type == TYPE_REGISTER)
        {
            tvTitle.setText("注册");
            linearLayout.setVisibility(View.VISIBLE);
        }
        else if(type == TYPE_LOGIN)
        {
            tvTitle.setText("登录");
            linearLayout.setVisibility(View.GONE);
        }
    }

    public void applyCommit()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        RetrofitUtils retrofitUtils = retrofit.create(RetrofitUtils.class);
        if(type == TYPE_LOGIN)
        {
            final String username = edtUserName.getText().toString();
            String password = edtPwd.getText().toString();
            Call<ResponseBody> login = retrofitUtils.loginCall(username, password);
            login.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "response:"+responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        int errorCode = jsonObject.optInt("errorCode");
                        if(errorCode == 0)
                        {
                            getDialog().dismiss();
                            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity)getActivity();
                            activity.setLoginUserName(username);
                        }
                        else
                        {
                            String errorMsg = jsonObject.optString("errorMsg");
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(),"error:"+ t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(type == TYPE_REGISTER)
        {
            String username = edtUserName.getText().toString();
            String password = edtPwd.getText().toString();
            String repassword = edtRePwd.getText().toString();
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            params.put("repassword", repassword);
            Call<ResponseBody> register = retrofitUtils.registerCall(params);
            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "response:"+responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        int errorCode = jsonObject.optInt("errorCode");
                        if(errorCode == 0)
                        {
                            getDialog().dismiss();
                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String errorMsg = jsonObject.optString("errorMsg");
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), "error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
