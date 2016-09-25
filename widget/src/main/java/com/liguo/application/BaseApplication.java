package com.liguo.application;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.Map;

/**
 * @author 彭凡
 * @version 1.0
 * @date 创建时间：2015-10-9 下午3:23:24
 */
public class BaseApplication extends Application {
    private static Context context;
    private static int statusBarHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    public static Context getContext() {
        return context;
    }
    /**
     * 获得状态栏高度
     * @return
     */
    public static int getStatusBarHeight()
    {
        if(statusBarHeight == 0){
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                try {
                    statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                }catch (Exception e){
                    statusBarHeight = 0;
                }
            }
        }
        return statusBarHeight;
    }

}
