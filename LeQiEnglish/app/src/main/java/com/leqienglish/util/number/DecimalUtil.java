package com.leqienglish.util.number;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * 数字处理工具类
 * @author guona
 *
 */
public class DecimalUtil {

	public final DecimalFormat format0_2 = new DecimalFormat("#.00");
	public static String getDouble(Double d, DecimalFormat  df){
		return df.format(d);
	}
	
	/**
	 * 转换为百分比的数
	 * @param d
	 * @return
	 */
	public static String getPercent(Double d){
		int dint = (int) (d*100);
		return dint+"%";
	}
	
}
