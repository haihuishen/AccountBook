package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.content.ContentValues;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shen.accountbook.R;
import com.shen.accountbook.Utils.toFormatUtil;
import com.shen.accountbook.db.model.ConsumptionMainType;
import com.shen.accountbook.db.model.ConsumptionType1;
import com.shen.accountbook.db.model.TypeManage;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.model.TypeModelManage;
import com.shen.accountbook.db.table.TableEx;
import com.shen.accountbook.widget.OnWheelChangedListener;
import com.shen.accountbook.widget.WheelView;
import com.shen.accountbook.widget.adapters.ArrayWheelAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shen on 9/9 0009.
 */
public class AddPager extends BasePager  implements OnWheelChangedListener {

    /** 时间选择控件*/
    private DatePicker datePicker;
    /** 主类型下拉框*/
    private Spinner sp_maintype;
    /** 次类型下拉框*/
    private Spinner sp_type1;

    /** 主类型滑动选择控件*/
    private WheelView wv_maintype;
    /** 次类型滑动选择控件*/
    private WheelView wv_type1;

    /** 具体类型编辑框*/
    private EditText et_concreteness;
    /** 总价编辑框*/
    private EditText et_price;
    /** 数量编辑框*/
    private EditText et_number;
    /** 单价编辑框*/
    private EditText et_unitPrice;

    /** 添加按钮*/
    private Button btn_add;

    private TypeManage typeManage;

    private String[] s_mainType;

    private List<String> list_mainType;
    private Map<Integer,ArrayList<ConsumptionType1>> map_list_Type1;
    private TypeModelManage m;


    public AddPager(Activity activity) {
        super(activity);
        initData();
    }

    @Override
    public View initView() {

        // 將 R.layout.base_pager布局 填充成 view,作为其布局
		View view = View.inflate(mActivity, R.layout.pager_add, null);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        sp_maintype = (Spinner) view.findViewById(R.id.spinner_maintype);
        sp_type1 = (Spinner) view.findViewById(R.id.spinner_type1);

        wv_maintype = (WheelView) view.findViewById(R.id.wheel_maintype);
        wv_type1 = (WheelView) view.findViewById(R.id.wheel_type1);

        et_concreteness = (EditText) view.findViewById(R.id.et_concreteness);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_unitPrice = (EditText) view.findViewById(R.id.et_unitPrice);
        btn_add = (Button) view.findViewById(R.id.pager_add_btn_add);

        return view;
    }


    @Override
    public void initData() {
        typeManage = new TypeManage(mActivity);
        list_mainType = new ArrayList<String>();

        if(!typeManage.list_MainType.isEmpty()){
            for (ConsumptionMainType mt : typeManage.list_MainType){
                list_mainType.add(mt.getType());
            }
            s_mainType = new String[list_mainType.size()];
            list_mainType.toArray(s_mainType);              // 将 List<String> 转换成 String[]
        }else
            Toast.makeText(mActivity,"为空",Toast.LENGTH_SHORT).show();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_dropdown_item, s_mainType);
        //绑定 Adapter到控件
        sp_maintype.setAdapter(adapter);

        /**
         * 给spinner绑定选中事件，当选中某一项时触发该事件
         */
        sp_maintype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * 某个条目被选中后调用此函数
             * 第三个参数 position 代表选中条目的索引
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*
				 * 调用Spinner的getItemAtPosition(int position)方法，获取选中的对象
				 * 强制转换为String类型
				 */
                String type = (String) sp_maintype.getItemAtPosition(position);
                String[] type1 = typeManage.getType1(type);
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_dropdown_item, type1);
                //绑定 Adapter到控件
                sp_type1.setAdapter(adapter1);

            }

            /*
             * 什么都没有选择时调用次函数
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_maintype.setId(0);

        m = new TypeModelManage(mActivity);
        wv_maintype.setVisibleItems(3);
        wv_type1.setVisibleItems(3);

        wv_maintype.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, m.mainType()));
        wv_type1.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, m.type1(wv_maintype.getCurrentItem())));

        // 添加change事件
        wv_maintype.addChangingListener(this);
        // 添加change事件
        wv_type1.addChangingListener(this);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add();
            }
        });
    }

    private void add(){
        TableEx consumptionEx = new TableEx(mActivity);

        String id = null;   // 空不要写成 ""  ，不要写成 "null" 要写成String id = null;  主键可以不写
        String maintype = null;
        String type1 = null;
        String concreteness = null;
        float price = 0;
        int number = 0;
        float unitPrice = 0;
        String image = null;    // 空不要写成 ""  ，不要写成 "null" 要写成String image = null;
        String date = null;

//        maintype = sp_maintype.getSelectedItem().toString();
//        type1 = sp_type1.getSelectedItem().toString();
        maintype = m.mainType()[wv_maintype.getCurrentItem()];
        type1 = m.type1(wv_maintype.getCurrentItem())[wv_type1.getCurrentItem()];

        concreteness = et_concreteness.getText().toString();
        price = Float.valueOf(et_price.getText().toString());
        number = Integer.valueOf(et_number.getText().toString());
        unitPrice = Float.valueOf(et_unitPrice.getText().toString());
        date = getDatePicker();

        System.out.println("maintype:" + maintype+
                "type1:" + type1+
                "concreteness:" + concreteness+
                "price:" + price+
                "number:" + number+
                "unitPrice:" + unitPrice+
                "date:" + date
        );

        ContentValues values = new ContentValues();
     //   values.put("id", id);                   // 主键可以不写
        values.put("maintype", maintype);                        // 字段  ： 值
        values.put("type1", type1);
        values.put("concreteness", concreteness);
        values.put("price", toFormatUtil.toDecimalFormat(price, 2));
        values.put("number", number);
        values.put("unitPrice", toFormatUtil.toDecimalFormat(unitPrice, 2));
        values.put("image", image);
        values.put("date", date);   // 这里只要填写 YYYY-MM-DD  ，不用填date(2016-09-12 00:00:00) 这么麻烦
        consumptionEx.Add(Constant.TABLE_CONSUMPTION, values);

//        consumptionEx.openDBConnect();
//        consumptionEx.getDb().execSQL("insert into consumption values(null,'就餐', '午餐', '自定义', 12.51, null, date('2004-08-13 00:00:00'))");

    }

    /**
     * 将 拿到"年月日==>""YYYY-MM-DD"<p>
     *
     * @return String YYYY-MM-DD
     */
    private String getDatePicker(){

        NumberFormat nf = NumberFormat.getInstance();   // 得到一个NumberFormat的实例
        nf.setGroupingUsed(false);                      // 设置是否使用分组
        nf.setMaximumIntegerDigits(2);                  // 设置最大整数位数
        nf.setMinimumIntegerDigits(2);                  // 设置最小整数位数

        int y = datePicker.getYear();
        int m = datePicker.getMonth();          // 记得"补零"
        int d = datePicker.getDayOfMonth();     // 记得"补零"

        String s_date = y + "-" + nf.format(m) + "-" + nf.format(d);        // YYYY-MM-DD
//        String s_date = y + nf.format(m) + nf.format(d);        // YYYY-MM-DD
//        s_date = s_date+"00:00:00";  // HH:MM:SS.SSS   date(2016-09-12 00:00:00)

        return s_date;
//        s_date = s_date+" "+"00:00:00";
//        System.out.println("date:y:" + y);
//        System.out.println("date:y+m:" + y + nf.format(m));
//        System.out.println("date:y+m+d:"+ y + nf.format(m) + nf.format(d));
//
//        Toast.makeText(mActivity.getApplicationContext(),"date:y:"+y, Toast.LENGTH_SHORT).show();
//        Toast.makeText(mActivity.getApplicationContext(),"date:y+m:" + y + nf.format(m), Toast.LENGTH_SHORT).show();
//        Toast.makeText(mActivity.getApplicationContext(),"date:y+m+d:"+ y + nf.format(m) + nf.format(d), Toast.LENGTH_SHORT).show();



        //        /**
        //         * Java里数字转字符串前面自动补0的实现。
        //         *
        //         */
        //        public class TestStringFormat {
        //            public static void main(String[] args) {
        //                int youNumber = 1;
        //                // 0 代表前面补充0
        //                // 4 代表长度为4
        //                // d 代表参数为正数型
        //                String str = String.format("%04d", youNumber);
        //                System.out.println(str); // 0001
        //            }
        //        }

        //Java 中给数字左边补0
        //        // 待测试数据
        //        int i = 1;
        //        // 得到一个NumberFormat的实例
        //        NumberFormat nf = NumberFormat.getInstance();
        //        // 设置是否使用分组
        //        nf.setGroupingUsed(false);
        //        // 设置最大整数位数
        //        nf.setMaximumIntegerDigits(4);
        //        // 设置最小整数位数
        //        nf.setMinimumIntegerDigits(4);
        //        // 输出测试语句
        //        System.out.println(nf.format(i));
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wv_maintype) {
            wv_type1.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, m.type1(wv_maintype.getCurrentItem())));
        }
    }
}
