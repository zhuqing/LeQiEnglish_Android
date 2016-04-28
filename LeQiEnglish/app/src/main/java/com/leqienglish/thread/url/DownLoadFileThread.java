package com.leqienglish.thread.url;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.duoduo.entity.Content;

import com.leqienglish.fragment.book.LessionsFragment;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.Util;
import com.leqienglish.util.file.FileUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DownLoadFileThread extends URLThreadAbstract {
	private final static LOGGER LOG= new LOGGER(LessionsFragment.class);
	private String folderName;
	private String keyId;

	public DownLoadFileThread(Handler handler, String url,String keyId) {
		super(handler, url);
		this.keyId = keyId;
		LOG.d("download\t"+url);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void downLoadData(int length, int allSize, byte[] data)
			throws Exception {
		LOG.d("download\t"+length+"\t allSize="+allSize);
		this.getBufferOutPut().write(data, 0, length);
		this.getBufferOutPut().flush();
		
		Message message = new Message();
		message.what = AppType.URL_DOWNLOAD;
		Bundle bundler = new Bundle();
		bundler.putString(AppType.KEY, keyId);
		bundler.putLong(AppType.URL_DOWNLOAD_LEGTH, Long.valueOf(allSize+""));
		message.setData(bundler);
		this.getHandler().sendMessage(message);
	}

	@Override
	protected void downLoadOver() throws Exception {
		// TODO Auto-generated method stub
		this.getBufferOutPut().close();
		String filePath = this.getDownLoadFile().getAbsolutePath();
		if(!filePath.endsWith("apk")){
			FileUtil.unZip(filePath, FileUtil.getDirFilePath(filePath));
			this.getDownLoadFile().delete();
		}

		Message mess = new Message();
		Bundle data = new Bundle();
		data.putString(AppType.KEY, this.keyId);
		data.putString(AppType.DOWNLOAD_PATH, FileUtil.getDirFilePath(filePath));
		mess.setData(data);
		mess.what = AppType.URL_DOWNLOAD_OVER;
		this.getHandler().sendMessage(mess);

	}

	@Override
	protected String getFilePath() {
		// TODO Auto-generated method stub
		return FileUtil.getFilePath(this.getHttpurl());
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
