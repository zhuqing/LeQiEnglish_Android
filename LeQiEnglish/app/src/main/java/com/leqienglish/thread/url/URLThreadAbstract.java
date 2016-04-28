package com.leqienglish.thread.url;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



import com.leqienglish.entity.User;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.NetUtil;
import com.leqienglish.util.Util;

import android.os.Handler;

/**
 * 下载文件的线程
 * 
 * @author guona
 * 
 */
public abstract class URLThreadAbstract extends Thread {
	public static LOGGER logger = new LOGGER(URLThreadAbstract.class);
	/**
	 * 
	 */
	private Handler handler;
	/**
	 * 请求连接
	 */
	private String httpurl;

	/**
	 * 请求方式
	 */
	private String requestMethod = "GET";
	/**
	 * 延时
	 */
	private int timeOut = 5 * 1000;
	/**
	 * 
	 */
	private File downLoadFile;
	/**
	 * 下载的文件的长度
	 */
	private Long fileSize;

	private BufferedOutputStream bufferOutPut;

	public URLThreadAbstract(Handler handler, String url) {
		this.handler = handler;

		this.httpurl = url;

		// replace();
	}

	public URLThreadAbstract(Handler handler, String url, String requestMethod,
			int timeOut) {
		this.handler = handler;

		this.httpurl = url;

		this.requestMethod = requestMethod;
		this.timeOut = timeOut;

		// replace();

	}

	public void run() {
		logger.d("start run");
		HttpURLConnection ucon = null;
		try {
			int length = -1;
			int fileDownLoadSize = 0;
			if (!NetUtil.isNetworkConnected()) {
				this.noConnect();
				return;
			}
			byte[] by = new byte[2048];
			this.createBufferedOutputStream();
			URL downLoad = new URL(httpurl);
			this.preStartDownLoad();

			ucon = (HttpURLConnection) downLoad.openConnection();
			ucon.setRequestMethod(this.requestMethod);
			ucon.setConnectTimeout(this.timeOut);

			int code = ucon.getResponseCode();
			logger.d("code=" + code);
			if (code == HttpURLConnection.HTTP_OK) {

				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);

				this.setFileSize(Long.valueOf(bis.available() + ""));
				while ((length = bis.read(by)) > 0) {

					fileDownLoadSize += length;
					downLoadData(length, fileDownLoadSize, by);
				}

				this.downLoadOver();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		if (ucon != null) {
			ucon.disconnect();
		}
	}

	/**
	 * 没有连接时
	 */
	protected abstract void noConnect();

	/**
	 * 开始执行download前的操作
	 */
	protected abstract void preStartDownLoad();

	/**
	 * 下载文件的长度
	 * 
	 * @param length
	 * @throws Exception
	 */
	protected abstract void downLoadData(int length, int allSize, byte[] data)
			throws Exception;

	/**
	 * 文件下载完成
	 * 
	 * @throws Exception
	 */
	protected abstract void downLoadOver() throws Exception;

	/**
	 * 替换url中的中文或特殊字符
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void replace() {
		try {
			this.getHttpurl().substring(0,
					this.getHttpurl().lastIndexOf('/') + 1);
			String url = this.getHttpurl().substring(
					this.getHttpurl().lastIndexOf('/') + 1);
			url = Util.urlEncode(url);
			this.setHttpurl(this.getHttpurl().substring(0,
					this.getHttpurl().lastIndexOf('/') + 1)
					+ url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取以下载文件保存的路径
	 * 
	 * @return
	 */
	protected abstract String getFilePath();

	/**
	 * 创建缓冲流
	 */
	protected void createBufferedOutputStream() {
		try {
			String fileName = this.getFilePath();
			if (fileName == null) {
				return;
			}
			this.downLoadFile = new File(fileName);

			this.downLoadFile.createNewFile();

			FileOutputStream fileOut = new FileOutputStream(this.downLoadFile);
			this.bufferOutPut = new BufferedOutputStream(fileOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.getHandler().sendEmptyMessage(AppType.URL_ERROR);
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public String getHttpurl() {
		return httpurl;
	}

	public void setHttpurl(String httpurl) {
		this.httpurl = httpurl;
	}

	/**
	 * @return the downLoadFile
	 */
	public File getDownLoadFile() {
		return downLoadFile;
	}

	/**
	 * @param downLoadFile
	 *            the downLoadFile to set
	 */
	public void setDownLoadFile(File downLoadFile) {
		this.downLoadFile = downLoadFile;
	}

	/**
	 * @return the bufferOutPut
	 */
	public BufferedOutputStream getBufferOutPut() {
		return bufferOutPut;
	}

	/**
	 * @param bufferOutPut
	 *            the bufferOutPut to set
	 */
	public void setBufferOutPut(BufferedOutputStream bufferOutPut) {
		this.bufferOutPut = bufferOutPut;
	}

	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
