package com.leqienglish.view;

import cn.duoduo.entity.Content;

import com.leqienglish.util.AppData;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.util.file.FileUtil;
import com.leqienglish.view.inter.UpdateViewItemInterface;

public abstract class ViewItem implements UpdateViewItemInterface {

	private Content content;

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}
	
	/*
	 * 下载文件
	 */
	public abstract void updateDownLoad(long currentSize, Long totalSize);
	/**
	 * 下载结束
	 */
	public abstract void downloadOver();


	@Override
	public String getKeyId() {

		return AppData.getKey(this.getContent());
	}

	@Override
	public String getDownloadUrl() {
		if(this.getContent()==null){
			return null;
		}
		String url = AppData.urlDataMap.get(URLDataUtil.HOST) + "duoduoroom/"
		+ getFormatPath(content.getAudioPath());
	//	String url = AppData.urlDataMap.get(URLDataUtil.HOST) + "duoduoroom/file/test.lq";
		return url;  
	}

	@Override
	public void downloadUpdate(String keyId, long currentSize, Long totalSize) {
		if (!isCurrentViewItem(keyId)) {
			return;
		}
		long totalLength = 0L;
		if(totalSize!=-1L){
			totalLength = totalSize;
		}else{
			totalLength = this.getContent().getContentLength();
		}
		updateDownLoad(currentSize, totalLength);

	}

	@Override
	public void downloadOver(String keyId) {
		if (!isCurrentViewItem(keyId)) {
			return;
		}
		this.downloadOver();
	}

	/**
	 * 是否调用当前的ViewItem
	 * @param keyId
	 * @return
	 */
	private boolean isCurrentViewItem(String keyId) {
		if (this.getKeyId() == null || keyId == null) {
			return false;
		}

		return this.getKeyId().equals(keyId);
	}
	

	/**
	 * 格式化下载路径
	 * @param filePath
	 * @return
	 */
	private String getFormatPath(String filePath) {
		return filePath.trim().replaceAll("\\s", "%20");
	}


}
