package com.leqienglish.database;

import static com.leqienglish.database.Constants.URL;
import static com.leqienglish.database.Constants.CACHE_TABLE;
import static com.leqienglish.database.Constants.ID;
import static com.leqienglish.database.Constants.CREATETIME;

import static com.leqienglish.database.Constants.JSON;
import static com.leqienglish.database.Constants.DATAVersion;
import static com.leqienglish.database.Constants.MD5;

import com.leqienglish.util.LOGGER;

import static com.leqienglish.database.Constants.DATABASE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLData extends SQLiteOpenHelper {
	private LOGGER log = new LOGGER(SQLData.class);

	public SQLData(Context context) {
		super(context, DATABASE_NAME, null, DATAVersion);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		///db.execSQL("drop table "+TABLE_LEARNE);
		StringBuffer strTable = new StringBuffer();
		strTable.append("create table " + CACHE_TABLE + " ( ");
		strTable.append(ID+" integer primary key autoincrement,");
		strTable.append(URL+" text  ,");
		strTable.append(CREATETIME+" text  ,");
		strTable.append(MD5+" text  ,");
		strTable.append(JSON+" text");
		strTable.append(");");
		
		db.execSQL(strTable.toString());
	}

	/**
	 * 删除表格
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    log.d("db.isReadOnly()="+db.isReadOnly());
		db.execSQL("drop table "+CACHE_TABLE);
		onCreate( db);
	}

}
