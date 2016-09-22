package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shen.accountbook.MainActivity;

/**
 * 三个"标签页"的基类<p>
 * viewpage的每一个"标签"
 * 
 */
public abstract class BasePager {

	/**
	 * MainActivity对象（主页面）<br>
	 *  BasePager类中public Activity mActivity; <br>
	 */
	public Activity mActivity;

	/** 标题栏的标题*/
	public TextView tvTitle;
	/** 标题的导航键（菜单按钮）*/
	public ImageButton btnMenu;
	/** 
	 * BasePager中空的"帧布局对象"(应是viewpage中的那个FrameLayout)<p>
	 * public FrameLayout flContent;<br>
	 * flContent = (FrameLayout) view.findViewById(R.id.fl_content);<p>
	 * 要动态添加布局
	 */
	public FrameLayout flContent;
	
	/** 组图切换(样式)按钮 */
	public ImageButton btnPhoto;
	/**
	 * 当前页面的布局对象
	 *
	 *<p>在BasePage中已经拿到了这个"对象"
	 *<br>mRootView = initView();
	 */
	public View mRootView;

	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

    /**
     * 初始化布局
     * @return View
     */
    public abstract View initView();
    //{

//		// 將 R.layout.base_pager布局 填充成 view,作为其布局
//		View view = View.inflate(mActivity, R.layout.base_pager, null);
//
//		tvTitle = (TextView) view.findViewById(R.id.tv_title);
//		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
//		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
//
//		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
//
//		// 标题的导航键（菜单按钮） 设置 点击监听
//		btnMenu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//打开或者关闭侧边栏
//				toggle();
//			}
//		});
//
//		return view;
//        return null;
//	}

	/**
	 * 打开或者关闭侧边栏
	 */
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		// 如果当前状态是开, 调用后就关; 反之亦然
		slidingMenu.toggle();
	}

	/**
	 *  初始化"标签"的数据<p>
	 *  
	 *  空的,等我们写
	 */
	public void initData() {

	}
}
