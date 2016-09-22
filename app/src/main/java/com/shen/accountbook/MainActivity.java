package com.shen.accountbook;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shen.accountbook.fragment.ContentFragment;
import com.shen.accountbook.fragment.LeftMenuFragment;

/**
 * Created by shen on 8/15 0015.
 */
public class MainActivity extends SlidingFragmentActivity{

    /**	左侧边栏  标记*/
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    /**	主面板  标记*/
    private static final String TAG_CONTENT = "TAG_CONTENT";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 使用SlidingMenu 开源框架
        setBehindContentView(R.layout.left_menu);			// 设置"左侧边栏"
        SlidingMenu slidingMenu = getSlidingMenu();			// new SlidingMenu类
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 全屏触摸

        // 应该使用   "比例"，不要使用像素
        // slidingMenu.setBehindOffset(200);				// 屏幕预留200像素宽度
        // 屏幕预留xxx宽度
        // ***200/320 * 屏幕宽度
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width * 120 / 320);

        initFragment();

    }


    /**
     * 初始化fragment
     *
     * <p>用fragment替换帧布局;
     */
    private void initFragment() {
        // 拿"兼容"的FragmentManager(注意继承SlidingFragmentActivity)
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();	// 开始事务

        // 用fragment替换帧布局;
        // ***参1:帧布局容器的id;
        // ***参2:是要替换的fragment;
        // ***参3:标记
        // ******根据标记找到对应的fragment
        // ******Fragment fragment = fm.findFragmentByTag(TAG_LEFT_MENU);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);

        transaction.commit();										// 提交事务

    }

    /**
     *  获取侧边栏fragment对象
     *
     * @return	LeftMenuFragment
     */
    public LeftMenuFragment getLeftMenuFragment() {

        FragmentManager fm = getSupportFragmentManager();
        // 根据标记找到对应的fragment
        LeftMenuFragment fragment =
                (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     *  获取主页fragment对象
     * @return ContentFragment
     */
    public ContentFragment getContentFragment() {

        FragmentManager fm = getSupportFragmentManager();

        // 根据标记找到对应的fragment
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }


}
