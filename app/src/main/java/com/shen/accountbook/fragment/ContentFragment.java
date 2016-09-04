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
import com.shen.accountbook.view.NoScrollViewPager;

/**
 * Created by shen on 8/25 0025.
 */
public class ContentFragment extends Fragment {

    /**
     * 获取当前fragment所依赖的activity
     * <br>这个activity就是MainActivity
     */
    public Activity mActivity;

    public ViewPager mViewPager;

    private int[] mpages;

    /** 下面的"底栏标签"(RadioButton)的 组
     * <p>private RadioGroup rgGroup;
     */
    private RadioGroup rgGroup;

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
        return view;
    }

    public View initView() {

        // 將R.layout.fragment_content布局填充成 view 作为 "本Fragment的布局"
        // ***注意上下文对象：获取当前fragment所依赖的activity
       View view = View.inflate(mActivity, R.layout.fragment_content, null);

        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);

        tv_title.setText("首页");
        mpages= new int[]{R.drawable.viewpage_shouye, R.drawable.viewpage_tianjia, R.drawable.viewpage_qita};
        mViewPager.setAdapter(new ContentAdapter());

        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("shen");
                    switch (checkedId){
                        case R.id.rb_home:
                            mViewPager.setCurrentItem(0);
                            break ;
                        case R.id.rb_add:
                            mViewPager.setCurrentItem(1);
                            break ;
                        case R.id.rb_other:
                            mViewPager.setCurrentItem(2);
                            break ;
                    }

            }
        });

        ImageButton btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        // 标题的导航键（菜单按钮） 设置 点击监听
        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打开或者关闭侧边栏
                toggle();
            }
        });
        return view;
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

        /**
         *  item(项)的个数
         */
        @Override
        public int getCount() {

            return mpages.length;
        }

        /**
         * 判断 arg1是不是 view,(好像是判断这个Object有没有被销毁 )
         * <p>是就可以复用
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        /**
         *  初始化item(项)布局
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if(position == 0)
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
            else
                mViewPager.getParent().requestDisallowInterceptTouchEvent(false);

            ImageView view = new ImageView(mActivity);
            view.setImageResource(mpages[position]);
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
