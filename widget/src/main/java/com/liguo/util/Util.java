package com.liguo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.liguo.application.BaseApplication;
import com.liguo.downFile.DownloadProgressInterceptor;
import com.liguo.downFile.DownloadProgressListener;
import com.liguo.servers.API;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 公共方法
 *
 * @version 1.0
 * @date 创建时间：2015-10-9 下午3:26:50
 */
public class Util {
    private static Toast toast;
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    private static API api;
    private static API downApi;
    private static String sdcardPath;


    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
    private final static String[] constellationArr = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
    public static DES des = new DES();

    //使用前必须配置 -----start
    public static String HTTPURL = "http://www.baidu.com";//网络连接的地址
    public static String SP_NAME = "Extends";//默认的SharedPreferences的文件名
    public static int PLACEHOLDER = 0;//占位图
    public static int ERROR = 0;//错误图
    public static int COLOR_DEFAULT = Color.parseColor("#00000000");//BaseActivity默认的状态栏颜色
    //使用前必须配置 -----end


    /**
     * dip转px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * BaseApplication.getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * 解读流
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readInputStream(InputStream inputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();
        return new String(bos.toByteArray(), "UTF-8");
    }

    /**
     * Toast提示
     *
     * @param str
     */
    public static void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 没有偏移量的Toast提示
     *
     * @param str
     * @param duration
     * @param gravity
     */
    public static void showToast(String str, int duration, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), str, duration);
        } else {
            toast.setText(str);
            toast.setDuration(duration);
        }
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 显示ProgressDialog
     *
     * @param context
     * @param title
     * @param message
     */
    public static ProgressDialog showProgressDialog(Context context,
                                                    String title, String message) {
        return ProgressDialog.show(context, title, message);
    }

    /**
     * 显示Dialog(用时记得修改)
     *
     * @param context
     */
    public static Dialog showDialog(Context context) {
        Dialog dialogBar = new Dialog(context);
        dialogBar.show();
        return dialogBar;
    }

    /**
     * 关闭Dialog
     *
     * @param dialog
     */
    public static void dimssDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    /**
     * 关闭ProgressDialog
     *
     * @param dialog
     */
    public static void dimssProgressDialog(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 写值到xml
     *
     * @param str  Key
     * @param str2 值
     * @return
     */
    public static void setString(String str, String str2) {
        setString(Util.SP_NAME, str, str2);
    }

    /**
     * 写值到xml
     *
     * @param str  Key
     * @param str2 值
     * @return
     */
    public static void setString(String SP_NAME, String str, String str2) {
        SharedPreferences sPreference = BaseApplication.getContext().getSharedPreferences(SP_NAME,
                Activity.MODE_PRIVATE);
        Editor editor = sPreference.edit();
        try {
            editor.putString(des.encrypt(str), des.encrypt(str2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    /**
     * 获取xml的值
     *
     * @param str
     * @return
     */
    public static String getString(String SP_NAME, String str) {
        SharedPreferences sPreference = BaseApplication.getContext().getSharedPreferences(SP_NAME,
                Activity.MODE_PRIVATE);
        String s = sPreference.getString(des.encrypt(str), "");
        try {
            return des.decrypt(s);
//            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取xml的值
     *
     * @param str
     * @return
     */
    public static String getString(String str) {
        return getString(Util.SP_NAME, str);
    }

    /**
     * 获取xml的值
     *
     * @param str
     * @return
     */
    public static Integer getInt(String str) {
        String temp = getString(str);
        if (temp.equals("")) {
            return 0;
        }
        return Integer.valueOf(temp);
    }

    /**
     * 删除SharedPreferences保存的数据
     * @param SP_NAME
     */
    public static  void deleteData(String SP_NAME){
        SharedPreferences preferences=BaseApplication.getContext().getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    /**
     * 改变字符串中特定字体的颜色
     *
     * @param textView
     * @param text
     * @param specifiedTexts
     * @param color
     */
    public static void setSpecifiedTextsColor(TextView textView, String text, String specifiedTexts, int color) {
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.length();
        String temp = text.toLowerCase();
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(specifiedTexts.toLowerCase());

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }
        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(new ForegroundColorSpan(color), i, i + sTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(styledText);
    }

    /**
     * 加载网络图片
     */
    public static void showImg(Object context, String url, final ImageView image, Transformation... transformation) {
        RequestManager rm = null;
        if(context == null){
            rm = Glide.with(BaseApplication.getContext());
        }else if(context instanceof FragmentActivity){
            rm = Glide.with((FragmentActivity)context);
        }else if(context instanceof Activity){
            rm = Glide.with((Activity)context);
        }else if(context instanceof android.support.v4.app.Fragment){
            rm = Glide.with((android.support.v4.app.Fragment)context);
        }else if(context instanceof Fragment){
            rm = Glide.with((Fragment)context);
        }else if(context instanceof Context){
            rm = Glide.with((Context)context);
        }else{
            return;
        }

        if(transformation == null){
            rm.load(url)
                    .placeholder(PLACEHOLDER) // 占位符
                    .error(ERROR) // 错误占位符
                    .crossFade()//平滑过渡
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }else{
            rm.load(url)
                    .placeholder(PLACEHOLDER) // 占位符
                    .error(ERROR) // 错误占位符
                    .bitmapTransform(transformation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);

        }



    }

    /**
     * 加载网络图片
     */
    public static void showImg(String url, final ImageView image) {
        showImg((Object) null,url,image);
    }

    /**
     * 加载网络图片
     */
    public static void showImg(Object object, String url, final ImageView image) {
        showImg(object,url,image,(Transformation[]) null);
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    /**
     * 获取时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * 将短时间格式字符串转换为时间 type(如yyyy-MM-dd)
     *
     * @param strDate
     * @param type
     * @return
     */
    public static Date strToDate(String strDate, String type) {
        return new SimpleDateFormat(type).parse(strDate, new ParsePosition(0));
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @param type
     * @return
     */
    public static String dateToStr(Date dateDate, String type) {
        return new SimpleDateFormat(type).format(dateDate);
    }

    /**
     * 将String类型的长时间转换为date类型string
     * @param currentTime
     * @param type
     * @return
     */
    public static  String longToString(String currentTime,String type){
        return new SimpleDateFormat(type).format(new Date(Long.parseLong(currentTime) * 1000));
    }

    /**
     * 截取view的图片
     * @param v
     * @return
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }


    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @return
     */
    public static Point getScreenMetrics() {
        DisplayMetrics dm = BaseApplication.getContext().getResources().getDisplayMetrics();
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
        return new Point(WIDTH, HEIGHT);
    }

    /**
     * 获取屏幕长宽比
     *
     * @return
     */
    public static float getScreenRate() {
        if (WIDTH == 0 || HEIGHT == 0) {
            getScreenMetrics();
        }
        return HEIGHT*1.0f/WIDTH;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth() {
        if (WIDTH == 0) {
            getScreenMetrics();
        }
        return WIDTH;
    }

    //获取屏幕的高度
    public static int getScreenHeight() {
        if (HEIGHT == 0) {
            getScreenMetrics();
        }
        return HEIGHT;
    }

    /**
     * 时间戳转时间格式
     *
     * @param time 时间戳
     * @param type 要转成的格式
     * @return
     */
    public static String times(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    //108 930 890  108 917 000
    public static String getStandardDate(String timeStr, String nowtime) {
        String times = "";
//        long t = Long.parseLong(timeStr);
//        long time = System.currentTimeMillis()- (t * 1000);
        long time = Long.parseLong(nowtime) - Long.parseLong(timeStr);//t;
        long mill = (long) Math.ceil(time);//秒前
        long minute = (long) Math.ceil(time / 60);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60);// 天前
        if (day - 1 >= 0 && day - 2 <= 0) {
            times = "1天前";
        } else if (hour - 1 >= 0 && hour <= 24) {
            times = hour + "小时前";
        } else if (minute <= 60) {
            if (minute < 1) {
                times = "1分钟前";
            } else {
                times = minute + "分钟前";
            }
        } else {
            times = times(timeStr, "yyyy.MM.dd");
        }
        return times;
    }


    public static String getSystemDate(String timeStr) {
        String times = "";
        StringBuffer sb = new StringBuffer();
        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t * 1000);
        long mill = (long) Math.ceil(time / 1000);//秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (2 > day && day > 1) {
            times = "1天前";
        } else if (hour > 1 && hour < 24) {
            times = hour + "小时前";
        } else if (minute < 60) {
            if (minute < 1) {
                times = "1分钟前";
            } else {
                times = minute + "分钟前";
            }
        } else {
            times = times(timeStr, "yyyy.MM.dd");
        }
        return times;
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSDPath() {
        if(sdcardPath != null){
            return sdcardPath;
        }
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 获取星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
    }

    /**
     * 获取字符串的MD5值
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 判断手机号格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断账号密码格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isregEx(String mobiles) {
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
        Matcher m = p.matcher(mobiles);
        return m.find();
    }


    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 判断应用是否在前台
     *
     * @param mContext
     * @return
     */
    public static boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过ID获得Drawable
     *
     * @param res
     * @return
     */
    public static Drawable getDrawablefromId(int res) {
        Drawable drawable = BaseApplication.getContext().getResources().getDrawable(res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isListViewReachTopEdge(final ListView listView) {
        boolean result = false;
        if (listView.getFirstVisiblePosition() == 0) {
            final View topChildView = listView.getChildAt(0);
//            result=topChildView.getTop()==0;
            result = true;
        }
        return result;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable() {
        Context context = BaseApplication.getContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static String getVerCode(Context context) {
        String verCode = "-1";
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
//            Log.e("msg",e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取sdcard中apk版本号
     *
     * @param context
     * @return
     */
    public static String getSDKVerCode(Context context) {
        String verCode = "-1";
        try {
            String filePath = Util.getSDPath() + "/everydaytaichi/app/everydaytaichi.apk";
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
//        Log.d("name", packageInfo.packageName);
//        Log.d("uid", packageInfo.sharedUserId);
//        Log.d("vname", packageInfo.versionName);
//        Log.d("code", packageInfo.versionCode+"");
            verCode = packageInfo.versionName;
        } catch (Exception e) {
        }
        return verCode;
    }


    /**
     * 判断是否为平板
     *
     * @return
     */
    public static boolean isPad() {
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 7.0) {
            return true;
        }
        return false;
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */

    public static String addComma(String str) {
        // 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将[789,456,] 中最后一个[,]去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length()-1);
        }
        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;

    }

    /**
     * 获取api
     * @return
     */
    public static API getAPI(){
        if(api == null){
            //OkHttpClient.Builder;
            api = new Retrofit.Builder()
                    .baseUrl(Util.HTTPURL)
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(5_000, TimeUnit.SECONDS)
                            .build())
                    .build()
                    .create(API.class);
        }
       return api;
    }

    /**
     * 获取api
     * @return
     */
    public static API getDownAPI(DownloadProgressListener listener){
        if(downApi == null){
            //OkHttpClient.Builder;
            downApi = new Retrofit.Builder()
                    .baseUrl(Util.HTTPURL)
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(new DownloadProgressInterceptor(listener))
                            .connectTimeout(5_000, TimeUnit.SECONDS)
                            .build())
                    .build()
                    .create(API.class);
        }
        return downApi;
    }

    /**
     * 获取api
     * @return
     */
    public static Observable getDownFile(DownloadProgressListener listener, String fileUrl, final File file){
        return getDownAPI(listener).getFile(fileUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream is) {
                        try {
//                            File file = new File(Environment.getExternalStorageDirectory(), "12345.zip");
                            FileOutputStream fos = new FileOutputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                fos.flush();
                            }
                            fos.close();
                            bis.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .map(new Func1() {
                    @Override
                    public String call(Object o) {
                        return file.getName();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 连接网络的进一步封装
     * @return
     */
    public static Observable connection(Map<String,String> map){
        return getAPI().getDefaults(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 读输入流
     *
     * @param is
     * @return
     */
    public static String getStringByInputStream(InputStream is) {
        String content = null;
        try {
            if (is != null) {

                byte[] buffer = new byte[is.available()];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1)
                        break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 动态获取listView高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*
    **启动第三方App
     */
    public static void launchAnotherApp(Context context,String packageName){
        if(isAppInstalled(context, packageName)){
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
        } else{
            goToMarket(context, packageName);
        }
    }
    /*
    **检测第三方App是否已安装
     */
    public static boolean isAppInstalled(Context context, String packageName){
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }
    /*
    **去应用市场下载页面
     */
    public static void goToMarket(Context context, String packageName){
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname="+packageName)));

        }
    }



}
