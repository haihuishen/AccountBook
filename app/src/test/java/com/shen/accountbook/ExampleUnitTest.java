package com.shen.accountbook;

import android.provider.Settings;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//        System.out.println(false+"");
        System.out.println(5%12+""); // 5
        System.out.println(0%12+""); // 0
        System.out.println(12%12+""); // 0
    }

    @Test
    public void number(){
        //new DecimalFormat("##0.00"); new DecimalFormat("0.00"); 有什么区别吗
        //new DecimalFormat("#")
        //最终结果没有区别，#表示没有则为空，0表示如果没有则该位补0.
        float a = (float) 2.433333334;
        DecimalFormat formatter1=new DecimalFormat("#0.00");
        System.out.println(formatter1.format(a));

        String b = formatter1.format(a);
        float c = Float.valueOf(b);
        System.out.println(c+"");
    }
    @Test
    public void time(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

//            Date now = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
//
//            String hehe = dateFormat.format( now );
    }
    @Test
    public void time1(){
    Random ra =new Random();

    for(int i = 0; i<10; i++) {
        int aa = ra.nextInt(10);
        System.out.println(aa);
    }
    }

    @Test
    public void time2(){
        Random ra =new Random();

        for(int i = 0; i<10; i++) {
            double aa = ra.nextDouble();
            System.out.println(aa);
        }
    }
//    JAVA中获取当前系统时间
//    一. 获取当前系统时间和日期并格式化输出:
//
//    import java.util.Date;
//    import java.text.SimpleDateFormat;
//
//    public class NowString {
//        public static void main(String[] args) {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//        }
//    }
//
//    二. 在数据库里的日期只以年-月-日的方式输出，可以用下面两种方法：
//
//    1、用convert()转化函数：
//    String sqlst = "select convert(varchar(10),bookDate,126) as convertBookDate from roomBook where bookDate between '2007-4-10' and '2007-4-25'";
//    System.out.println(rs.getString("convertBookDate"));
//
//    2、利用SimpleDateFormat类：
//    先要输入两个java包：
//    import java.util.Date;
//    import java.text.SimpleDateFormat;
//
//    然后：
//    定义日期格式：SimpleDateFormat sdf = new SimpleDateFormat(yy-MM-dd);
//
//    sql语句为：String sqlStr = "select bookDate from roomBook where bookDate between '2007-4-10' and '2007-4-25'";
//    输出：
//            System.out.println(df.format(rs.getDate("bookDate")));
//
//    ************************************************************
//    java中获取当前日期和时间的方法
//    import java.util.Date;
//    import java.util.Calendar;
//    import java.text.SimpleDateFormat;
//
//    public class TestDate{
//        public static void main(String[] args){
//            Date now = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
//
//            String hehe = dateFormat.format( now );
//            System.out.println(hehe);
//
//            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int date = c.get(Calendar.DATE);
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//            int second = c.get(Calendar.SECOND);
//            System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second);
//        }
//    }
//
//    有时候要把String类型的时间转换为Date类型，通过以下的方式，就可以将你刚得到的时间字符串转换为Date类型了。
//    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//
//    java.util.Date time=null;
//    try {
//        time= sdf.parse(sdf.format(new Date()));
//    } catch (ParseException e) {
//        e.printStackTrace();
//    }
}