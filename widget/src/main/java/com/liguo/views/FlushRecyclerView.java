package com.liguo.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

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
 *  自定义的Recycler，可获取顶部、底部位置监听
 * * Created by Extends on 2016/5/3 0003.
 */
public class FlushRecyclerView extends RecyclerView {
    private LayoutManager manager;
    private FlushListener flushListener;
    private View firstVisiableChildView;
    private int last = 0, frist;
    private int count = 0;
    private boolean isFlag = false;

    public FlushRecyclerView(Context context) {
        this(context, null);
    }

    public FlushRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlushRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setIsFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(flushListener!=null){
                    flushListener.onScroll(recyclerView,dx,dy,getScollYDistance());
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!isFlag) {
                    if (manager == null) {
                        manager = getLayoutManager();
                    }
                } else {
                    manager = getLayoutManager();
                }
                if (manager != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        count = manager.getItemCount();
                        if (manager instanceof LinearLayoutManager) {
                            last = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                            frist = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
                        } else if (manager instanceof GridLayoutManager) {
                            last = ((GridLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                            frist = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
                        }

                        if (flushListener != null && last == count - 1) {
                            flushListener.onBottom();
                        }

                        if (flushListener != null && frist == 0) {
                            flushListener.onTop(true);
                        } else if(flushListener != null){
                            flushListener.onTop(false);
                        }

                    }

                }
            }
        });
    }

    /**
     * 返回滑动的高度
     * @return
     */
    public int getScollYDistance() {
        if (manager == null) {
            manager = getLayoutManager();
        }
        if(manager instanceof LinearLayoutManager){
            int position = ((LinearLayoutManager)manager).findFirstVisibleItemPosition();
            firstVisiableChildView = manager.findViewByPosition(position);
            int itemHeight = firstVisiableChildView.getHeight();
            return (position) * itemHeight - firstVisiableChildView.getTop();
        }else{
            return 0;
        }

    }


    public void setOnFlushListener(FlushListener flushListener) {
        this.flushListener = flushListener;
    }


    public static abstract class FlushListener {
        public void onBottom() {}

        public void onTop(boolean b) {}

        /**
         * 未实现的方法
         * @param frist
         */
        @Deprecated
        public void onLeft(int frist) {}

        /**
         * 滑动监听
         * @param recyclerView
         * @param dx
         * @param dy
         * @param scollY 滑动的绝对距离 （只有LayoutManager为LinerLayout时有效）
         */
        public void onScroll(RecyclerView recyclerView, int dx, int dy , int scollY) {}
    }
}
