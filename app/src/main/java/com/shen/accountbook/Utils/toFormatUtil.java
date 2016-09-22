package com.shen.accountbook.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by shen on 9/13 0013.
 */
public class toFormatUtil {


    /**
     * 根据传递进来的 src 源数据， 和截取的位数number，截取小数点<p>
     *
     * @param src       要转换的数据
     * @param number   小数点位数(1~7位;超过默认是2位)
     * @return
     */
    public static String toDecimalFormat(float src, int number){

        String n = "";
        switch (number){
            case 1: n = "#.0"; break;
            case 2: n = "#.00"; break;
            case 3: n = "#.000"; break;
            case 4: n = "#.0000"; break;
            case 5: n = "#.00000"; break;
            case 6: n = "#.000000"; break;
            case 7: n = "#.0000000"; break;
            default: n = "#.00"; break;
        }
        DecimalFormat df=new DecimalFormat(n);
        String  a= df.format(src);

        return a;
    }
}
