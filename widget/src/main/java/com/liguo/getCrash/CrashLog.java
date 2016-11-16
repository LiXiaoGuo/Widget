package com.liguo.getCrash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;


import com.liguo.application.AppManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
 * * Created by Extends on 2016/5/9 0009.
 */
public class CrashLog implements UncaughtExceptionHandler {
    private String[] receiveEmail;
    private String username,password,nickname;
    private static final String TAG = "CrashLog";
    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashLog mInstance = new CrashLog();
    private Context mContext;
    private Map<String, String> mLogInfo = new HashMap();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd:HH-mm-ss");

    private CrashLog() {
    }

    public static CrashLog getInstance() {
        return mInstance;
    }

    public void init(Context paramContext,String username,String password,String nickname,String... receiveEmail) {
        this.receiveEmail = receiveEmail;
        this.mContext = paramContext;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!this.handleException(ex) && this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
            AppManager.getInstance().finishAllActivity();
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    public boolean handleException(final Throwable paramThrowable) {
        if(paramThrowable == null) {
            return false;
        } else {
            (new Thread() {
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                    getDeviceInfo(mContext);
                    saveCrashLogToFile(paramThrowable);
                    Looper.loop();

                }
            }).start();

            return true;
        }
    }

    private void sentEmail(String paramThrowable) {
        try {
            MailSenderInfo e = new MailSenderInfo();
            e.setMailServerHost("smtp.qq.com");
            e.setMailServerPort("465");
            e.setValidate(true);
            e.setUserName(this.username);//
            e.setPassword(this.password);//
            e.setFromAddress(this.username,this.nickname);
            e.setToAddress(this.receiveEmail);
            e.setSubject(mContext.getPackageName() + " " + mLogInfo.get("BRAND")+"/"+mLogInfo.get("MODEL") + " " + this.mSimpleDateFormat.format(new Date()));
            e.setContent(paramThrowable);
            SimpleMailSender sms = new SimpleMailSender();
            Log.e(TAG, "sentEmail");
            boolean b = sms.sendTextMail(e);
            Log.e(TAG,b+"");
        } catch (Exception var4) {
            Log.e("SendMail", var4.getMessage(), var4);
        }

//        SendMail sendmail = new SendMail();
//        sendmail.setHost("smtp.qq.com");//smtp.mail.yahoo.com.cn
//        sendmail.setUserName("1043274460@qq.com");//您的邮箱用户名
//        sendmail.setPassWord("2010YYandLG");//您的邮箱密码
//        sendmail.setTo(receiveEmail);//接收者
//        sendmail.setFrom("1043274460@qq.com");//发送者
//        sendmail.setSubject("你好，这是测试2！");
//        sendmail.setContent("你好这是一个带多附件的测试2！");
//        boolean b = sendmail.sendMail();

    }

    public void getDeviceInfo(Context paramContext) {
        try {
            PackageManager mFields = paramContext.getPackageManager();
            PackageInfo field = mFields.getPackageInfo(paramContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(field != null) {
                String versionName = field.versionName == null?"null":field.versionName;
                String versionCode = String.valueOf(field.versionCode);
                this.mLogInfo.put("versionName", versionName);
                this.mLogInfo.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException var10) {
            var10.printStackTrace();
        }

        Field[] var11 = Build.class.getDeclaredFields();
        Field[] var6 = var11;
        int var14 = var11.length;

        for(int var13 = 0; var13 < var14; ++var13) {
            Field var12 = var6[var13];

            try {
                var12.setAccessible(true);
                this.mLogInfo.put(var12.getName(), var12.get("").toString());
                Log.e("NorrisInfo", var12.getName() + ":" + var12.get(""));
            } catch (IllegalArgumentException var8) {
                var8.printStackTrace();
            } catch (IllegalAccessException var9) {
                var9.printStackTrace();
            }
        }

    }

    private String saveCrashLogToFile(Throwable paramThrowable) {
        StringBuffer mStringBuffer = new StringBuffer();
//        mStringBuffer.append("UserId:"+Util.getString(Util.UID)+"\r\n");
//        mStringBuffer.append("UserName:"+ Util.getString(Util.NAME)+"\r\n");
        Iterator mPrintWriter = this.mLogInfo.entrySet().iterator();
        String mResult;
        while(mPrintWriter.hasNext()) {
            Map.Entry mWriter = (Map.Entry)mPrintWriter.next();
            String mThrowable = (String)mWriter.getKey();
            mResult = (String)mWriter.getValue();
            mStringBuffer.append(mThrowable + "=" + mResult + "\r\n");
        }
        StringWriter mWriter1 = new StringWriter();
        PrintWriter mPrintWriter1 = new PrintWriter(mWriter1);
        paramThrowable.printStackTrace(mPrintWriter1);
        paramThrowable.printStackTrace();
        for(Throwable mThrowable1 = paramThrowable.getCause(); mThrowable1 != null; mThrowable1 = mThrowable1.getCause()) {
            mThrowable1.printStackTrace(mPrintWriter1);
            mPrintWriter1.append("\r\n");
        }
        mPrintWriter1.close();
        mResult = mWriter1.toString();
        mStringBuffer.append(mResult);
        Log.e("---------", mStringBuffer.toString());

        sentEmail(mStringBuffer.toString());
        return null;
    }


}
