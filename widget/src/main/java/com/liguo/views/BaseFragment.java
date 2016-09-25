package com.liguo.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguo.interfaces.LogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


public abstract class BaseFragment extends Fragment implements LogUtil {
    public boolean DEBUG = true;
    private String TAG = "BaseFragment";
    public View view;
    private Unbinder unbinder;
    public Subscription rxSubscription;

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = config(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView( inflater,  container,  savedInstanceState);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        RxBus.unsubscribe(rxSubscription);
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

    /**
     * 手动刷新数据
     */
    public void updata(String response) {

    }

    /**
     * 登陆或退出刷新数据
     */
    public void logged() {

    }


    /**
     * 传递数据
     *
     * @param list
     * @param keystr
     */
    public void setMessage(List list, String keystr) {

    }

    /**
     * 传递数据
     *
     * @param object
     * @param keystr
     */
    public void setMessage(Object object, String keystr) {

    }

    public void setObject(Object object){

    }

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
