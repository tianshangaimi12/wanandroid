package com.example.wanandroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanandroid.CollectActivity;
import com.example.wanandroid.R;
import com.example.wanandroid.view.CircleCrop;

/**
 * Created by zhangchong on 18-3-22.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{

    private ImageView imageView;
    private TextView txtUser;
    private Button btnRegister;
    private Button btnLogin;
    private LinearLayout btnShowCollected;
    private LinearLayout btnExitLogin;
    private LinearLayout btnAboutApp;
    private LinearLayout btnExitApp;

    private final String ABOUT_IMG_URL = "http://www.enterdesk.com/uploadfile/2014/0227/20140227113508372.jpg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        return view;
    }

    public void init(View view)
    {
        imageView = (ImageView)view.findViewById(R.id.img_setting_user);
        Glide.with(getActivity())
                .load(R.drawable.user)
                .transform(new CircleCrop(getActivity()))
                .into(imageView);
        txtUser = (TextView)view.findViewById(R.id.txt_login_user);
        btnRegister = (Button)view.findViewById(R.id.btn_register);
        btnLogin = (Button)view.findViewById(R.id.btn_login);
        btnShowCollected = (LinearLayout)view.findViewById(R.id.ll_setting_item_collected);
        btnExitLogin = (LinearLayout)view.findViewById(R.id.ll_setting_item_exit_login);
        btnAboutApp = (LinearLayout)view.findViewById(R.id.ll_setting_item_about);
        btnExitApp = (LinearLayout)view.findViewById(R.id.ll_setting_item_exit_app);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnShowCollected.setOnClickListener(this);
        btnExitLogin.setOnClickListener(this);
        btnAboutApp.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_register:
                LoginFragment registerFragment = new LoginFragment();
                registerFragment.show(getFragmentManager(), "LoginFragment");
                registerFragment.setMode(0);
                break;
            case R.id.btn_login:
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setMode(1);
                loginFragment.show(getFragmentManager(), "LoginFragment");
                break;
            case R.id.ll_setting_item_exit_app:
                getActivity().finish();
                break;
            case R.id.ll_setting_item_collected:
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_setting_item_about:
                showAboutWindow();
                break;
            case R.id.ll_setting_item_exit_login:
                txtUser.setText("");
                break;
            default:
                break;
        }
    }

    public void setUserName(String userName)
    {
        txtUser.setText(userName);
    }

    public void showAboutWindow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null,false);
        builder.create();
        final Dialog dialog = builder.show();
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 700;
        params.height = 600 ;
        ImageView imageView = (ImageView)view.findViewById(R.id.img_dialog_about);
        Glide.with(getActivity())
                .load(R.drawable.user)
                .placeholder(R.drawable.ic_about)
                .fitCenter()
                .transform(new CircleCrop(getActivity()))
                .into(imageView);
        TextView textView = (TextView)view.findViewById(R.id.txt_dialog_about);
        textView.setText("CREATED"+"\n"+"BY"+"\n"+"Z&C");
        TextView button = (TextView)view.findViewById(R.id.btn_dialog_about);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
