package com.shen.accountbook.db.constant;

import com.shen.accountbook.db.model.MainTypeModel;

import java.util.List;

/**
 * 常量
 *
 * */
public class Constant {

    /** 数据库名 */
	public final static String DB_NAME = "AccountBook.db";


    public final static String TABLE_USER = "user";
    public final static String TABLE_MAINTYPE = "MainType";
    public final static String TABLE_TYPE1 = "Type1";
    public final static String TABLE_CONSUMPTION = "consumption";


    public final static String[] dialogYear = new String[]{
            "1980","1981","1982","1983","1984","1985","1986","1987","1988","1989", 
            "1990","1991","1992","1993","1994","1995","1996","1997","1998","1999",
            "2000","2001","2002","2003","2004","2005","2006","2007","2008","2009", 
            "2010","2011","2012","2013","2014","2015","2016","2017","2018","2019",
            "2020","2021","2022","2023","2024","2025","2026","2027","2028","2029",
            "2030","2031","2032","2033","2034","2035","2036","2037","2038","2039",
    };

    public final static String[] dialogMonth = new String[]{
            "1","2","3","4","5","6","7","8","9","10","11","12"
    };


    public final static List<MainTypeModel> mainTypeList = null;
}
