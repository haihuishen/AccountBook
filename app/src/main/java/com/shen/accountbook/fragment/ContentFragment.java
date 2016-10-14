package com.shen.accountbook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shen.accountbook.MainActivity;
import com.shen.accountbook.R;
import com.shen.accountbook.Utils.ImageFactory;
import com.shen.accountbook.Utils.SharePrefUtil;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.fragment.pages.AddPager;
import com.shen.accountbook.fragment.pages.BasePager;
import com.shen.accountbook.fragment.pages.MainPager;
import com.shen.accountbook.fragment.pages.OtherPager;
import com.shen.accountbook.view.NoScrollViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    /** 预览图片控件*/
    private PhotoView pv_camaraPhoto;
    private LinearLayout linearLayout_pv;

    private Bitmap bitmap;

    View mParent;
    View mBg;
    PhotoView mPhotoView;
    Info mInfo;

    AlphaAnimation in;
    AlphaAnimation out;

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

        mParent = view.findViewById(R.id.parent);
        mBg = view.findViewById(R.id.bg);
        mPhotoView = (PhotoView) view.findViewById(R.id.img);

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


        in = new AlphaAnimation(0, 1);
        out = new AlphaAnimation(1, 0);

        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mBg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        bitmap = ImageFactory.getBitmap(Constant.CACHE_IMAGE_PATH +"/no_preview_picture.png");

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

            // 如果："添加页面"
            if(pager.getClass().equals(AddPager.class)){
                linearLayout_pv = (LinearLayout) view.findViewById(R.id.linearLayout_pv);

                pv_camaraPhoto = (PhotoView) view.findViewById(R.id.pager_add_pv_image);
                pv_camaraPhoto.disenable();// 把PhotoView当普通的控件把触摸功能关掉
                pv_camaraPhoto.setImageBitmap(bitmap);

                Button btnCamera = (Button) view.findViewById(R.id.pager_add_btn_camera);     // 拍照按钮
                Button btnClear = (Button) view.findViewById(R.id.pager_add_btn_clear);     // 清除按钮


                mPhotoView.setImageResource(R.drawable.no_preview_picture);

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        File file = new File(mActivity.getCacheDir(),);
//                        if (!file.exists()) {
//                            boolean b = file.mkdirs();
//                            Toast.makeText(mActivity, "创建文件:"+b, Toast.LENGTH_LONG).show();
//                        }else{
//                            Toast.makeText(mActivity, "文件已经存在", Toast.LENGTH_LONG).show();
//                        }
//
//                        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//                        Toast.makeText(mActivity, name, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Constant.CACHE_IMAGE_PATH ,"CacheImage.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                        startActivityForResult(intent, 1);
                    }
                });
                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharePrefUtil.saveBoolean(mActivity,SharePrefUtil.IMAGE_KEY.IS_ADD_IMAGE,false);

                        bitmap = ImageFactory.getBitmap(Constant.CACHE_IMAGE_PATH +"/no_preview_picture.png");
                        pv_camaraPhoto.setImageBitmap(bitmap);              // 将图片显示在ImageView里
                        mPhotoView.setImageBitmap(bitmap);
                    }
                });

                mPhotoView.enable();
                mPhotoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("草，来到这里了3");
                        Toast.makeText(mActivity, "好像是这个3", Toast.LENGTH_LONG).show();
                        mBg.startAnimation(out);
                        mPhotoView.animaTo(mInfo, new Runnable() {
                            @Override
                            public void run() {
                                mParent.setVisibility(View.GONE);
                            }
                        });
                    }
                });

                linearLayout_pv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        pv_camaraPhoto.setLayoutParams(new LinearLayout.LayoutParams((int) (getResources().getDisplayMetrics().density * 100), (int) (getResources().getDisplayMetrics().density * 100)));
                        mInfo = pv_camaraPhoto.getInfo();

//                        mPhotoView.setImageResource(R.drawable.no_preview_picture);
                        mPhotoView.setImageBitmap(bitmap);
                        mBg.startAnimation(in);
                        mBg.setVisibility(View.VISIBLE);
                        mParent.setVisibility(View.VISIBLE);;
                        mPhotoView.animaFrom(mInfo);
                        Toast.makeText(mActivity,"点击了预览图片",Toast.LENGTH_SHORT).show();

                    }
                });
            }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        //将值传入AddPager/ContentFragment
//        this.getSupportFragmentManager().findFragmentByTag(ContentFragment.class.getSimpleName()).onActivityResult(requestCode, resultCode, data);

        System.out.println("草，来到这里了2");
        Toast.makeText(mActivity, "好像是这个2", Toast.LENGTH_LONG).show();
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
//            String sdStatus = Environment.getExternalStorageState();
//            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                Log.i("TestFile","SD card is not avaiable/writeable right now.");
//                return;
//            }
//            String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//            Toast.makeText(mActivity, name, Toast.LENGTH_LONG).show();
//            Bundle bundle = data.getExtras();
//            bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

//            FileOutputStream b = null;
//            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
//            File file = new File("data/data/com.example.shen.mycamera/image/");
//            file.mkdirs();// 创建文件夹
//            String fileName = "data/data/com.example.shen.mycamera/image/"+name;
//            System.out.println("文件大小"+file.length()+"");
//            try {
//                b = new FileOutputStream(fileName);
//                // 图片压缩
//                // ***参1:图片格式
//                // ***参2:压缩比例0-100; %
//                // ***参3:输出流 (压缩到哪里)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    b.flush();
//                    b.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            SharePrefUtil.saveBoolean(mActivity,SharePrefUtil.IMAGE_KEY.IS_ADD_IMAGE,true);
            System.out.println("拍照完后SharePrefUtil.IMAGE_KEY.IS_ADD_IMAGE："+SharePrefUtil.getBoolean(mActivity, SharePrefUtil.IMAGE_KEY.IS_ADD_IMAGE, false));
            bitmap = ImageFactory.ratio(Constant.CACHE_IMAGE_PATH +"/CacheImage.jpg", 200, 200);
            pv_camaraPhoto.setImageBitmap(bitmap);// 将图片显示在ImageView里
        }
    }

}
