package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.shen.accountbook.R;
import com.shen.accountbook.ReportForDayActivity;

/**
 * Created by shen on 9/9 0009.
 */
public class MainPager extends BasePager implements View.OnClickListener{

    private Button btn_report;

    public MainPager(Activity activity) {
        super(activity);
        initDate();
    }

    @Override
    public View initView() {

        // 將 R.layout.base_pager布局 填充成 view,作为其布局
		View view = View.inflate(mActivity, R.layout.pager_main, null);

        btn_report = (Button) view.findViewById(R.id.btn_D);
        return view;
    }


    public void initDate(){

        btn_report.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_D:
                Intent intent = new Intent(mActivity.getBaseContext(), ReportForDayActivity.class);
                mActivity.startActivity(intent);
                break;
            default:break;
        }
    }
}
