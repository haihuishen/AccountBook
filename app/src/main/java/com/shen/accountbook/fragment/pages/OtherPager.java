package com.shen.accountbook.fragment.pages;

import android.app.Activity;
import android.view.View;

import com.shen.accountbook.R;

/**
 * Created by shen on 9/9 0009.
 */
public class OtherPager extends BasePager {

    public OtherPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        // 將 R.layout.base_pager布局 填充成 view,作为其布局
		View view = View.inflate(mActivity, R.layout.pager_other, null);
        return view;
    }
}
