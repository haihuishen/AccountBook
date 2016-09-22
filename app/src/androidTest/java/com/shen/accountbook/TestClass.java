package com.shen.accountbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import com.shen.accountbook.Utils.toFormatUtil;
import com.shen.accountbook.db.Helper.DatabaseHelper;
import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.table.BaseEx;
import com.shen.accountbook.db.table.TableEx;
import com.shen.accountbook.db.table.UserEx;

import java.util.Random;

/**
 * Created by shen on 9/4 0004.
 */
public class TestClass extends InstrumentationTestCase{

    public void test() throws  Exception{


//        TableEx consumptionEx = new TableEx(getInstrumentation().getContext(), Constant.TABLE_CONSUMPTION);
////        System.out.println(getInstrumentation().getContext().getFilesDir().getPath());
//
//        consumptionEx.openDBConnect();
//        consumptionEx.getDb().execSQL("insert into consumption values(null,'就餐', '午餐', '自定义', 12.51, null, date('2004-08-13 00:00:00'))");
//

//        db.execSQL("create table if not exists consumption(id integer primary key autoincrement," +
//                "maintype varchar(20) not null,type1 varchar(20),concreteness varchar(30)," +
//                "price REAL not null,image varchar(50),date date not null)");

 //        consumptionEx.getDb().execSQL("insert into consumption values(null,'就餐', '午餐', '自定义', 12.51, null, date('2004-08-13 00:00:00'))");
        String path = "data/data/com.shen.accountbook/databases/AccountBook.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY );//读写","若不存在则创建
//        db.execSQL("insert into consumption values(null,'就餐1', '午餐', '自定义', 12.51, null, date('2004-08-13 00:00:00'))");
      String image = null;
        String id = null;
        ContentValues values = new ContentValues();
//        values.put("id", id);// 空不要写成 ""  ，不要写成 "null" 要写成String id = null;  主键可以不写
        values.put("maintype", "就餐");                        // 字段  ： 值
        values.put("type1", "午餐");
        values.put("concreteness", "自定义");
        values.put("price", 12);
        values.put("image", image);  // 空不要写成 ""  ，不要写成 "null" 要写成String image = null;
//        values.put("date", "date("+"'2016-09-12 00:00:00'"+")");//1092941466
//        values.put("date", "1092941466");
        values.put("date", "2016-09-12");
//
//
        long i = db.insert(Constant.TABLE_CONSUMPTION, null, values);
        assertEquals(1, i);
        System.out.println(i+"");

    }

    public void test1() throws  Exception {
        String path = "data/data/com.shen.accountbook/databases/AccountBook.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY );//读写、若不存在则创建

        ContentValues values = new ContentValues();
        values.put("name", "神1");                        // 字段  ： 值
        values.put("password", "123");
        values.put("sex", 1);
        long i = db.insert(Constant.TABLE_USER, "name", values);
        assertEquals(1, i);
        System.out.println(i+"");
    }


    public void test2() throws  Exception{
        String path = "data/data/com.shen.accountbook/databases/AccountBook.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY );//读写、若不存在则创建

        Cursor cursor = db.query(Constant.TABLE_CONSUMPTION,new String[]{"price"}, "id=?",new String[]{"4"},null,null,null,null);

        cursor.moveToNext();
        float a = cursor.getFloat(0);
        System.out.println(a+"");
        assertEquals(1, a);
    }








    public void test3() throws  Exception{
        final int MAINTTPE = 12;
        int TYPE1 = 0;
        int mt_Random = 0;
        int t1_Random = 0;
        int M = 1;
        int day = 1;



        String path = "data/data/com.shen.accountbook/databases/AccountBook.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY );//读写、若不存在则创建


        String[] mt ={"就餐","交通","零食","蒲","教育","生活用品","各种月费",
                "家电","电子产品","交通工具","游戏","其他"};

        String[] ty0 ={"早餐","午餐","晚餐","夜宵","饭局","非正餐","其他"};
        String[] ty1 ={"船","火车","地铁","公交车","飞机","神州n号","的士","快车","其他"};
        String[] ty2 ={"饮料","酒","其他"};
        String[] ty3 ={"喝酒","唱K","其他"};
        String[] ty4 ={"学车","夜校","孩子学费","技能考试","其他"};
        String[] ty5 ={"洗头水","沐浴露","牙膏","洗衣粉","毛巾","其他"};
        String[] ty6 ={"仅房租","房租+水电","水费","电费","话费","其他"};
        String[] ty7 ={"电视","洗衣机","空调","其他"};
        String[] ty8 ={"手机","手表","其他"};
        String[] ty9 ={"自行车","摩托","其他"};
        String[] ty10 ={"其他"};
        String[] ty11 ={"其他"};

        Random ra =new Random();



        for(int i = 0; i<3000; i++) {
            String type1 = null;
            String maintype = null;
            float price = 0;
            int number = 0;
            float unitPrice = 0;

            mt_Random = ra.nextInt(MAINTTPE);
            maintype = mt[mt_Random];

            switch (mt_Random){
                case 0: TYPE1 = 7;t1_Random = ra.nextInt(TYPE1);type1 = ty0[t1_Random]; break;
                case 1: TYPE1 = 9;t1_Random = ra.nextInt(TYPE1);type1 = ty1[t1_Random]; break;
                case 2: TYPE1 = 3;t1_Random = ra.nextInt(TYPE1);type1 = ty2[t1_Random]; break;
                case 3: TYPE1 = 3;t1_Random = ra.nextInt(TYPE1);type1 = ty3[t1_Random]; break;
                case 4: TYPE1 = 5;t1_Random = ra.nextInt(TYPE1);type1 = ty4[t1_Random]; break;
                case 5: TYPE1 = 6;t1_Random = ra.nextInt(TYPE1);type1 = ty5[t1_Random]; break;
                case 6: TYPE1 = 6;t1_Random = ra.nextInt(TYPE1);type1 = ty6[t1_Random]; break;
                case 7: TYPE1 = 4;t1_Random = ra.nextInt(TYPE1);type1 = ty7[t1_Random]; break;
                case 8: TYPE1 = 3;t1_Random = ra.nextInt(TYPE1);type1 = ty8[t1_Random]; break;
                case 9: TYPE1 = 3;t1_Random = ra.nextInt(TYPE1);type1 = ty9[t1_Random]; break;
                case 10: TYPE1 = 1;t1_Random = ra.nextInt(TYPE1);type1 = ty10[t1_Random]; break;
                case 11: TYPE1 = 1;t1_Random = ra.nextInt(TYPE1);type1 = ty11[t1_Random]; break;
            }

            number = ra.nextInt(10)+1;
            price = ra.nextFloat() * (1000 - 100) + 1;
            unitPrice = ra.nextFloat() * (100 - 1) + 1;

            String id = null;   // 空不要写成 ""  ，不要写成 "null" 要写成String id = null;  主键可以不写
       //     String maintype = null;
      //      String type1 = null;
//            String concreteness = null;
            String concreteness = "自定义";
//            float price = 0;
//            int number = 0;
//            float unitPrice = 0;
            String image = null;    // 空不要写成 ""  ，不要写成 "null" 要写成String image = null;
            String date = null;

            if(0<M && M<12) {
                if(0<day && day<=28){
                    date = "2016-"+M+"-"+day;
                    day++;
                }else{
                    M++;
                    day = 1;
                    date = "2016-"+M+"-"+day;
                    day++;
                }
            }
            else{
                M = 1;
                if(0<day && day<=28){
                    date = "2016-"+M+"-"+day;
                    day++;
                }else{
                    M++;
                    day = 1;
                    date = "2016-"+M+"-"+day;
                    day++;
                }
            }

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

            db.insert(Constant.TABLE_CONSUMPTION,"",values);

        }
    }
}
