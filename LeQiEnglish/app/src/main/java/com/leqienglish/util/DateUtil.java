package com.leqienglish.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	/**
	 * 将毫秒转为日期
	 * @param dateMill
	 * @param format
	 * @return
	 */
	public static String toDataStr(String dateMill,String format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.valueOf(dateMill));
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(calendar.getTime());
		
	}
}
