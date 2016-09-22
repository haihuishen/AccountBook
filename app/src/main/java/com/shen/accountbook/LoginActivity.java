package com.shen.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shen.accountbook.Utils.SharePrefUtil;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.table.UserEx;

/**
 * Created by shen on 9/1 0001.
 */
public class LoginActivity extends Activity {

    public static final int OK = 1;

    private ImageButton mMeun;
    private ImageButton mBack;
    private TextView mTitle;

    private EditText mUsename;
    private EditText mPassword;

    /**记录登录密码*/
    private CheckBox mRemember;
    private CheckBox mAotu;

    private Button mLogin;

    private String c_name;
    private String c_password;
    private int c_sex;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        initView();
        initListend();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.btn_back);
        mMeun = (ImageButton) findViewById(R.id.btn_menu);
        mTitle = (TextView) findViewById(R.id.tv_title);

        mUsename = (EditText) findViewById(R.id.login_et_username);
        mPassword = (EditText) findViewById(R.id.login_et_password);

        mRemember = (CheckBox) findViewById(R.id.login_cb_remember);
        mAotu = (CheckBox) findViewById(R.id.login_cb_auto);
        mLogin = (Button) findViewById(R.id.login_btn_login);
    }

    private void initListend() {
        mMeun.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mTitle.setText("登录界面");

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    System.out.println("选择了");
//                    Toast.makeText(LoginActivity.this, "ON", Toast.LENGTH_SHORT).show();
//                } else {
//                    System.out.println("不选择了");
//                    Toast.makeText(LoginActivity.this, "OFF", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        mAotu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                    SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.AUTO_ISCHECK, mAotu.isChecked());
//                else
//                    SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.AUTO_ISCHECK, mAotu.isChecked());
//            }
//        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUsename.getText().toString()) ||
                        TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(getBaseContext(), "用户和密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    UserEx userEx = new UserEx(getApplication());

                    Cursor cursor = userEx.Query(Constant.TABLE_USER,new String[]{"name,password,sex"}, "name=? and password=?",
                            new String[]{mUsename.getText().toString(),mPassword.getText().toString()},null,null,null);

                    if(cursor.getCount() >= 1) {
                        cursor.moveToNext();
                        c_name = cursor.getString(0);
                        c_password = cursor.getString(1);
                        c_sex = cursor.getInt(2);

                        Toast.makeText(getBaseContext(), c_name + ":" + c_password + ":" + c_sex, Toast.LENGTH_SHORT).show();

                        if (mRemember.isChecked()) {
                            SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.REMEMBER_ISCHECK, mRemember.isChecked());
                            SharePrefUtil.saveString(getBaseContext(), SharePrefUtil.KEY.USENAME, mUsename.getText().toString());
                            SharePrefUtil.saveString(getBaseContext(), SharePrefUtil.KEY.PASSWORK, mPassword.getText().toString());
                        } else {
                            SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.REMEMBER_ISCHECK, mRemember.isChecked());
                            SharePrefUtil.saveString(getBaseContext(), SharePrefUtil.KEY.USENAME, "");
                            SharePrefUtil.saveString(getBaseContext(), SharePrefUtil.KEY.PASSWORK, "");
                        }

                        if (mAotu.isChecked())
                            SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.AUTO_ISCHECK, mAotu.isChecked());
                        else
                            SharePrefUtil.saveBoolean(getBaseContext(), SharePrefUtil.KEY.AUTO_ISCHECK, mAotu.isChecked());

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putString("name",c_name);
                        intent.putExtras(bundle);
                        setResult(OK,intent);

                        finish();
                    }
                    else{
                        Toast.makeText(getBaseContext(),"用户或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
//                Toast.makeText(LoginActivity.this,
//                        "用户" + SharePrefUtil.getString(getBaseContext(), SharePrefUtil.KEY.USENAME, "") +
//                                "密码" + SharePrefUtil.getString(getBaseContext(), SharePrefUtil.KEY.PASSWORK, ""), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shen.accountbook/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shen.accountbook/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
