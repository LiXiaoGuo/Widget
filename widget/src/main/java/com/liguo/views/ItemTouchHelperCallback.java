package com.liguo.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by Extends on 2016/2/19 0019.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback{
    public static final String TAG = "ItemTouchHelperCallback";
    private final ItemTouchHelperAdapter mAdapter;
    
    public ItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        //默认上下拖动，左右删除，当getLayoutMananger为LinearLayoutMananger时适用
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        //判断当getLayoutMananger不为LinearLayoutMananger时，且getSpanCount不等于1时，则左右上下拖动，不支持滑动删除
        //若getSpanCount等于1，则等同于LinaearLayoutMananger
        if((mAdapter instanceof BaseRecyclerAdapter)&& ((BaseRecyclerAdapter) mAdapter).getHeaderView()!=null && viewHolder.getAdapterPosition() == 0){
            dragFlags = 0;
            swipeFlags = 0;
        }else{
            if(lm instanceof GridLayoutManager){
                if(((GridLayoutManager) lm).getSpanCount() != 1){
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
                    swipeFlags = 0;
                }
            }else if(lm instanceof StaggeredGridLayoutManager){
                if(((StaggeredGridLayoutManager) lm).getSpanCount() != 1){
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
                    swipeFlags = 0;
                }
            }
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        Log.e(TAG,viewHolder.getAdapterPosition()+";;;"+target.getAdapterPosition());
        if((mAdapter instanceof BaseRecyclerAdapter)&& ((BaseRecyclerAdapter) mAdapter).getHeaderView()!=null){
            if(viewHolder.getAdapterPosition() == 0 || target.getAdapterPosition() == 0){
                return true;
            }
        }

        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG,viewHolder.getAdapterPosition()+"");
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public interface ItemTouchHelperAdapter {
        void onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
}
