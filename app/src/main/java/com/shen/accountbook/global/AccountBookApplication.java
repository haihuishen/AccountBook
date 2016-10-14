package com.shen.accountbook.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.shen.accountbook.domain.UserInfo;

/**
 * 自定义application, 进行全局初始化
 * 这里将这个类作为全局初始化，程序运行时就已经初始化了
 * 在类中定义用于保存重要信息的变量，
 *	如：
 *		登陆成功后把唯一标示保存进去，整个应用程序都可以取到这个值了
 *
 *	使用：
 *		MyApplication.getContext();
 *
 * 全局要这样在"清单文件"中声明!
 *
 *  <application>
 *      android:name="com.xxx.xxx.global.MyApplication"   ===> 这样就全局化了
 *
 *      android:allowBackup="true"
 *      android:icon="@drawable/ic_launcher"
 *      android:label="@string/app_name"
 *      android:theme="@style/Theme.AppCompat.Light"
 *      <activity
 *      	。。。。。。。。。。。。。
 *      </activity>
 *  </application>
 *
 *  如果不使用，static
 *      首先调用本类的getInstance()得到AccountBookApplication的对象
 *      就要使用 ：AccountBookApplication.getInstance().getXXX();
 *
 */
public class AccountBookApplication extends Application {

    private static Context context;
    private static Handler handler;
    /** 主线程的ID*/
    private static int mainThreadId;

    private static boolean isLogin;         // 登录状态
    private static UserInfo userInfo;        // 登录后用户信息

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();	// 获取当前线程的id
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取————主线程的ID
     * @return
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }


    /**********************************登录信息******************************************/
    /**
     * 返回登录状态
     * @return
     */
    public static boolean isLogin() {
        return isLogin;
    }

    /**
     * 设置当前有没有登录
     * @param isLogin   登录标志
     */
    public static void setIsLogin(boolean isLogin) {
        AccountBookApplication.isLogin = isLogin;
    }

    /**
     * 获取登录后用户信息
     * @return
     */
    public static UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 设置登录后用户信息
     * @param userInfo
     */
    public static void setUserInfo(UserInfo userInfo) {
        AccountBookApplication.userInfo = userInfo;
    }
}
