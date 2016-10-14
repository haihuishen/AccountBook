package com.shen.accountbook.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shen.accountbook.LoginActivity;
import com.shen.accountbook.R;
import com.shen.accountbook.RegisterActivity;
import com.shen.accountbook.Utils.SharePrefUtil;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.table.UserEx;
import com.shen.accountbook.domain.UserInfo;
import com.shen.accountbook.global.AccountBookApplication;

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

    private String mLogin_User;

    private String mSp_user;
    private String mSp_password;

    static final int REQUEST = 1;
    private String c_name;
    private String c_password;
    private int c_sex;
    private String mSex;

    private TextView mLoginFinishUser;
    private TextView mLoginFinishSex;
    private Button mLoginFinishLogout;


    LinearLayout layout_login;
    LinearLayout layout_logout;
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

        View view = initView();         // 初始化界面

        bt_login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,REQUEST);
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginFinishLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountBookApplication.setIsLogin(false);
                AccountBookApplication.setUserInfo(null);
                layout_logout.setVisibility(View.VISIBLE);
                layout_login.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private View initView() {
        // 將R.layout.fragment_left_menu布局填充成 view 作为 "本Fragment的布局"
        // ***注意上下文对象：获取当前fragment所依赖的activity
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_register = (Button) view.findViewById(R.id.bt_register);

        layout_login = (LinearLayout) view.findViewById(R.id.layout_login_finish);
        layout_logout = (LinearLayout) view.findViewById(R.id.layout_login_and_register);

        mLoginFinishUser = (TextView) view.findViewById(R.id.layout_login_finish_tv_user);
        mLoginFinishSex = (TextView) view.findViewById(R.id.layout_login_finish_tv_sex);
        mLoginFinishLogout = (Button) view.findViewById(R.id.layout_login_finish_btn_logout);

        login();

        return view;
    }

    /**
     * 根据登录标志，登录
     */
    private void login(){
        if(AccountBookApplication.isLogin()){
            UserInfo userInfo = AccountBookApplication.getUserInfo();

            if(userInfo != null){
                mLoginFinishUser.setText(userInfo.getUserName());
                mLoginFinishSex.setText(userInfo.getSex() == 1? "男":"女");
                mLoginFinishUser.setTextSize(25);
                mLoginFinishSex.setTextSize(25);
                layout_logout.setVisibility(View.GONE);
                layout_login.setVisibility(View.VISIBLE);
            }
        }
        else{
            AccountBookApplication.setIsLogin(false);
            AccountBookApplication.setUserInfo(null);
            layout_logout.setVisibility(View.VISIBLE);
            layout_login.setVisibility(View.GONE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST){
            if(resultCode == LoginActivity.OK){
                Bundle bundle = data.getExtras();
                mLogin_User = bundle.getString("name","");

                if(!TextUtils.isEmpty(mLogin_User)){
                    UserEx userEx = new UserEx(getActivity().getApplication());
                    Cursor cursor = userEx.Query(Constant.TABLE_USER,new String[]{"name,password,sex"}, "name=?",
                            new String[]{mLogin_User},null,null,null);

                    if(cursor.getCount() >= 1) {
                        cursor.moveToNext();
                        c_name = cursor.getString(0);
                        c_password = cursor.getString(1);
                        c_sex = cursor.getInt(2);
                        if(c_sex == 1)
                            mSex = "男";
                        else
                            mSex = "女";

                        layout_logout.setVisibility(View.GONE);
                        layout_login.setVisibility(View.VISIBLE);

                        mLoginFinishUser.setText(c_name);
                        mLoginFinishSex.setText(mSex);
                        mLoginFinishUser.setTextSize(25);
                        mLoginFinishSex.setTextSize(25);

                        AccountBookApplication.setIsLogin(true);
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUserName(c_name);
                        userInfo.setPassWord(c_password);
                        userInfo.setSex(c_sex);
                        AccountBookApplication.setUserInfo(userInfo);
                    }
                    else
                        Toast.makeText(getActivity(),"!(cursor.getCount() >= 1)",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(),"传过来的 name 为空",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
