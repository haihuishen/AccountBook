package com.shen.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shen.accountbook.Utils.SharePrefUtil;
import com.shen.accountbook.db.Helper.DatabaseHelper;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.model.MainTypeModel;
import com.shen.accountbook.db.model.TypeModelManage;
import com.shen.accountbook.db.table.UserEx;
import com.shen.accountbook.domain.UserInfo;
import com.shen.accountbook.global.AccountBookApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 闪屏<p>
 *
 * Splash [splæʃ] vt.溅，泼；用...使液体飞溅	n.飞溅的水；污点；卖弄		vi.溅湿；溅开<p>
 *
 * 现在大部分APP都有Splash界面，下面列一下Splash页面的几个作用：<p>
 * 1、展示logo,提高公司形象<br>
 * 2、初始化数据 (拷贝数据到SD)<br>
 * 3、提高用户体验 <br>
 * 4、连接服务器是否有新的版本等。<br>
 *
 *     //implements UncaughtExceptionHandler
 *     //在onCreate()调用下面方法，才能捕获到线程中的异常
 *      Thread.setDefaultUncaughtExceptionHandler(this);
 */
public class SplashActivity extends Activity implements Thread.UncaughtExceptionHandler {

    private Handler handler = new Handler();
    private Runnable runnable;

    private DatabaseHelper mdbHelper;

    /** APP当前的版本*/
    private int dbVersion = 1;

    private UserEx userEx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(runnable = new Runnable()                   // 发送个消息(runnable 可执行事件)到"消息队列中"，延时执行
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);        // 跳转到主页面
                startActivity(intent);
                finish();
            }
        }, 3000);

        //在此调用下面方法，才能捕获到线程中的异常
        Thread.setDefaultUncaughtExceptionHandler(this);

        initAddressDB("Type.db");
        initData();
        initSrc(Constant.CACHE_IMAGE_PATH,"test.png");
        initSrc(Constant.CACHE_IMAGE_PATH,"no_preview_picture.png");
        initSrc(Constant.IMAGE_PATH,"test.png");
        initLogin();

    }

    private void initData() {
        TypeModelManage manage = new TypeModelManage(getApplicationContext());
        List<MainTypeModel> mainTypeList = manage.getMainTypeList();
        Log.i("shen", "mainTypeList.size()  " + mainTypeList.size());
        for(int i=0; i< mainTypeList.size(); i++){
//            mainTypeList.get(i).getName();
//            String[] type1 = mainTypeList.get(i).getType1().getName();
            Log.i("shen",mainTypeList.get(i).toString());

        }

    }


    /**
     * 拷贝数据库（xx.db）到files文件夹下
     * <br>注1：只在第一次打开应用才拷贝,第二次会判断有没有这个数据库
     * <br>注2：xx.db拷贝到工程目录assets目录下
     * <br>拿到files文件夹：File files = getFilesDir();
     * <br>如：复制后的data/data/com.shen.accountbook/files/xx.db"
     * @param dbName	数据库名称
     */
    private void initAddressDB(String dbName) {
        //1,在files文件夹下创建同名dbName数据库文件过程
        File files = getFilesDir();
        // 在files文件夹下生成一个"dbName名字"的文件
        File file = new File(files, dbName);

        // 如果"dbName名字的文件" 存在  （如第二次进入）
        if(file.exists()){
            return;
        }

        InputStream stream = null;
        FileOutputStream fos = null;

        //2,输入流读取assets目录下的文件
        try {
            // getAssets()拿到"资产目录"的文件夹（工程目录下的assets目录）
            // ***打开"dbName名字的文件"    （拿到他的输入流）
            stream = getAssets().open(dbName);

            //3,将读取的内容写入到指定文件夹的文件中去
            // ***拿到"file文件"的"输出流"
            fos = new FileOutputStream(file);

            //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){	// 將"输入流"（stream）读到"bs"
                fos.write(bs, 0, temp);				// 將"bs"写到"fos"（输出流）
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){	// "流"非等于"null",说明没有关闭
                try {
                    // 关闭流
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制资源(assets目录下的文件)到指定路径
     * @param path          指定路径
     * @param imageName     图片文件名(文件名)
     */
    private void initSrc(String path, String imageName){
        File files = new File(path);
        if (!files.exists()) {
            try {
                //按照指定的路径创建文件夹
                files.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("创建文件夹失败："+path);
            }
        }
        File file = new File(files, imageName);
        if(file.exists()){
            return;
        }

        InputStream stream = null;
        FileOutputStream fos = null;

        //2,输入流读取assets目录下的文件
        try {
            // getAssets()拿到"资产目录"的文件夹（工程目录下的assets目录）
            // ***打开"dbName名字的文件"    （拿到他的输入流）
            stream = getAssets().open(imageName);

            //3,将读取的内容写入到指定文件夹的文件中去
            // ***拿到"file文件"的"输出流"
            fos = new FileOutputStream(file);

            //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){	// 將"输入流"（stream）读到"bs"
                fos.write(bs, 0, temp);				// 將"bs"写到"fos"（输出流）
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){	// "流"非等于"null",说明没有关闭
                try {
                    // 关闭流
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 自动登录<p>
     * 自动登录 ＆＆ 记住密码
     */
    private void initLogin() {
        // 自动登录 ＆＆ 记住密码
        Boolean auto = SharePrefUtil.getBoolean(this, SharePrefUtil.KEY.AUTO_ISCHECK, false);
        Boolean remember = SharePrefUtil.getBoolean(this, SharePrefUtil.KEY.REMEMBER_ISCHECK, false);
        if(auto && remember){
            String mSp_user = SharePrefUtil.getString(this, SharePrefUtil.KEY.USENAME, "");
            String mSp_password = SharePrefUtil.getString(this, SharePrefUtil.KEY.PASSWORK, "");

            // System.out.println( "shen:"+mSp_user + ":" + mSp_password );
            // 从数据库查询
            UserEx userEx = new UserEx(this.getApplication());
            Cursor cursor = userEx.Query(Constant.TABLE_USER,new String[]{"name,password,sex"}, "name=? and password=?",
                    new String[]{mSp_user,mSp_password},null,null,null);

            if(cursor.getCount() >= 1) {
                cursor.moveToNext();
                String c_name = cursor.getString(0);
                String c_password = cursor.getString(1);
                int c_sex = cursor.getInt(2);
                String mSex;
                if(c_sex == 1)
                    mSex = "男";
                else
                    mSex = "女";

                // System.out.println( "shen:"+c_name + ":" + c_password + ":" + c_sex);
                if(mSp_user.equals(c_name) && mSp_password.equals(c_password)){
                    AccountBookApplication.setIsLogin(true);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserName(c_name);
                    userInfo.setPassWord(c_password);
                    userInfo.setSex(c_sex);

                    AccountBookApplication.setUserInfo(userInfo);
                }
                else{
                    Toast.makeText(this,"自动登录失败：用户或密码错误", Toast.LENGTH_SHORT);
                    AccountBookApplication.setIsLogin(false);
                    AccountBookApplication.setUserInfo(null);
                }
            }else{
                AccountBookApplication.setIsLogin(false);
                AccountBookApplication.setUserInfo(null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)                 // 触摸事件
    {

//        if(event.getAction()==MotionEvent.ACTION_UP)
//        {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//            if (runnable != null)                           // 如果这个(runnable 可执行事件)被new出来了.
//                handler.removeCallbacks(runnable);          // 就从"消息队列"删除(这个事件)
//        }

        return super.onTouchEvent(event);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //在此处理异常， arg1即为捕获到的异常
        Log.i("AAA", "uncaughtException   " + ex);
    }
}
