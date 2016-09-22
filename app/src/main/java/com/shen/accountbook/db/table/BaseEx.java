package com.shen.accountbook.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import com.shen.accountbook.db.Helper.DatabaseHelper;
import com.shen.accountbook.db.constant.Constant;

/**
 * 业务操作父类，主要负责数据库的打开与关闭，获取数据库版本。
 *
 * */
public class BaseEx implements BaseInterface {

	private DatabaseHelper dbHelper = null;
	private SQLiteDatabase db = null;
	public Context mContext = null;

	private int dbVersion = 1;

	public BaseEx(Context context) {

		try {
			this.mContext = context;
			this.dbVersion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

    /**
     * 打开数据库
     */
	public void openDBConnect() {
		dbHelper = new DatabaseHelper(mContext, Constant.DB_NAME, 1);
		db = dbHelper.getWritableDatabase();
	}

    /**
     * 关闭数据库
     */
	public void closeDBConnect() {
		if (db != null) {
			db.close();
		}
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	@Override
	public long Add(String table, ContentValues values) {
        return 0;
	}

	@Override
	public void Update(String table, ContentValues values, String whereClause,
                       String[] whereArgs) {

	}

	@Override
	public void Delete(String table, String whereClause, String[] whereArgs) {

	}

	@Override
	public Cursor Query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
		return null;

	}

    /** 得到数据库帮助类 */
	public DatabaseHelper getDbHelper() {
        return dbHelper;
	}
    /** 得到数据库 */
	public SQLiteDatabase getDb() {
        return db;
	}

}
