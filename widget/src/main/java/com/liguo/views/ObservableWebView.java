package com.liguo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

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
 *
 * 添加了滑动监听的WebView
 * * Created by Extends on 2016/4/1 0001.
 */
public class ObservableWebView extends WebView {

    private ScrollViewListener scrollViewListener = null;

    public ObservableWebView(Context context) {
        this(context,null);
    }

    public ObservableWebView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public void setScrollListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollViewListener != null) {
            scrollViewListener.onOverScrolled(this, scrollX,scrollY,clampedX,clampedY);
        }
    }


    public static abstract class ScrollViewListener {
        /**
         * 当滑动事件改变时
         * @param webView
         * @param x 变化后的X轴位置
         * @param y 变化后的Y轴位置
         * @param oldx 原先的X轴的位置
         * @param oldy 原先的Y轴的位置
         */
        public void onScrollChanged(ObservableWebView webView, int x, int y, int oldx, int oldy){}

        /**
         *
         * @param webView
         * @param scrollX 距离远点的X轴的距离
         * @param scrollY 距离远点的Y轴的距离
         * @param clampedX 当ScrollView滑动到左侧边界的时候值为true
         * @param clampedY 当ScrollView滑动到下边界的时候值为true
         */
        public void onOverScrolled(ObservableWebView webView, int scrollX, int scrollY, boolean clampedX, boolean clampedY){}
    }

}
