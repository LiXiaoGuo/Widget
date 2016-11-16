package com.liguo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.liguo.R;

/**
 * 设置圆角ImageView
 * Created by Administrator on 2016/8/10.
 */
public class ArcImageView extends ImageView {

    public ArcImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context,attrs);
        init();
    }

    public ArcImageView(Context context) {
        this(context,null);
    }

    private final RectF roundRect = new RectF();
    private float rect_adius = 6;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    /**
     * 初始化控件的属性
     * 从xml中获取
     * @param paramContext
     * @param paramAttributeSet
     */
    private void initFromAttributes(Context paramContext, AttributeSet paramAttributeSet)
    {
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.arcImagView, 0, 0);
        try
        {
            this.rect_adius = localTypedArray.getDimension(R.styleable.arcImagView_arc_rectAdius, rect_adius);
        }finally{
            localTypedArray.recycle();
        }
    }

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
    }

    /**
     * 设置四角弯曲程度,单位 dp
     * @param adius
     */
    public void setRectAdius(float adius) {
        rect_adius = adius*getResources().getDisplayMetrics().density;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

}
