package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.shen.accountbook.LineChartActivity;
import com.shen.accountbook.R;
import com.shen.accountbook.ReportForDayActivity;
import com.shen.accountbook.ReportForM_LineChar_Activity;
import com.shen.accountbook.ReportForType_LineChar_Activity;
import com.shen.accountbook.ReportForY_LineChar_Activity;

/**
 * Created by shen on 9/9 0009.
 */
public class MainPager extends BasePager implements View.OnClickListener{

    private Button btn_report_day;
    private Button btn_report_month;
    private Button btn_report_year;
    private Button btn_report_type;

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
        return view;
    }


    public void initDate(){

        btn_report_day.setOnClickListener(this);
        btn_report_month.setOnClickListener(this);
        btn_report_year.setOnClickListener(this);
        btn_report_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_D:
                intent = new Intent(mActivity.getBaseContext(), ReportForDayActivity.class);
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
            default:break;
        }
    }
}
