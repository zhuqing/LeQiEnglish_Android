package com.leqienglish.thread.url;

import android.os.Handler;

import com.leqisoft.inter.Callback;

public class DownLoadCallback extends DownLoadJSONThread {

	private Callback<String,Object> callback;
	public DownLoadCallback(Handler handler, String url, Callback<String,Object> callback) {
		super(handler, url, -1);
		this.callback = callback;
		// TODO Auto-generated constructor stub
	}
	protected void downLoadFinished(String json) {
		if(this.callback == null){
			return;
		}
		this.callback.call(json);
	}
}
