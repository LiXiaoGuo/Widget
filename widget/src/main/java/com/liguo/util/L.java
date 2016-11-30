package com.liguo.util;

import android.util.Log;

/**
 * 日志打印框架
 * Created by Extends on 2016/11/30.
 */
public final class L {
    public static String TAG = "L";
    public static boolean isShow = true;

    public static void debug(String msg) {
        if(isShow){
            Log.d(TAG,msg);
        }
    }
    public static void debug(Class c,String msg) {
        debug(c,1,msg);
    }
    public static void debug(Class c,int lineNum,String msg) {
        if(isShow){
            Log.d(c.getName()+"("+c.getSimpleName()+".java:"+lineNum+")",msg);
        }
    }

    public static void info(String msg) {
        if(isShow){
            Log.i(TAG,msg);
        }
    }
    public static void info(Class c,String msg) {
        info(c,1,msg);
    }
    public static void info(Class c,int lineNum,String msg) {
        if(isShow){
            Log.i(c.getName()+"("+c.getSimpleName()+".java:"+lineNum+")",msg);
        }
    }

    public static void error(String msg) {
        if(isShow){
            Log.e(TAG,msg);
        }
    }
    public static void error(Class c,String msg) {
        error(c,1,msg);
    }
    public static void error(Class c,int lineNum,String msg) {
        if(isShow){
            Log.e("("+c.getSimpleName()+".java:"+lineNum+")",msg);
        }
    }

    public static void warn(String msg) {
        if(isShow){
            Log.w(TAG,msg);
        }
    }
    public static void warn(Class c,String msg) {
        warn(c,1,msg);
    }
    public static void warn(Class c,int lineNum,String msg) {
        if(isShow){
            Log.w(c.getName()+"("+c.getSimpleName()+".java:"+lineNum+")",msg);
        }
    }
}
