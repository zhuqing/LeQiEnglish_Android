package com.leqienglish.util;

import com.leqienglish.R;

import android.content.Context;
import cn.duoduo.entity.PageParam;

public class PageUtil {
	/**
	 * 初始化分页
	 * @param context
	 * @return
	 */
	public static PageParam initPage(Context context){
		PageParam page = new PageParam();
		page.setCurrentPage(1);
		page.setPageSize(Integer.parseInt(context.getString(R.string.pageSize)));
		return page;
	}
}
