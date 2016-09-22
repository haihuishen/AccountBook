package com.shen.accountbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.shen.accountbook.clander.CalendarAdapter;
import com.shen.accountbook.clander.Constants;
import com.shen.accountbook.clander.SpecialCalendar;
import com.shen.accountbook.clander.adapter.MySimpleCursorAdapter;
import com.shen.accountbook.clander.adapter.TestAdapter;
import com.shen.accountbook.clander.view.ScrollableLayout;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.table.TableEx;
import com.shen.accountbook.widget.WheelView;
import com.shen.accountbook.widget.adapters.ArrayWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shen on 9/14 0014.
 */
public class ReportForDayActivity extends FragmentActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private ImageButton mMeun;
    private ImageButton mBack;
    private TextView mTitle;


    private GridView gridView = null;                   // 被RelativeLayout  mTopLayout 包裹的
    private ListView mListView;

    private ArrayList<String> mData = new ArrayList<String>();
    private TestAdapter mAdapter;                       // listview测试使用的适配器
    private MySimpleCursorAdapter adapter;                        // 表格使用的!
    /** 这里是使用 一个LinearLayout 包裹一个TextView(放一张图片)当作按键*/
    private LinearLayout mBtnLeft,mBtnRight;
    /** 被ScrollableLayout包裹的--用来包裹GridView*/
    private RelativeLayout mTopLayout;                  // 被ScrollableLayout包裹的
    private ScrollableLayout mScrollLayout;
    /** Gesture Detector手势探测器*/
    private GestureDetector gestureDetector = null;    // Gesture Detector手势探测器
    /** 日历gridview中的每一个item显示的textview<br>
     * 是适配器*/
    private CalendarAdapter calV = null;                // 日历gridview中的每一个item显示的textview
    /** 两个箭头中间的 文本控件*/
    private TextView topText = null;                    // 两个箭头中间的 文本控件
    /** 每次滑动，增加或减去一个月,默认为0（即显示当前月）*/
    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    /** 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)*/
    private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    /** 从系统中拿到当前日期*/
    private String currentDate = "";       // 从系统中拿到当前日期

    /**最终决定的收缩比例值*/
    private float location;             // 最终决定的收缩比例值
    /**记录当天的收缩比例值*/
    private float currentLoction = 1f; // 记录当天的收缩比例值
    /**记录选择那一天的收缩比例值*/
    private float selectLoction = 1f;   // 记录选择那一天的收缩比例值

    /**
     * 本activity的构造函数(在 onCreate之前)
     */
    public ReportForDayActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_day);


        mBack = (ImageButton) findViewById(R.id.btn_back);
        mMeun = (ImageButton) findViewById(R.id.btn_menu);
        mTitle = (TextView) findViewById(R.id.tv_title);

        mMeun.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mTitle.setText("日报表");
        mBack.setOnClickListener(this);


        gridView = (GridView) findViewById(R.id.gridview);
        mListView = (ListView) findViewById(R.id.main_lv);


        mBtnLeft = (LinearLayout) findViewById(R.id.btn_prev_month);
        mBtnRight = (LinearLayout) findViewById(R.id.btn_next_month);
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);

        // TODO 计算当天的位置和收缩比例
        SpecialCalendar calendar = new SpecialCalendar();
        boolean isLeapYear = calendar.isLeapYear(year_c);
        int days = calendar.getDaysOfMonth(isLeapYear,month_c);
        int dayOfWeek = calendar.getWeekdayOfMonth(year_c,month_c);// 指定某年中的某月的第一天是星期几
        //System.out.println("dayOfWeek:"+dayOfWeek);               // 测试过了，第一天是星期四，返回4
        int todayPosition = day_c;
        if (dayOfWeek != 7){                // 指定某年中的某月的第一天是星期几;如果不是：星期日
            days = days + dayOfWeek;        // (前面加上，上个月的最后星期一到本月1号"星期几")
            todayPosition += dayOfWeek -1;  // 下标从0开始---- 好像因为是从星期日开始的(所以星期日是"0")
        }else{                              // 如果是星期日!
            todayPosition -= 1;
        }
        /**
         * 如果 少于或者等于35天显示五行 多余35天显示六行
         * 五行: 收缩比例是：0.25，0.5，0.75，1
         * 六行: 收缩比例是：0.2，0.4，0.6，0.8，1
         * 每行星期1到7
         */
        if (days <= 35){  // 5行==> 当是第一行时，不用缩放，所以从0开始
            Constants.scale = 0.25f;
            // 第一行：0~6  ==> 4-0      currentLoction == 1f
            // 第二行：7~13 ==> 4-1     currentLoction == 0.75f
            currentLoction = (4 - todayPosition/7) * Constants.scale; // todayPosition/7==>今日在第几行
        }else{      // 6行==> 当是第一行时，不用缩放，所以从0开始
            Constants.scale = 0.2f;
            currentLoction = (5 - todayPosition/7) * Constants.scale;
        }
        location = currentLoction;

        mTopLayout = (RelativeLayout) findViewById(R.id.rl_head);
        mScrollLayout = (ScrollableLayout)findViewById(R.id.scrollableLayout);

        // 拿到 OnScrollListener()接口的"实例"
        // 主要是实现  onScroll() 方法
        mScrollLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            // 实现onScroll()方法 (调用此方法是在 ScrollableLayout类中)
            @Override
            public void onScroll(int currentY, int maxY) {

//                System.out.println("currentY:"+currentY);   // 得出的结果是：拉开到最大时，currentY：0
//                                                                //              收到最小时， currentY：382
//                System.out.println("maxY:"+maxY);
                // 移动布局  上下移动
                // 那么ViewHelper.setTranslationY(view,100)
                // 就是把view向下（比最原始的位置）移动100
                ViewHelper.setTranslationY(mTopLayout, currentY * location); // 说明：拉开在最大的时候：mTopLayout布局是没有移动的
                                                                                //    收到最小时：不是应该是 currentY变大，向下移动的吗？
            }
        });

        mScrollLayout.getHelper().setCurrentContainer(mListView); // 直接设置一个 View 进来

        gestureDetector = new GestureDetector(this);  // Gesture Detector手势探测器
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);
        topText = (TextView) findViewById(R.id.tv_month);
        topText.setOnClickListener(this);
        addTextToTopTextView(topText);

        addContentListView(year_c+"", month_c+"", day_c+"");
    }

    /** 添加gridview*/
    private void addGridView() {

        // TODO 如位果滑动到其他月默认定到第一行，划回本月定位到当天那行
        if (jumpMonth == 0){
            location = currentLoction;
        }else{                    // jumpMonth != 0
            location = 1f;
        }
        // TODO 选择的月份 定位到选择的那天
        if (((jumpMonth + month_c)+"").equals(Constants.zMonth)){
            location = selectLoction;
        }
        Log.d("location", "location == " + location + "   currentLoction == " + currentLoction);

        // 宫格的触摸事件
        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件"回传"给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return ReportForDayActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        // 宫格的项被点击后
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //gridView中的每一个item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon(); // 在点击gridView时，得到这个月中第一天的位置
                int endPosition = calV.getEndPosition();    // 在点击gridView时，得到这个月中最后一天的位置
                String scheduleDay;
                String scheduleYear;
                String scheduleMonth;
                location = (float) ((5 - position/7) * 0.2); // 点击的item在哪一行，根据5行，*0.2 收缩

                // 判断条件是：当点击的是"本月"才执行下面代码
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
                    scheduleYear = calV.getShowYear();
                    scheduleMonth = calV.getShowMonth();
                    Constants.zYear = scheduleYear;
                    Constants.zMonth = scheduleMonth;
                    Constants.zDay = scheduleDay;

                    if (Constants.scale == 0.2f){
                        location = (5 - position/7) * Constants.scale;
                    }else{
                        location = (4 - position/7) * Constants.scale;
                    }
                    selectLoction = location;
                    calV.notifyDataSetChanged();    // 适配器的数据改变了，通知一下(刷新)
                    Toast.makeText(ReportForDayActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
                    addContentListView(scheduleYear, scheduleMonth, scheduleDay);

                }
            }

        });
    }

    /** 给listview的适配器添加内容(从数据库中拿到);再适配个listview*/
    public void addContentListView(String year, String month, String day){
        //        for (int i = 0;i<30;i++){
//            mData.add("");
//        }
//
//        mAdapter = new TestAdapter(mData,this);

        TableEx tableEx = new TableEx(this);
//        Cursor cr = tableEx.Query(new String[]{"mainType,type1,concreteness,unitPrice,number,price"},
//                null, null, null, null, null);
//
//        String mainType = cr.getColumnName(0);// 获取第0列
//        String type1 = cr.getColumnName(1);// 获取第1列
//        String concreteness = cr.getColumnName(2);// 获取第2列
//
//        String unitPrice = cr.getColumnName(3);// 获取第3列
//        String number = cr.getColumnName(4);// 获取第4列
//        String price = cr.getColumnName(5);// 获取第5列
        Cursor cr = tableEx.Query(Constant.TABLE_CONSUMPTION, null,"date=?",new String[]{year+"-"+month+"-"+day}, null, null, null);
        String mainType = cr.getColumnName(1);// 获取第1列
        String type1 = cr.getColumnName(2);// 获取第2列
        String concreteness = cr.getColumnName(3);// 获取第3列

        String unitPrice = cr.getColumnName(6);// 获取第6列
        String number = cr.getColumnName(5);// 获取第5列
        String price = cr.getColumnName(4);// 获取第4列

        String[] ColumnNames = { mainType,type1,concreteness, unitPrice, number, price };


        adapter = new MySimpleCursorAdapter(getApplicationContext(),
                R.layout.table_item, cr, ColumnNames,
                new int[] { R.id.tableItem_tv_ProductName_maintype,
                        R.id.tableItem_tv_ProductName_type1,
                        R.id.tableItem_tv_ProductName_concreteness,
                        R.id.tableItem_tv_UnitPrice,
                        R.id.tableItem_tv_Number,
                        R.id.tableItem_tv_Price});
        mListView.setAdapter(adapter);
    }

    /** 添加头部的年份 闰哪月等信息*/
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(
                calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
        view.setTextColor(Color.WHITE);           // 字体颜色：白色
        view.setTypeface(Typeface.DEFAULT_BOLD); // 默认字体
    }

    /** 数据改变了，最后调用一下这个，以便刷新界面*/
    private void upDateView(){
        addGridView();   //添加一个gridView
        // getResources()是获取项目中的资源文件可以用来获取你说的string,xml还可以获取图片，音乐，视频等资源文件。
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(topText);

        Log.d("upDateView()", "calV.getShowYear()== " + calV.getShowYear() + "calV.getShowMonth() == " + calV.getShowMonth() );
        addContentListView(calV.getShowYear(), calV.getShowMonth(), "1");
    }


    // 将本控件的 触摸事件交给——Gesture Detector手势探测器
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_prev_month:
                jumpMonth --;     //上一个月
                upDateView();
                break;
            case R.id.btn_next_month:
                jumpMonth ++;     //下一个月
                upDateView();
                break;
            case R.id.tv_month:
                selectYearAndMonth();
                break;
        }
    }

    /** 新建一个对话框，选择年月*/
    private void selectYearAndMonth() {

        int index_year = 0;
        int index_month = 0;

        for(index_year=0; index_year<Constant.dialogYear.length; index_year++){
            if(calV.getShowYear().equals(Constant.dialogYear[index_year]))
                break;
        }
        for(index_month=0; index_month<Constant.dialogMonth.length; index_month++){
            if(calV.getShowMonth().equals(Constant.dialogMonth[index_month]))
                break;
        }

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_select_y_m,
                (ViewGroup) findViewById(R.id.dialog));

        final WheelView wheelYear = (WheelView) layout.findViewById(R.id.id_year);
        final WheelView wheelMonth = (WheelView) layout.findViewById(R.id.id_month);

        ArrayWheelAdapter<String> yearAdapter = new ArrayWheelAdapter<String>(this, Constant.dialogYear);
        ArrayWheelAdapter<String> monthAdapter = new ArrayWheelAdapter<String>(this, Constant.dialogMonth);
        yearAdapter.setTextSize(30);        // 文本大小
        monthAdapter.setTextSize(30);

        wheelYear.setViewAdapter(yearAdapter);
        wheelMonth.setViewAdapter(monthAdapter);
        // 可见数目，默认是5
        wheelYear.setVisibleItems(3);
        wheelMonth.setVisibleItems(3);

        wheelYear.setCurrentItem(index_year);
        wheelMonth.setCurrentItem(index_month);

        new AlertDialog.Builder(this).setTitle("选择年月").setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String year = Constant.dialogYear[wheelYear.getCurrentItem()];
                        String month = Constant.dialogMonth[wheelMonth.getCurrentItem()];

                        // jumpYear 最后还是会转换成月份来计算的
                        int jY = Integer.valueOf(year) - year_c;
                        jumpMonth = Integer.valueOf(month) - month_c + jY*12;

                        upDateView();
                    }
                })
                .setNegativeButton("取消", null).show();
    }


    /******************************************************************************/
    // 下面的是
    // implements GestureDetector.OnGestureListener  需要的

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            //向左滑动
            jumpMonth++;     //下一个月
            upDateView();
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            jumpMonth--;     //上一个月
            upDateView();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

}
