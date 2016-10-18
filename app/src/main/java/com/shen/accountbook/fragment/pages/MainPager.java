package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.shen.accountbook.R;
import com.shen.accountbook.ReportForD2_Activity;
import com.shen.accountbook.ReportForD_Activity;
import com.shen.accountbook.ReportForM_LineChar_Activity;
import com.shen.accountbook.ReportForType_LineChar_Activity;
import com.shen.accountbook.ReportForY_LineChar_Activity;
import com.shen.accountbook.Utils.ToastUtil;
import com.shen.accountbook.global.AccountBookApplication;

/**
 * Created by shen on 9/9 0009.
 */
public class MainPager extends BasePager implements View.OnClickListener{

    private Button btn_report_day;
    private Button btn_report_month;
    private Button btn_report_year;
    private Button btn_report_type;

    private Button btn_report_day2;
    private Button btn_report_month2;
    private Button btn_report_year2;
    private Button btn_report_type2;

    public MainPager(Activity activity) {
        super(activity);
        initDate();
    }

    @Override
    public View initView() {

        // 將 R.layout.base_pager布局 填充成 view,作为其布局
        View view = View.inflate(mActivity, R.layout.pager_main, null);

        btn_report_day = (Button) view.findViewById(R.id.btn_D);
        btn_report_month = (Button) view.findViewById(R.id.btn_M);
        btn_report_year = (Button) view.findViewById(R.id.btn_Y);
        btn_report_type = (Button) view.findViewById(R.id.btn_type);

        btn_report_day2 = (Button) view.findViewById(R.id.btn_D2);
        btn_report_month2 = (Button) view.findViewById(R.id.btn_M2);
        btn_report_year2 = (Button) view.findViewById(R.id.btn_Y2);
        btn_report_type2 = (Button) view.findViewById(R.id.btn_type2);
        return view;
    }


    public void initDate(){

        btn_report_day.setOnClickListener(this);
        btn_report_month.setOnClickListener(this);
        btn_report_year.setOnClickListener(this);
        btn_report_type.setOnClickListener(this);

        btn_report_day2.setOnClickListener(this);
        btn_report_month2.setOnClickListener(this);
        btn_report_year2.setOnClickListener(this);
        btn_report_type2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(AccountBookApplication.isLogin()){
            switch (v.getId()){
                case R.id.btn_D:
                    intent = new Intent(mActivity.getBaseContext(), ReportForD_Activity.class);
                    mActivity.startActivity(intent);
                    break;

                case R.id.btn_M:
                    intent = new Intent(mActivity.getBaseContext(), ReportForM_LineChar_Activity.class);
                    mActivity.startActivity(intent);
                    break;

                case R.id.btn_Y:
                    intent = new Intent(mActivity.getBaseContext(), ReportForY_LineChar_Activity.class);
                    mActivity.startActivity(intent);
                    break;

                case R.id.btn_type:
                    intent = new Intent(mActivity.getBaseContext(), ReportForType_LineChar_Activity.class);
                    mActivity.startActivity(intent);
                    break;

                case R.id.btn_D2:
                    intent = new Intent(mActivity.getBaseContext(), ReportForD2_Activity.class);
                    mActivity.startActivity(intent);
                    break;
                default:break;
            }
        }else
            ToastUtil.show("请登陆后再操作!");
    }
}
