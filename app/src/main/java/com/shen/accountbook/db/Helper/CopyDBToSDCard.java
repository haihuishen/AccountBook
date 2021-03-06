package com.shen.accountbook.db.Helper;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 复制数据库到SD卡/aDBTest/中
 *
 * */
public class CopyDBToSDCard {

	public static boolean CopyDB(Context context) {

		// 获得"DBTest.db" 的位置
		File dbFile = context.getDatabasePath((String) "DBTest.db");

		InputStream myInput;
		try {
			myInput = new FileInputStream(dbFile);

			File file = new File(Environment.getExternalStorageDirectory() + "/aDBTest/");
			if (!file.exists()) {
				file.mkdir();
			}

			OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory()
							+ "/aDBTest/DBTest.db");

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
