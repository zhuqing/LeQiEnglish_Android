package com.leqienglish.thread.url;

import java.io.IOException;
import java.net.URL;

import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.file.FileUtil;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DownLoadImageThread extends URLThreadAbstract {
	public static LOGGER logger = new LOGGER(DownLoadImageThread.class);
	private String fileName;
	private String keyId;
	public DownLoadImageThread(Handler handler, String url) {
		super(handler, url);
		logger.d(url);
		fileName = FileUtil.getFileName(url);
		//this.keyId = keyId;
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void downLoadData( int length,int allSize , byte[] data) throws Exception {
		this.getBufferOutPut().write(data, 0, length);
		this.getBufferOutPut().flush();
		Message message = new Message();
		message.what = AppType.URL_IMAGE_DOWNLOAD;
		Bundle bundler = new Bundle();
		bundler.putInt(AppType.URL_DOWNLOAD_LEGTH, allSize);
		bundler.putLong(AppType.URL_DOWNLOAD_ALLLEGTH, this.getFileSize());
		message.setData(bundler);
		this.getHandler().sendMessage(message);
		// TODO Auto-generated method stub

	}

	@Override
	protected void downLoadOver() throws Exception {
		// TODO Auto-generated method stub
		this.getBufferOutPut().close();
		Message msg = new Message();
		msg.what = AppType.URL_IMAGE_DOWNLOAD_OVER;
		Bundle data = new Bundle();
		data.putString(AppType.URL_IMAGE_NAME, this.fileName);
		msg.setData(data);
		this.getHandler().sendMessage(msg);
	}
	
	
	@Override
	protected String getFilePath() {
		// TODO Auto-generated method stub
		return AppData.getImageFileDir()+this.fileName;
	}


	@Override
	protected void noConnect() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void preStartDownLoad() {
		// TODO Auto-generated method stub
		
	}
	


}
