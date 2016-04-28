package com.leqienglish.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.leqienglish.util.LOGGER;

/**
 * bitmap处理工具
 * @author guona
 *
 */
public class BitmapUtil {
	public final static LOGGER logger = new LOGGER(BitmapUtil.class);

	/**
	 * 对图片进行缩放处理
	 * @param bitmap
	 * @param currWidth
	 * @return
	 */
	public static Bitmap sacalTo(Bitmap bitmap, float currWidth) {
		if(bitmap == null ){
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postScale((float) (currWidth * 1.0 / bitmap.getWidth()),
				currWidth * 1.0f / bitmap.getWidth());
		
		logger.v(bitmap.getWidth() + "::" + bitmap.getHeight());
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		// bitmap.recycle();
		// bitmap = null;
		return bitmap;
	}
	
	
}
