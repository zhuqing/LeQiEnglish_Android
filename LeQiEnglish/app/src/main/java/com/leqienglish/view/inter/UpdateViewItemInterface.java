package com.leqienglish.view.inter;
/*
 * ViewAdapter 的刷新接口
 * @author guona
 *
 */
public interface UpdateViewItemInterface {
	/**
	 * 获取ViewAdapter的唯一值
	 * @return
	 */
	public String getKeyId();
	/**
	 * 获取下载链接
	 * @return
	 */
	public String getDownloadUrl();
	/**
	 * 更新下载进度
	 * @param keyId
	 * @param currentSize
	 * @param totalSize
	 */
	public void downloadUpdate(String keyId,long currentSize,Long totalSize);
	/**
	 * 下载完成
	 */
	public void downloadOver(String keyId);
}
