package com.leqienglish.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leqienglish.util.LOGGER;
import com.leqienglish.util.Util;

import static com.leqienglish.database.Constants.CACHE_TABLE;
import static com.leqienglish.database.Constants.CREATETIME;
import static com.leqienglish.database.Constants.ID;
import static com.leqienglish.database.Constants.JSON;
import static com.leqienglish.database.Constants.MD5;
import static com.leqienglish.database.Constants.URL;

public class ExecuteSQL {
	private LOGGER log = new LOGGER(ExecuteSQL.class);

	private SQLData sqlData;

	public ExecuteSQL(SQLData sqlData) {
		this.sqlData = sqlData;
	}

	/**
	 * 插入数据
	 * 
	 * @param json
	 * @return
	 */
	public Long insertLearnE(String url, String json) {
		log.d("start insertLearnE ");
		SQLiteDatabase db = sqlData.getWritableDatabase();

		ContentValues values = this.createValues(url, json);
		log.d("start insertLearnE url=" + url);
		// log.d("start insertLearnE data="+json);
		Long id = this.getIdByKey(url);
		if (id != null&&id>=0) {
			db.update(CACHE_TABLE, values, ID + "=?",
					new String[] { id.toString() });
			return id;
		} else {
			id = db.insertOrThrow(CACHE_TABLE, null, values);

			return id;
		}

	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteJsonDataById(String id) {
		try {
			SQLiteDatabase db = sqlData.getWritableDatabase();
			db.delete(CACHE_TABLE, "id=" + id, null);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 删除数据
	 * 
	 * @param md5
	 * @return
	 */
	public boolean deleteJsonDataByMd5(String md5) {
		try {
			SQLiteDatabase db = sqlData.getWritableDatabase();
			db.delete(CACHE_TABLE, MD5 + "='" + md5 + "'", null);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 创建values
	 * 
	 * @param json
	 * @return
	 */
	private ContentValues createValues(String url, String json) {
		ContentValues values = new ContentValues();
		values.put(JSON, json);
		values.put(URL, url);
		values.put(MD5, Util.Md5(url));
		values.put(CREATETIME, System.currentTimeMillis());
		return values;
	}

	/**
	 * 更新课程
	 * 
	 * @param json
	 * @return
	 */
	public boolean updateJSONData(String json, String url, long id) {
		try {
			SQLiteDatabase db = sqlData.getWritableDatabase();
			ContentValues values = this.createValues(json, url);
			db.update(CACHE_TABLE, values, "id=" + id, null);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 通过Url获取JSON数据
	 * 
	 * @param key
	 * @return
	 */
	public String getJsonDataByKey(String key) {
		String md5 = Util.Md5(key);
		log.d("getJsonDataByKey key=" + key);
		return getJSONDataByMd5(md5);
	}

	/**
	 * 根据Key值获取Id
	 */
	private Long getIdByKey(String key) {
		String md5 = Util.Md5(key);

		SQLiteDatabase db = sqlData.getReadableDatabase();
		// Cursor cursor = db.rawQuery("select * from "+CACHE_TABLE,null);
		Cursor cursor = db.query(CACHE_TABLE, null, MD5 + "=?",
				new String[] { md5 }, null, null, null);
		if (cursor.getCount() <= 0) {
			cursor.close();

			return null;
		}
		if (!cursor.moveToFirst()) {
			log.d("!cursor.moveToFirst()");
			cursor.close();

			return null;
		}
		Long id = -1L;
		while (cursor.moveToNext()) {
			id = cursor.getLong(cursor.getColumnIndex(ID));
		}
		log.d("getIdByKey\t id=" + id);
		cursor.close();
		return id;
	}

	/**
	 * 根据Md5获取Json字符串
	 * 
	 * @param md5
	 * @return
	 */
	private String getJSONDataByMd5(String md5) {
		log.d("getJSONDataByMd5 md5=" + md5);
		if(sqlData==null){
			log.d("sqlData==null");
			return null;
		}
		SQLiteDatabase db = sqlData.getReadableDatabase();
		// Cursor cursor = db.rawQuery("select * from "+CACHE_TABLE,null);
		Cursor cursor = db.query(CACHE_TABLE, null, MD5 + "=?",
				new String[] { md5 }, null, null, null);
		if (cursor.getCount() <= 0) {
			cursor.close();

			return null;
		}
		if (!cursor.moveToFirst()) {
			log.d("!cursor.moveToFirst()");
			cursor.close();

			return null;
		}
		String jsonData = null;
		while (cursor.moveToNext()) {
			// Content learnE = this.createLearnE(cursor);
			jsonData = cursor.getString(cursor.getColumnIndex(JSON));
			log.d("getJSONDataByMd5 jsonData=" + jsonData);
			// list.add(learnE);
		}
		log.d("getJSONDataByMd5 jsonData=" + jsonData);
		cursor.close();

		return jsonData;
	}

}
