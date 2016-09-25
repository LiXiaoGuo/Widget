package com.liguo.views;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
 * 可以改变第二进度条高度和颜色，带动画效果
 * * Created by Extends on 2016/7/8 0008.
 */
public class ThicknessProgressBar extends View {
    private Paint paint;
    private int progressW;
    private int max;
    private float secondaryProgressH;
    private int progressColor;
    private int secondaryProgressColor;
    private boolean isAnim = false;
    private float l = 0.0f;//边长
    private float old = 0.0f;
    public ThicknessProgressBar(Context context) {
        this(context, null);
    }

    public ThicknessProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThicknessProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        progressColor = Color.RED;
        secondaryProgressColor = Color.parseColor("#ebedec");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(secondaryProgressH == 0){
            secondaryProgressH = getHeight();//*2.0f/5;
        }

        paint.setColor(secondaryProgressColor);
        canvas.drawRect(0, getHeight() - secondaryProgressH, getWidth(), getHeight(), paint);

        paint.setColor(progressColor);
        if(!isAnim){
            isAnim = true;
            l = progressW*getWidth()*1.0f/max;
            canvas.drawRect(0, 0, old, getHeight(), paint);
            startAnimation();
        }else{
            canvas.drawRect(0, 0, l, getHeight(), paint);
        }

    }

    private void startAnimation() {
        ValueAnimator anim = ValueAnimator.ofObject(new LEvaluator(), old, l);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                l = Float.valueOf(animation.getAnimatedValue()+"");
                postInvalidate();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                old = l;
                isAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        postInvalidate();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public int getProgressW() {
        return progressW;
    }

    public void setProgressW(int progressW) {
        this.progressW = progressW;
        postInvalidate();
    }

    public int getSecondaryProgressColor() {
        return secondaryProgressColor;
    }

    public void setSecondaryProgressColor(int secondaryProgressColor) {
        this.secondaryProgressColor = secondaryProgressColor;
        postInvalidate();
    }

    public float getSecondaryProgressH() {
        return secondaryProgressH;
    }

    public void setSecondaryProgressH(float secondaryProgressH) {
        this.secondaryProgressH = secondaryProgressH;
        postInvalidate();
    }

    public class LEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float start = Float.valueOf(startValue+"");
            float end = Float.valueOf(endValue+"");

            return start+fraction*(end-start);
        }

    }
}
