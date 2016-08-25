package com.liguo.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;


/**
 * 导航栏控件
 * Created by Extends on 2016/2/22 0022.
 */
public class NavigationView extends HorizontalScrollView {

    static final String LOG_TAG = "NavigationView";
    private Context parms;

    /**
     * 控件中的子布局
     */
    private LinearLayout[] linearLayouts;

    /**
     * 子布局中的文本控件
     */
    private TextView[] mTextViewChild;

    /**
     * 文本控件的文字集合，通过资源id获得
     */
    private CharSequence[] mTextViewChildTextFromResource;

    /**
     * 文本控件的文字集合，用过String获得
     */
    private String[] mTextViewChildTextFromString;

    /**
     * 控件的子布局数，也就是Item数
     * 默认为0
     */
    private int navigationBitNumber = 0;

    /**
     * 上一次选中的Item项
     */
    private int oldelectNavigationBit = 0;

    /**
     * 当前选中的Item项
     */
    private int selectNavigationBit = 0;

    /**
     * 选中时的文本颜色
     */
    private int selectTextColor = 0xffff0000;

    /**
     * 选中时的背景颜色
     */
    private int selectBackColor = 0x00000000;

    /**
     * 选中时文本显示图片的资源集合
     */
    private int[] selectTextViewChildImageFromResource;

    /**
     * 文字大小
     */
    private float textSize = 42.0F;

    /**
     * 选中时文字大小，默认不改变
     */
    private float selecttTextSize = 0;

    /**
     * 未选中时文本的颜色
     */
    private int unSelectTextColor = 0xff000000;

    /**
     * 选中时的背景颜色
     */
    private int unSelectBackColor = 0x00000000;

    /**
     * 未选中时文本显示图片的资源集合
     */
    private int[] unselectTextViewChildImageFromResource;

    /**
     * 最左边的Drawable
     */
    private Drawable leftDrawable = new GradientDrawable();

    /**
     * 中间的Drawable
     */
    private Drawable middleDrawable =  new GradientDrawable();

    /**
     * 最右边的Drawable
     */
    private Drawable rightDrawable =  new GradientDrawable();

    /**
     * 绑定的ViewPager
     */
    private ViewPager viewPager;

    /**
     * 滑动条当前位置
     */
    private int mCurrentNum;

    /**
     * 控件宽度，单位是px
     */
    private float mWidth;

    /**
     * 一段滑动条的宽度
     */
    private float mTabWidth;

    /**
     * TextView的宽度
     */
    private float[] mTextWidth;

    /**
     * 滑动条的偏移量
     */
    private float mOffset;

    /**
     * 滑动条的中间值
     */
    private float middle;

    /**
     * 滑动条的画笔
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 滑动条的颜色
     */
    private int scllorColor = 0xffff0000;

    /**
     * 滑动条的高度，单位为px
     * 默认为3px
     */
    private float scllorHeight = 3;

    /**
     * 一行有几列，默认为-1 表示一行铺满所有列
     */
    private int columnCount = -1;

    /**
     * 每列的宽度，如果columnCount为-1，则columnW为0
     */
    private float columnW = 0;

    /**
     * 列的偏移量
     */
    private int columnOffset = 0;

    /**
     * 滑动条位置，默认不显示滑动条
     */
    private int slidingStripPosition = NONE;

    @IntDef({TOP, BOTTOM,NONE})
    public @interface SlidingStripPosition {}

    private float padding = 0;

    /**
     * 图片位置，默认显示在文字上边
     */
    private int iconPosition = TOP;

    @IntDef({TOP,RIGHT,BOTTOM,LEFT})
    public @interface IconPosition {}

    /**
     * 滑动条的宽度
     */
    private int slidingStripSize = MATCH_PARENT;

    @IntDef({MATCH_PARENT,WRAP_CONTENT})
    public @interface SlidingStripSize {}


    public static final int NONE = -1;
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private HashMap<Integer,Boolean> iatc = new HashMap<Integer,Boolean>();

    private OnItemChangeListener listener;


    private LinearLayout linearLayout;


    public NavigationView(Context paramContext)
    {
        this(paramContext, null);
    }

    public NavigationView(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public NavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        this.parms = paramContext;
//        setBackgroundColor(Color.BLUE);
        setFillViewport(true);
        //初始化控件的属性
        initFromAttributes(paramContext, paramAttributeSet, paramInt, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
        super(paramContext, paramAttributeSet, paramInt1, paramInt2);
        this.parms = paramContext;
//        setBackgroundColor(Color.BLUE);
        setFillViewport(true);
        //初始化控件的属性
        initFromAttributes(paramContext, paramAttributeSet, paramInt1, paramInt2);
    }

    /**
     * 初始化控件的属性
     * 从xml中获取
     * @param paramContext
     * @param paramAttributeSet
     * @param paramInt1
     * @param paramInt2
     */
    private void initFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
//        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NavigationView, paramInt1, paramInt2);
//        try
//        {
//            //获得文本资源集合
//            this.mTextViewChildTextFromResource = localTypedArray.getTextArray(R.styleable.NavigationView_mTextViewChildTextFromResource);
//            //获得控件Item数量
//            this.navigationBitNumber = this.mTextViewChildTextFromResource == null?0:this.mTextViewChildTextFromResource.length;
//            //初始化mTextViewChildTextFromString
//            this.mTextViewChildTextFromString = new String[this.navigationBitNumber];
//            for(int i = 0;i<this.navigationBitNumber;i++){
//                this.mTextViewChildTextFromString[i] = this.mTextViewChildTextFromResource[i].toString();
//            }
//            //初始化TextView集合
//            this.mTextViewChild = new TextView[this.navigationBitNumber];
//            //初始化布局集合
//            this.linearLayouts = new LinearLayout[this.navigationBitNumber];
//            //获得未选中时文本控件的颜色
//            this.unSelectTextColor = localTypedArray.getResourceId(R.styleable.NavigationView_unSelectTextColor,this.unSelectTextColor)==this.unSelectTextColor?
//                    localTypedArray.getColor(R.styleable.NavigationView_unSelectTextColor,this.unSelectTextColor):this.unSelectTextColor;
//            //获得选中时文本控件的颜色
//            this.selectTextColor = localTypedArray.getResourceId(R.styleable.NavigationView_selectTextColor,this.selectTextColor)==this.selectTextColor?
//                    localTypedArray.getColor(R.styleable.NavigationView_selectTextColor,this.selectTextColor):this.selectTextColor;
//            //获得文字大小
//            this.textSize = localTypedArray.getDimension(R.styleable.NavigationView_textSize, 14.0F);
//            initView(paramContext);
//        }finally{
//            localTypedArray.recycle();
//        }
    }

    /**
     * 初始化控件
     * @param paramContext
     */
    private void initView(Context paramContext){
        removeAllViews();
        linearLayout = new LinearLayout(paramContext);
        addView(linearLayout);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //设置控件中的布局为横向布局
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //设置控件内部的对其方式为Gravity.CENTER_VERTICAL
        linearLayout.setGravity(16);
//        linearLayout.setBackgroundColor(Color.RED);

        TextView localTextView;
        linearLayout.removeAllViewsInLayout();//移除重复布局
        LinearLayout.LayoutParams localLayoutParams = null;
        if(columnCount == -1){
            localLayoutParams = new LinearLayout.LayoutParams(0, -1,1.0F);
        }else{
            columnW = getResources().getDisplayMetrics().widthPixels*1.0f/columnCount;
            localLayoutParams = new LinearLayout.LayoutParams((int)columnW, -1);
        }
        for(int i = 0;i<this.navigationBitNumber;i++){
            //设置localLayoutParams宽为权重1，高为填充整个窗体

            //初始化每一个子布局
            LinearLayout localLinearLayout = new LinearLayout(paramContext);
            localLinearLayout.setLayoutParams(localLayoutParams);
            localLinearLayout.setGravity(Gravity.CENTER);
            //初始化每一个子布局中的文本控件
            localTextView = new TextView(paramContext);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setCompoundDrawablePadding((int)padding);
            //判断当前文本是否为空字串
            if(this.mTextViewChildTextFromString[i].equals("")){
                localTextView.setTextSize(0.0F);
            }else{
                localTextView.setText(this.mTextViewChildTextFromString[i]);
                localTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,this.textSize);
            }
            if(navigationBitNumber > 1){
                if(i==0){
                    localLinearLayout.setBackground(leftDrawable);
                }else if(i==navigationBitNumber-1){
                    localLinearLayout.setBackground(rightDrawable);
                }else{
                    localLinearLayout.setBackground(middleDrawable);
                }
            }

            //判断当前TextView是否是默认选中项
            if(i!=this.selectNavigationBit){
                localTextView.setTextColor(this.unSelectTextColor);
                ((GradientDrawable)localLinearLayout.getBackground()).setColor(unSelectBackColor);
            }else{
                localTextView.setTextColor(this.selectTextColor);
                ((GradientDrawable)localLinearLayout.getBackground()).setColor(selectBackColor);
            }

            this.mTextViewChild[i] = localTextView;
            localLinearLayout.addView(this.mTextViewChild[i]);
            localLinearLayout.setOnClickListener(this.clickListener);
            this.linearLayouts[i] = localLinearLayout;
            linearLayout.addView(this.linearLayouts[i]);
        }
        setCurrentNum(selectNavigationBit);
        setWillNotDraw(false);
        invalidate();
    }

    /**
     * 设置文本控件选中时的图片资源
     * @param paramArrayOfInt
     */
    public NavigationView setSelectTextViewChildImageFromResource(int[] paramArrayOfInt)
    {
        this.selectTextViewChildImageFromResource = paramArrayOfInt;
        return this;
    }

    /**
     * 设置文本控件没有选中时的图片资源
     * @param paramArrayOfInt
     */
    public NavigationView setUnselectTextViewChildImageFromResource(int[] paramArrayOfInt)
    {
        this.unselectTextViewChildImageFromResource = paramArrayOfInt;
        return this;
    }

    /**
     * 更新TextView显示的图片
     */
    private void updateTextViewDrawable()
    {
        for(int i = 0;i<this.navigationBitNumber;i++){
            if(i == this.selectNavigationBit){
                if (this.selectTextViewChildImageFromResource != null && this.selectTextViewChildImageFromResource.length>0){
                    setCompundDrawables(i,this.selectTextViewChildImageFromResource);
                }
            }else{
                if (this.unselectTextViewChildImageFromResource != null && this.unselectTextViewChildImageFromResource.length>0){
                    setCompundDrawables(i, this.unselectTextViewChildImageFromResource);
                }else if(this.selectTextViewChildImageFromResource != null && this.selectTextViewChildImageFromResource.length>0){
                    setCompundDrawables(i, this.selectTextViewChildImageFromResource);
                }
            }
        }
    }

    private void setCompundDrawables(int i,int[] is){
        switch (this.iconPosition){
            case TOP:this.mTextViewChild[i].setCompoundDrawables(null, updateDrawable(getContext(),is[i]), null, null);break;
            case RIGHT:this.mTextViewChild[i].setCompoundDrawables(null, null, updateDrawable(getContext(),is[i]), null);break;
            case BOTTOM:this.mTextViewChild[i].setCompoundDrawables(null, null, null, updateDrawable(getContext(),is[i]));break;
            case LEFT:this.mTextViewChild[i].setCompoundDrawables(updateDrawable(getContext(),is[i]), null, null, null);break;
        }
    }

    /**
     * 更改TextView的UI
     * @param i
     */
    private void updateTextViewUI(int i){
        if(i<navigationBitNumber){
            oldelectNavigationBit = selectNavigationBit;
            selectNavigationBit = i;

            //设置当前点击项的文本颜色为选中时颜色
            NavigationView.this.mTextViewChild[NavigationView.this.selectNavigationBit].setTextColor(NavigationView.this.selectTextColor);
            //设置当前点击项的背景颜色为选中时背景颜色
            ((GradientDrawable)NavigationView.this.linearLayouts[NavigationView.this.selectNavigationBit].getBackground()).setColor(selectBackColor);
            //设置上一个点击项的文本颜色为没有选中时颜色
            NavigationView.this.mTextViewChild[NavigationView.this.oldelectNavigationBit].setTextColor(NavigationView.this.unSelectTextColor);
            //设置上一个点击项的背景颜色为没有选中时背景颜色
            ((GradientDrawable)NavigationView.this.linearLayouts[NavigationView.this.oldelectNavigationBit].getBackground()).setColor(unSelectBackColor);

            //设置文字变大效果
            if(selecttTextSize != 0){
                NavigationView.this.mTextViewChild[NavigationView.this.selectNavigationBit].setTextSize(TypedValue.COMPLEX_UNIT_PX,this.selecttTextSize);
                NavigationView.this.mTextViewChild[NavigationView.this.oldelectNavigationBit].setTextSize(TypedValue.COMPLEX_UNIT_PX,this.textSize);
            }

            if (NavigationView.this.selectTextViewChildImageFromResource != null)
            {
                NavigationView.this.mTextViewChild[NavigationView.this.selectNavigationBit].setCompoundDrawables(null,
                        updateDrawable(getContext(),NavigationView.this.selectTextViewChildImageFromResource[NavigationView.this.selectNavigationBit]), null, null);
            }
            if (NavigationView.this.unselectTextViewChildImageFromResource != null)
            {
                NavigationView.this.mTextViewChild[NavigationView.this.oldelectNavigationBit].setCompoundDrawables(null,
                        updateDrawable(getContext(), NavigationView.this.unselectTextViewChildImageFromResource[NavigationView.this.oldelectNavigationBit]), null, null);
            }

        }
    }

    /**
     * 布局的点击事件监听
     */
    private OnClickListener clickListener = new OnClickListener()
    {
        public void onClick(View paramAnonymousView)
        {
            for(int i=0;i<NavigationView.this.navigationBitNumber;i++){
                if(NavigationView.this.linearLayouts[i] == paramAnonymousView){
                    if(i == NavigationView.this.selectNavigationBit){
                    }else{
                        if(listener!=null){
                            setItemIsAllowedClick(i, listener.onItemChange(i));
                        }
                        if(getItemIsAllowedClick(i)){
                            if(slidingStripPosition == NONE){
                                setCurrentNum(i);
                                if(NavigationView.this.viewPager != null){
                                    NavigationView.this.viewPager.setCurrentItem(i,false);
                                }else{
                                    updateTextViewUI(i);
                                }
                            }else{
                                if(NavigationView.this.viewPager != null){
                                    NavigationView.this.viewPager.setCurrentItem(i,true);
                                }else{
                                    updateTextViewUI(i);
                                }
                            }
                        }


                    }
                }

            }
        }
    };

    /**
     * 更改TextView的Drawable
     * @param context
     * @param resource
     * @return
     */
    private Drawable updateDrawable(Context context, int resource){
        Drawable drawable = context.getResources().getDrawable(resource);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 绑定ViewPager
     * @param vp
     */
    public NavigationView binding(ViewPager vp){
        if(this.viewPager != vp){
            this.viewPager = vp;
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                    setOffset(position, positionOffset);
                }

                @Override
                public void onPageSelected(int position){
                    updateTextViewUI(position);
                }

                @Override
                public void onPageScrollStateChanged(int state){
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        //空闲状态  pager处于空闲状态
                        oldelectNavigationBit = selectNavigationBit;
                    }
                }
            });
        }
        return this;
    }

    /**
     * 设置滑动条的当前位置
     * @param n
     */
    private void setCurrentNum(int n) {
        mCurrentNum = n;
        mOffset = 0;
    }

    /**
     * 设置滑动条的偏移量
     * @param position
     * @param offset
     */
    private void setOffset(int position, float offset) {
        if (offset == 0) {
            return;
        }
        mCurrentNum = position;
        mOffset = offset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(slidingStripPosition != NONE){
            if(columnCount == -1){
                if (mTabWidth == 0) {
                    mWidth = getWidth();
                    mTabWidth = mWidth / navigationBitNumber;
                }
                if(mTextWidth==null || mTextWidth.length!=this.navigationBitNumber){
                    mTextWidth = new float[this.navigationBitNumber];
                    for(int i = 0;i<this.navigationBitNumber;i++){
                        this.mTextWidth[i] = mTextViewChild[i].getWidth();
                    }
                }

                float left = mCurrentNum * mTabWidth;
                float right = 0;
                float top = 0;
                float bottom = 0;

                if(slidingStripSize == MATCH_PARENT){
                    left = left+mOffset*mTabWidth;
                    right = left+mTabWidth;
                }else{
                    left = left + (mTabWidth-mTextWidth[mCurrentNum])/2+mOffset*(mTabWidth+mTextWidth[mCurrentNum]/2-(mCurrentNum+1==navigationBitNumber?0:mTextWidth[mCurrentNum+1])/2);
                    right = left+mTextWidth[mCurrentNum]+mOffset*((mCurrentNum+1==navigationBitNumber?0:mTextWidth[mCurrentNum+1])-mTextWidth[mCurrentNum]);
                }

                if(slidingStripPosition == TOP){
                    top = 0;
                    bottom = scllorHeight;
                }else if(slidingStripPosition == BOTTOM){
                    top = getHeight()-scllorHeight;
                    bottom = getHeight();
                }
                mPaint.setColor(scllorColor);
                middle = (left+right)/2;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }else{
                if (mTabWidth == 0) {
                    mWidth = getWidth();
                    mTabWidth = mWidth / navigationBitNumber;
                }
                if(mTextWidth==null || mTextWidth.length!=this.navigationBitNumber){
                    mTextWidth = new float[this.navigationBitNumber];
                    for(int i = 0;i<this.navigationBitNumber;i++){
                        this.mTextWidth[i] = mTextViewChild[i].getWidth();
                    }
                }

                float left = mCurrentNum * columnW;
                float right = 0;
                float top = 0;
                float bottom = 0;

                if(mCurrentNum>=columnCount/2 && navigationBitNumber-mCurrentNum>columnCount-columnCount/2){
                    if(oldelectNavigationBit != selectNavigationBit){
                        columnOffset =mCurrentNum- columnCount/2;
                        scrollTo((int)((mOffset+columnOffset)*columnW),0);
                    }

                }

                if(slidingStripSize == MATCH_PARENT){
                    left = left+mOffset*columnW;
                    right = left+columnW;
                }else{
                    left = left + (columnW-mTextWidth[mCurrentNum])/2+mOffset*(columnW+mTextWidth[mCurrentNum]/2-(mCurrentNum+1==navigationBitNumber?0:mTextWidth[mCurrentNum+1])/2);
                    right = left+mTextWidth[mCurrentNum]+mOffset*((mCurrentNum+1==navigationBitNumber?0:mTextWidth[mCurrentNum+1])-mTextWidth[mCurrentNum]);
                }

                if(slidingStripPosition == TOP){
                    top = 0;
                    bottom = scllorHeight;
                }else if(slidingStripPosition == BOTTOM){
                    top = getHeight()-scllorHeight;
                    bottom = getHeight();
                }
                mPaint.setColor(scllorColor);
                middle = (left+right)/2;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

        }

    }

    /**
     * 获取滑动条的中间值
     * @return
     */
    public float getMiddle(){
        return this.middle;
    }

    /**
     * 获取一节滑动条的宽度
     * @return
     */
    public float getTabWidth(){
        return getWidth()/navigationBitNumber;
    }

    /**
     * 设置滑动条位置，默认不显示
     * @param slidingStripPosition 允许 {@link #TOP} 或 {@link #BOTTOM} 或{@link #NONE}.
     * 默认值是 {@link #NONE}.
     */
    public NavigationView setSlidingStripPosition(@SlidingStripPosition int slidingStripPosition){
        this.slidingStripPosition = slidingStripPosition;
        return this;
    }

    /**
     * 设置图片显示文字，默认显示在文字上边
     * @param iconPosition 允许 {@link #TOP} 或 {@link #RIGHT} 或 {@link #BOTTOM} 或 {@link #LEFT}.
     * 默认值是 {@link #NONE}.
     */
    public NavigationView setIconPosition(@IconPosition int iconPosition){
        this.iconPosition = iconPosition;
        return this;
    }

    /**
     * 设置滑动条的高度，单位是sp
     * @param height
     */
    public NavigationView setScllorHeight(float height){
        this.scllorHeight = applyDimension(TypedValue.COMPLEX_UNIT_SP, height);
        return this;
    }

    /**
     * 设置滑动条的颜色
     * @param color
     */
    public NavigationView setScllorColor(int color){
        this.scllorColor = color;
        return this;
    }

    /**
     * 设置滑动条的宽度
     * @param slidingStripSize 允许 {@link #MATCH_PARENT} 或 {@link #WRAP_CONTENT}.
     * 默认值是 {@link #MATCH_PARENT}.
     */
    public NavigationView setScllorWidth(@SlidingStripSize int slidingStripSize){
        this.slidingStripSize = slidingStripSize;
        return this;
    }

    /**
     * 设置title集合
     * @param title
     */
    public NavigationView setTextViewChildTextFromString(String[] title){
        this.mTextViewChildTextFromString = title;
        return this;
    }

    /**
     * 通过资源Id设置title集合
     * @param titleID
     */
    public NavigationView setTextViewChildTextFromString(int titleID){
        this.mTextViewChildTextFromString = getContext().getResources().getStringArray(titleID);
        return this;
    }

    /**
     * 初始化title及其他
     */
    private void initTitleEtc(){
        //获得控件Item数量
        this.navigationBitNumber = this.mTextViewChildTextFromString == null?0:this.mTextViewChildTextFromString.length;
        //初始化TextView集合
        this.mTextViewChild = new TextView[this.navigationBitNumber];
        //初始化布局集合
        this.linearLayouts = new LinearLayout[this.navigationBitNumber];
    }

    /**
     * 设置文字大小
     * @param size
     */
    public NavigationView setTextSize(float size){
        this.textSize = applyDimension(TypedValue.COMPLEX_UNIT_SP,size);
        return this;
    }

    /**
     * 设置选中时文字大小
     * @param size
     */
    public NavigationView setSelectTextSize(float size){
        this.selecttTextSize = applyDimension(TypedValue.COMPLEX_UNIT_SP,size);
        return this;
    }

    /**
     * 设置未选中时文字颜色
     * @param color
     */
    public NavigationView setUnSelectTextColor(int color){
        this.unSelectTextColor = color;
        return this;
    }

    /**
     * 设置选中时文字颜色
     * @param color
     */
    public NavigationView setSelectTextColor(int color){
        this.selectTextColor = color;
        return this;
    }

    /**
     * 设置选中时背景颜色
     * @param selectBackColor
     */
    public NavigationView setSelectBackColor(int selectBackColor) {
        this.selectBackColor = selectBackColor;
        return this;
    }

    /**
     * 设置未选中时背景颜色
     * @param unSelectBackColor
     */
    public NavigationView setUnSelectBackColor(int unSelectBackColor) {
        this.unSelectBackColor = unSelectBackColor;
        return this;
    }

    public NavigationView setLeftDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        return this;
    }

    public NavigationView setMiddleDrawable(Drawable middleDrawable) {
        this.middleDrawable = middleDrawable;
        return this;
    }

    public NavigationView setRightDrawable(Drawable rightDrawable) {
        this.rightDrawable = rightDrawable;
        return this;
    }

    /**
     * 设置图片与文字之间的间隔
     * @param padding
     * @return
     */
    public NavigationView setPadding(float padding){
        this.padding  = applyDimension(TypedValue.COMPLEX_UNIT_SP,padding);
        return this;
    }

    /**
     *  单位转换 比如sp转px
     * @param unit
     * @param size
     * @return
     */
    private float applyDimension(int unit, float size){
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    /**
     * 设置一行的列数
     */
    public NavigationView setColumnCount(int columnCount){
        this.columnCount = columnCount;
        return this;
    }

    /**
     * 获得一行的列数
     */
    public int getColumnCount(){
        return this.columnCount;
    }

    /**
     * 设置最开始选中项
     * @param selectNavigationBit
     * @return
     */
    public NavigationView setSelectNavigationBit(int selectNavigationBit) {
//        this.oldelectNavigationBit = this.selectNavigationBit;
        this.selectNavigationBit = selectNavigationBit;
        return this;
    }

    /**
     * 执行修改
     */
    public void commit(){
        initTitleEtc();
        initView(parms);
        updateTextViewDrawable();
    }

    /**
     * 获取选中项
     * @return
     */
    public int getSelectNavigationBit() {
        return selectNavigationBit;
    }



    /**
     * 注意：仅仅只有点击事件受用，viewpager的滑动不受影响
     *       常常与禁用的滑动事件的viewpager配合使用
     * @param l
     */
    public void setOnItemChangeListener(OnItemChangeListener l){
        this.listener = l;
    }

    public void setItemIsAllowedClick(int item,boolean b){
        iatc.put(item,b);
    }

    public boolean getItemIsAllowedClick(int item){
        return iatc.get(item)==null?true:iatc.get(item);
    }

    public interface OnItemChangeListener{
        /**
         * 返回为false时禁止跳转界面，返回为true时允许跳转
         * @param position
         * @return
         */
        boolean onItemChange(int position);
    }
}