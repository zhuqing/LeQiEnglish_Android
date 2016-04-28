package com.leqienglish.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.leqienglish.thread.url.DownLoadFileThread;
import com.leqienglish.util.AppType;
import com.leqienglish.util.file.LearnEnglishUtil;
import com.leqienglish.view.inter.UpdateViewItemInterface;

public class DownloadManager implements Handler.Callback {

	/**
	 * 下载view
	 */
	private Map<String, UpdateViewItemInterface> registMap;
	/**
	 * 下载线程
	 */
	private Map<String, Thread> downLoadThread;
	/**
	 * 下载信息
	 */
	private Map<String, DownloadInfo> downloadInfo;
	private Handler handler;

	/**
	 * 线程池
	 */
	private ExecutorService threadPool;

	/**
	 * 线程池最大线程数
	 */
	private final static int maxPoolSize = 3;

	/*
	 * 单例引用
	 */
	private static final DownloadManager downLoadManage = new DownloadManager();

	private DownloadManager() {
		handler = new Handler(this);
		threadPool = Executors.newFixedThreadPool(maxPoolSize);
	}

	public static DownloadManager getInstance() {
		return downLoadManage;
	}

	/**
	 * 开始下载
	 * 
	 * @param viewItemInterface
	 */
	public void startDownLoad(UpdateViewItemInterface viewItemInterface) {
		if (viewItemInterface == null) {
			return;
		}
		if (!this.getRegistMap().containsKey(viewItemInterface.getKeyId())) {
			this.getRegistMap().put(viewItemInterface.getKeyId(), viewItemInterface);
		}

		this.startDownloadThread(viewItemInterface);
	}

	/**
	 * 创建一个下载线程，并放入线程池
	 * 
	 * @param viewItemInterface
	 */
	private void startDownloadThread(UpdateViewItemInterface viewItemInterface) {
		if (viewItemInterface == null
				|| viewItemInterface.getDownloadUrl() == null) {
			return;
		}
		DownLoadFileThread downloadFileThead = new DownLoadFileThread(
				this.handler, viewItemInterface.getDownloadUrl(),
				viewItemInterface.getKeyId());
		this.threadPool.execute(downloadFileThead);
		this.getDownLoadThread().put(viewItemInterface.getKeyId(),
				downloadFileThead);
	}

	/**
	 * 关闭下载
	 * 
	 * @param viewItemInterface
	 */
	public void closeDownLoad(UpdateViewItemInterface viewItemInterface) {
		Thread thread = this.getDownLoadThread(viewItemInterface);
		if (thread == null) {
			return;
		}
		// if(thread.isAlive())
		thread.interrupt();
	}

	/**
	 * 暂停downLoad
	 * 
	 * @param viewItemInterface
	 */
	public void suspendDownLoad(UpdateViewItemInterface viewItemInterface) {

		Thread thread = this.getDownLoadThread(viewItemInterface);
		if (thread == null) {
			return;
		}
		try {
			thread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// thread.suspend();

	}

	/**
	 * 继续下载
	 * 
	 * @param viewItemInterface
	 */
	public void restartDownLoad(UpdateViewItemInterface viewItemInterface) {
		Thread thread = this.getDownLoadThread(viewItemInterface);
		if (thread == null) {
			return;
		}
		thread.notify();
		// thread.suspend();

	}

	/**
	 * 是否正在下载
	 * 
	 * @param viewItemInterface
	 * @return
	 */
	public boolean isDownloading(UpdateViewItemInterface viewItemInterface){
	  if(viewItemInterface==null){
		  return false;
	  }
	  return this.getDownLoadThread().containsKey(viewItemInterface.getKeyId());
  }

	/**
	 * 获取下载线程
	 * 
	 * @param viewItemInterface
	 * @return
	 */
	private Thread getDownLoadThread(UpdateViewItemInterface viewItemInterface) {
		if (viewItemInterface == null) {
			return null;
		}
		if (this.getDownLoadThread().containsKey(viewItemInterface.getKeyId())) {
			Thread thread = this.getDownLoadThread().get(
					viewItemInterface.getKeyId());
			return thread;
			// thread.suspend();
		}

		return null;
	}

	/**
	 * 获取下载数据
	 * 
	 * @param viewItemInterface
	 * @return
	 */
	public DownloadInfo getDataDownInfo(
			UpdateViewItemInterface viewItemInterface) {
		if (this.getDownloadInfo().containsKey(viewItemInterface.getKeyId())) {
			return this.getDownloadInfo().get(viewItemInterface.getKeyId());
		}
		return null;
	}

	/**
	 * 将下载数据放入downloadInfo中
	 * 
	 * @param keyId
	 * @param hasDownload
	 */
	private void putDataInDownloadInfo(String keyId, long hasDownload) {
		DownloadInfo downloadInfo = null;
		if (this.getDownloadInfo().containsKey(keyId)) {
			downloadInfo = this.getDownloadInfo().get(keyId);
			downloadInfo.setHasDownloadSize(hasDownload);
		} else {
			downloadInfo = new DownloadInfo();
			downloadInfo.setHasDownloadSize(hasDownload);
			downloadInfo.setKeyId(keyId);
			this.getDownloadInfo().put(keyId, downloadInfo);
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle data = msg.getData();
		String keyId = data.getString(AppType.KEY);
		UpdateViewItemInterface viewItem = this.getRegistMap().get(keyId);
		switch (msg.what) {
		case AppType.URL_DOWNLOAD:
			Long length = data.getLong(AppType.URL_DOWNLOAD_LEGTH);
			this.putDataInDownloadInfo(keyId, length);
			viewItem.downloadUpdate(keyId, length, -1L);
			break;
		case AppType.URL_DOWNLOAD_OVER:
			String path = data.getString(AppType.DOWNLOAD_PATH);
			LearnEnglishUtil.addDownLoadFile(keyId, path);
			
			viewItem.downloadOver(keyId);
			
			this.getDownloadInfo().remove(keyId);
			this.getDownLoadThread().remove(keyId);
			this.getRegistMap().remove(keyId);

			break;
		}
		return false;
	}

	/**
	 * 下载信息
	 * 
	 * @author guona
	 * 
	 */
	public class DownloadInfo {
		/**
		 * 当前下载大小
		 */
		private long hasDownloadSize;
		/**
		 * 总大小
		 */
		private long totalSize;
		/**
		 * 下载的数据Id
		 */
		private String keyId;

		/**
		 * @return the hasDownloadSize
		 */
		public long getHasDownloadSize() {
			return hasDownloadSize;
		}

		/**
		 * @param hasDownloadSize
		 *            the hasDownloadSize to set
		 */
		public void setHasDownloadSize(long hasDownloadSize) {
			this.hasDownloadSize = hasDownloadSize;
		}

		/**
		 * @return the totalSize
		 */
		public long getTotalSize() {
			return totalSize;
		}

		/**
		 * @param totalSize
		 *            the totalSize to set
		 */
		public void setTotalSize(long totalSize) {
			this.totalSize = totalSize;
		}

		/**
		 * @return the keyId
		 */
		public String getKeyId() {
			return keyId;
		}

		/**
		 * @param keyId
		 *            the keyId to set
		 */
		public void setKeyId(String keyId) {
			this.keyId = keyId;
		}

	}

	/**
	 * @return the registMap
	 */
	private Map<String, UpdateViewItemInterface> getRegistMap() {
		if (this.registMap == null) {
			this.registMap = new HashMap<String, UpdateViewItemInterface>();
		}
		return registMap;
	}

	/**
	 * @return the downLoadThread
	 */
	private Map<String, Thread> getDownLoadThread() {
		if (this.downLoadThread == null) {
			this.downLoadThread = new HashMap<String, Thread>();
		}
		return downLoadThread;
	}

	/**
	 * @return the downloadInfo
	 */
	public Map<String, DownloadInfo> getDownloadInfo() {
		if (this.downloadInfo == null) {
			this.downloadInfo = new HashMap<String, DownloadInfo>();
		}
		return downloadInfo;
	}

	/**
	 * @return the downloadmanage
	 */
	public static DownloadManager getDownloadmanage() {
		return downLoadManage;
	}

}
