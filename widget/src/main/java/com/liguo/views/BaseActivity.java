package com.liguo.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.liguo.application.AppManager;
import com.liguo.application.BaseApplication;
import com.liguo.interfaces.LogUtil;
import com.liguo.util.L;
import com.liguo.util.Util;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


/**
 * Created by Extends on 2016/2/22 0022.
 */
public abstract class BaseActivity extends FragmentActivity implements LogUtil {
    public Subscription rxSubscription;
    private View statusBarView;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config(savedInstanceState);
        ViewGroup contentView = (ViewGroup) this.findViewById(android.R.id.content);
        statusBarView = new View(this);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                BaseApplication.getStatusBarHeight());
        contentView.addView(statusBarView, lp);
        compat();
        AppManager.getInstance().addActivity(this);
        unbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData();
    }



    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        RxBus.unsubscribe(rxSubscription);
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * 配置信息，如setContentView方法可以写在这里
     */
    protected abstract void config(Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 更改状态栏颜色
     * @param statusColor
     */
    public void compat(int statusColor)
    {
        statusBarView.setBackgroundColor(statusColor);
    }
    /**
     * 更改状态栏颜色
     */
    public void compat()
    {
        compat(Util.COLOR_DEFAULT);
    }

    @Override
    public void debug(String msg) {
        L.debug(getClass(),msg);
    }

    @Override
    public void info(String msg) {
        L.info(getClass(),msg);
    }

    @Override
    public void error(String msg) {
        L.error(getClass(),msg);
    }

    @Override
    public void warn(String msg) {
        L.warn(getClass(),msg);
    }
}
