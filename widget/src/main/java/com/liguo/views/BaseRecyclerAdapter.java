package com.liguo.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
 * * Created by Extends on 2016/3/28 0028.
 */
public abstract class BaseRecyclerAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_HEADER = 0;//头部
    public static final int TYPE_NORMAL = 1;//身部
    public static final int TYPE_FOOTER = 2;//底部

    private List<T> mDatas = new ArrayList<>();

    private View mHeaderView;   //头部View
    private View mFooterView;   //底部View

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    /**
     * 设置头部控件
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * 设置底部控件
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(0);
    }

    /**
     * 获取头部控件
     * @return
     */
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 获取底部控件
     * @return
     */
    public View getFooterView() {
        return mHeaderView;
    }

    /**
     * 添加数据
     * @param datas
     */
    public void addDatas(List<T> datas) {
//        mDatas.addAll(datas);
//        notifyDataSetChanged();
        mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 获取Item类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
//        Log.e("tag",position+";;;"+(mHeaderView == null)+";;;"+(mFooterView == null)+";;;"+getItemCount());
        if(position == 0){
            if(mHeaderView == null){
                if(mFooterView != null && getItemCount() == 1)
                    return TYPE_FOOTER;
                return TYPE_NORMAL;
            }
            return TYPE_HEADER;
        }else if(position == getItemCount()-1){
            if(mFooterView == null) return TYPE_NORMAL;
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public VH onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return (VH) new Holder(mHeaderView);
        if(mFooterView != null && viewType == TYPE_FOOTER) return (VH) new Holder(mFooterView);
        return onCreate(parent, viewType);
    }

    /**
     * 绑定ViewHolder
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if(getItemViewType(position) != TYPE_NORMAL) return;

        final int pos = getRealPosition(viewHolder);
        final T data = mDatas.get(pos);
        if((mHeaderView == null && mFooterView != null) || (mHeaderView != null && mFooterView == null)){

        }else if(mHeaderView != null && mFooterView != null){

        }else{

        }
        onBind(viewHolder, pos, data);

        if(mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if((getItemViewType(position) == TYPE_FOOTER) || (getItemViewType(position) == TYPE_HEADER)) return gridManager.getSpanCount();
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        /**
         * 有莫名其妙的错误，解决不了，所以暂不支持瀑布流
         * Attempt to read from field 'int android.support.v7.widget.StaggeredGridLayoutManager$Span.mIndex' on a null object reference
         at android.support.v7.widget.StaggeredGridLayoutManager.hasGapsToFix(StaggeredGridLayoutManager.java:319)
         at android.support.v7.widget.StaggeredGridLayoutManager.checkForGaps(StaggeredGridLayoutManager.java:247)
         at android.support.v7.widget.StaggeredGridLayoutManager.onScrollStateChanged(StaggeredGridLayoutManager.java:282)
         at android.support.v7.widget.RecyclerView.dispatchOnScrollStateChanged(RecyclerView.java:3832)
         at android.support.v7.widget.RecyclerView.setScrollState(RecyclerView.java:1141)
         at android.support.v7.widget.RecyclerView.access$3200(RecyclerView.java:141)
         at android.support.v7.widget.RecyclerView$ViewFlinger.run(RecyclerView.java:3996)
         at android.view.Choreographer$CallbackRecord.run(Choreographer.java:800)
         at android.view.Choreographer.doCallbacks(Choreographer.java:603)
         at android.view.Choreographer.doFrame(Choreographer.java:571)
         at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:786)
         at android.os.Handler.handleCallback(Handler.java:815)
         at android.os.Handler.dispatchMessage(Handler.java:104)
         at android.os.Looper.loop(Looper.java:194)
         at android.app.ActivityThread.main(ActivityThread.java:5739)
         at java.lang.reflect.Method.invoke(Native Method)
         at java.lang.reflect.Method.invoke(Method.java:372)
         at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1039)
         at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:834)
         */
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if(lp != null
//                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//            p.setFullSpan(holder.getLayoutPosition() == 0);
//        }
    }

    /**
     * 获取真实的position
     * @param holder
     * @return
     */
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    /**
     * 获得Item数量
     * @return
     */
    @Override
    public int getItemCount() {
        if((mHeaderView == null && mFooterView != null) || (mHeaderView != null && mFooterView == null)){
            return mDatas.size() + 1;
        }else if(mHeaderView != null && mFooterView != null){
            return mDatas.size()+2;
        }
        return mDatas.size();
    }

    /**
     * 获得Item数量
     * @return
     */
    public int getRealItemCount() {
        return mDatas.size();
    }

    public abstract VH onCreate(ViewGroup parent, final int viewType);
    public abstract void onBind(VH viewHolder, int realPosition, T data);

    public class Holder<VH> extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

}