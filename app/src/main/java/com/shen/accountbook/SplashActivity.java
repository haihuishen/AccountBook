package com.shen.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

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
 */
public class SplashActivity extends Activity {

    private Handler handler = new Handler();
    private Runnable runnable;

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)                 // 触摸事件
    {

        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            if (runnable != null)                           // 如果这个(runnable 可执行事件)被new出来了.
                handler.removeCallbacks(runnable);          // 就从"消息队列"删除(这个事件)
        }

        return super.onTouchEvent(event);
    }
}
