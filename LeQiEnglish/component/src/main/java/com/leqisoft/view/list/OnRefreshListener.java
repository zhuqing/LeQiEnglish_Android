package com.leqisoft.view.list;

public interface OnRefreshListener {
//	public void onDownPullRefresh();
	/**
	 * 加载更多的数据
	 */
	public void onLoadingMore();
	/**
	 * 加载新数据
	 */
	public void onLoadingNew();
}
