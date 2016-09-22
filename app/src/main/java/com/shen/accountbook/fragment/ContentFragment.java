package com.shen.accountbook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shen.accountbook.MainActivity;
import com.shen.accountbook.R;
import com.shen.accountbook.fragment.pages.AddPager;
import com.shen.accountbook.fragment.pages.BasePager;
import com.shen.accountbook.fragment.pages.MainPager;
import com.shen.accountbook.fragment.pages.OtherPager;
import com.shen.accountbook.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by shen on 8/25 0025.
 */
public class ContentFragment extends Fragment {

    /**
     * 获取当前fragment所依赖的activity
     * <br>这个activity就是MainActivity
     */
    public Activity mActivity;

    public NoScrollViewPager mViewPager;


    /** 下面的"底栏标签"(RadioButton)的 组
     * <p>private RadioGroup rgGroup;
     */
    private RadioGroup rgGroup;

    /**
     * 三个标签页的集合
     * <p>private ArrayList<BasePager> mPagers;
     */
    private ArrayList<BasePager> mPagers;

    TextView tv_title;
    ImageButton btnMenu;
    /**
     *  Fragment创建
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取当前fragment所依赖的activity
        mActivity = getActivity();
    }

    /**
     *  初始化fragment的布局
     *  <br>创建布局
     *
     *  先调用onCreate 再调用onCreateView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = initView();
        initData();
        return view;
    }

    public View initView() {

        // 將R.layout.fragment_content布局填充成 view 作为 "本Fragment的布局"
        // ***注意上下文对象：获取当前fragment所依赖的activity
       View view = View.inflate(mActivity, R.layout.fragment_content, null);

        tv_title = (TextView)view.findViewById(R.id.tv_title);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);

        return view;
    }

    private void initData(){

        mPagers = new ArrayList<BasePager>();
        // 添加五个标签页
        mPagers.add(new MainPager(mActivity));
        mPagers.add(new AddPager(mActivity));
        mPagers.add(new OtherPager(mActivity));
        // 给ViewPage添加 适配器
        mViewPager.setAdapter(new ContentAdapter());

        // 标题的导航键（菜单按钮） 设置 点击监听
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开或者关闭侧边栏
                toggle();
            }
        });

        // "底栏标签" 切换监听
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:						// 首页

                        // mViewPager.setCurrentItem(0);
                        // 参2:表示是否具有滑动动画
                        mViewPager.setCurrentItem(0, false);
                        tv_title.setText("首页");
                        break;
                    case R.id.rb_add:						// 添加

                        mViewPager.setCurrentItem(1, false);
                        tv_title.setText("添加");
                        break;
                    case R.id.rb_other:						// 其他

                        mViewPager.setCurrentItem(2, false);
                        tv_title.setText("其他");
                        break;

                    default:
                        break;
                }
            }
        });

        rgGroup.check(R.id.rb_home);   // 默认选中"首页"
    }

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
     * viewpager 的 适配器
     *
     * <p>class ContentAdapter extends PagerAdapter
     *
     */
    class ContentAdapter extends PagerAdapter {

        /**  item(项)的个数*/
        @Override
        public int getCount() {
            return mPagers.size();
        }

        /**
         * 判断 arg1是不是 view,(好像是判断这个Object有没有被销毁 )
         * <p>是就可以复用
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /** 初始化item(项)布局*/
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // 將 哪一个布局,弄成标签
            BasePager pager = mPagers.get(position);
            // 获取当前页面对象的布局
            View view = pager.mRootView;

            // 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法
            // pager.initData();
            container.addView(view);

            return view;
        }

        /**
         *  销毁item
         *  <p>为什么要销毁：因为"ViewPage"缓存的"页"太大,会溢出
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
