package com.shen.accountbook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shen.accountbook.LoginActivity;
import com.shen.accountbook.R;
import com.shen.accountbook.RegisterActivity;

/**
 * Created by shen on 8/25 0025.
 */
public class LeftMenuFragment extends Fragment {
    /**
     * 获取当前fragment所依赖的activity
     * <br>这个activity就是MainActivity
     */
    public Activity mActivity;

    private Button bt_login;
    private Button bt_register;

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

        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_register = (Button) view.findViewById(R.id.bt_register);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private View initView() {
        // 將R.layout.fragment_left_menu布局填充成 view 作为 "本Fragment的布局"
        // ***注意上下文对象：获取当前fragment所依赖的activity
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        // xUtils的ViewUtils  （使用"注解"进行UI,资源和事件绑定）
        // ***在：
        // ******@ViewInject(R.id.lv_list)
        // ******private ListView lvList;
        // ***就相当于下面
        // ***lvList = (ListView) view.findViewById(R.id.lv_list);
        //ViewUtils.inject(this, view);// 注入view和事件; 参数2：在"参数2布局"中找"控件"

        return view;
    }

}
