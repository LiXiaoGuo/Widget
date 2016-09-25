package com.liguo.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * 
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * 
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
	
	private boolean isScrollable = true;
	private static final String TAG = "HackyViewPager";

	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			if (isScrollable == false) {
				return false;
			} else {
				return super.onInterceptTouchEvent(ev);
			}
		} catch (IllegalArgumentException e) {
			// 不理会
			Log.e(TAG, "hacky viewpager error1");
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			// 不理会
			Log.e(TAG, "hacky viewpager error2");
			return false;
		}
	}

	//设置是否可以滑动
	public void setScanScroll(boolean b) {
		this.isScrollable = b;
	}

}
