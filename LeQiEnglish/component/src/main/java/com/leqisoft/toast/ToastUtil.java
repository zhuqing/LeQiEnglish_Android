package com.leqisoft.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具类
 * @author guona
 *
 */
public class ToastUtil {
	/**
	 * Toast 显示在顶端
	 * @param context
	 * @param message
	 * @param during
	 */
	public static void showTop(Context context ,String message,int during ){
		Toast toast = Toast.makeText(context, message, during);
		toast.setGravity(Gravity.TOP, 0, 100);
		toast.show();
	}
	/**
	 * Toast 显示在顶端
	 * @param context
	 * @param message
	 */
	public static void showTop(Context context ,String message ){
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, 100);
		toast.show();
	}
	
	/**
	 * 显示Toast
	 * @param context
	 * @param message
	 * @param during
	 */
	public static void show(Context context ,String message,int during ){
		Toast toast = Toast.makeText(context, message, during);
		toast.show();
	}
	/**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
	public static void show(Context context ,String message ){
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
	}
}
