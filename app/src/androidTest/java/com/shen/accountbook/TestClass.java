package com.shen.accountbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.InstrumentationTestCase;

import com.shen.accountbook.db.table.BaseEx;
import com.shen.accountbook.db.table.UserEx;

/**
 * Created by shen on 9/4 0004.
 */
public class TestClass extends InstrumentationTestCase{

    public void test() throws  Exception{

        UserEx userEx = new UserEx(getInstrumentation().getContext());

        for(int i=30; i<50; i++) {
            ContentValues values = new ContentValues();
            values.put("name", "shen" + i);                        // 字段  ： 值
            values.put("password", i);
            //values.put("sex", 1);
            userEx.Add(values);
        }

        Cursor cursor = userEx.Query(new String[]{"name,password"},null,null,null,null,null);


//        System.out.println("条数"+cursor.getCount());
//        while(cursor.moveToNext()) {
//
            String name = cursor.getString(0);
            String password = cursor.getString(1);
//            int sex = cursor.getInt(2);
            System.out.println(name + "_" + password + "_" );
//        }
//    Cursor cursor = database.query("person", new String[]{"name,age,phone"}, "name=?", new String[]{"heima"}, null, null, null);
//        int id = cursor.getInt(0);
//        String name = cursor.getString(1);
//        String phone = cursor.getString(2);
//        System.out.println(id + "_" + name + "_" + phone);
    }
}
