package com.liguo.views;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.liguo.interfaces.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * *    ┏┓　　　┏┓
 * *  ┏┛┻━━━┛┻┓
 * *  ┃　　　　　　　┃
 * *  ┃　　　━　　　┃
 * *  ┃　┳┛　┗┳　┃
 * *  ┃　　　　　　　┃
 * *  ┃　　　┻　　　┃
 * *  ┃　　　　　　　┃
 * *  ┗━┓　　　┏━┛
 * *      ┃　　　┃  神兽保佑
 * *      ┃　　　┃  代码无BUG！
 * *      ┃　　　┗━━━┓
 * *      ┃　　　　　　　┣┓
 * *      ┃　　　　　　　┏┛
 * *      ┗┓┓┏━┳┓┏┛
 * *        ┃┫┫　┃┫┫
 * *        ┗┻┛　┗┻┛
 * * Created by Extends on 2016/8/15 0015.
 */
public abstract class BaseDialogFragment extends DialogFragment implements LogUtil {
    public boolean DEBUG = true;
    private String TAG = "BaseDialogFragment";
    private View view;
    private Unbinder unbinder;
    public Window window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        window = getDialog().getWindow();
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        view = config(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        //去掉标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置背景透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView( inflater,  container,  savedInstanceState);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    /**
     * 配置信息，如setContentView方法可以写在这里
     */
    protected abstract View config(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    protected abstract void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void debug(String msg) {
        if(DEBUG){
            Log.d(TAG, msg);
        }
    }

    @Override
    public void info(String msg) {
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }

    @Override
    public void error(String msg) {
        if(DEBUG){
            Log.e(TAG, msg);
        }
    }

    @Override
    public void warn(String msg) {
        if(DEBUG){
            Log.w(TAG, msg);
        }
    }
}
