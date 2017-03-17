package com.liguo.views;

import android.util.Log;


import com.liguo.util.Util;

import rx.Subscriber;

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
 * * Created by Extends on 2016/10/8 0008.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        if(!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(e!=null&&e.getMessage()!=null){
            Util.showToast(e.getMessage());
            Log.e("onError",e.getMessage());
        }
        onCompleted();
    }

    @Override
    public void onNext(T t) {

    }
}
