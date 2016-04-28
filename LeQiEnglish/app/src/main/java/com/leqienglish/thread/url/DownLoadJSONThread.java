package com.leqienglish.thread.url;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.leqienglish.database.ExecuteSQL;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;

import java.nio.ByteBuffer;

//import org.apache.http.util.ByteArrayBuffer;
//import org.apache.http.util.EncodingUtils;

public class DownLoadJSONThread extends URLThreadAbstract {

	/**
	 * 执行成功后
	 */
	private int type;
	/**
	 * 已经从数据库中找到缓存
	 */
	private String cacheStr;

   // public ByteBuffer bafer= new By

	//public ByteArrayBuffer baf = new ByteArrayBuffer(2048);

	public DownLoadJSONThread(Handler handler, String url, int type) {
		super(handler, url);
		this.type = type;

	}

	/**
	 * 文件下载完成后处理
	 *
	 * @param type
	 * @param bf
	 */
	private void sendHandler(int type, byte[] bf) {
		Log.v("sendHandler", "thpe=" + type);
		switch (type) {
		case AppType.URL_ERROR:
			this.getHandler().sendEmptyMessage(AppType.URL_ERROR);
			break;

		default:
			String json = "";//EncodingUtils.getString(bf, "UTF-8");
			//如果没有从数据库中取到数据，则发送当前数据。
			if (this.cacheStr == null) {
				downLoadFinished(json);
			}
			//如果当前数据与缓存数据不一致，则存入数据库
			if (this.cacheStr != json) {
				insertJsonData2DataBase(json);
			}

		}
	}

	/**
	 * 将缓存的数据存入数据库
	 *
	 * @param json
	 */
	private void insertJsonData2DataBase(String json) {

		AppData.createExecuteSQL().insertLearnE(this.getHttpurl(), json);
	}

	/**
	 * 通过Handler发送数据
	 *
	 * @param json
	 */
	protected void downLoadFinished(String json) {
		Message mess = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("json", json);
		mess.setData(bundle);
		mess.what = type;
		this.getHandler().sendMessage(mess);
	}


	@Override
	protected void downLoadOver() {
		//this.sendHandler(type, baf.toByteArray());
	}

	@Override
	protected void downLoadData(int length, int allSize, byte[] data) {
		// TODO Auto-generated method stub
		//this.baf.append(data, 0, length);

	}

	@Override
	protected String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void noConnect() {
		sendCacheStr();
	}

	@Override
	protected void preStartDownLoad() {
		// TODO Auto-generated method stub
		sendCacheStr();
	}

	/**
	 * 通过url从数据库中取相应的数据
	 */
	private void sendCacheStr(){
		ExecuteSQL sqlExcute = new ExecuteSQL(AppData.sqlData);
		cacheStr = sqlExcute.getJsonDataByKey(this.getHttpurl());
		if (cacheStr != null) {
			downLoadFinished(cacheStr);
		}
	}

}
