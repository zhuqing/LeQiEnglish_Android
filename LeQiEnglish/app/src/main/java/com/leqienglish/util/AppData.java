package com.leqienglish.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.leqienglish.R;
import com.leqienglish.database.ExecuteSQL;
import com.leqienglish.database.SQLData;
import com.leqienglish.thread.url.DownLoadImageThread;
import com.leqienglish.util.bitmap.BitmapUtil;
import com.leqienglish.util.file.FileUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.duoduo.entity.Content;
import cn.duoduo.entity.Entity;

/**
 * app的初始化数据
 * 
 * @author guona
 * 
 */
public class AppData {
	public static LOGGER logger = new LOGGER(AppData.class);
	/**
	 * 当前的上下文
	 */
	public static Context context;
	/**
	 * App名称
	 */
	private final static String appName = "leqiEnglish";
	/**
	 * 服务Api
	 */
	public static Map<String, String> urlDataMap;
	/**
	 * 实体对象的缓存
	 */
	public static Map<String, Entity> appContentDataMap;
	/**
	 * 以下载文件的路径
	 */
	public static Map<String, String> hasDownLoadLearnEMap = new HashMap<String, String>();
	/**
	 * 第三方字体
	 */
	private static Typeface typeface;
	/**
	 * 名言警句的缓存
	 */
	private final static List<Content> famousWords = new ArrayList<Content>();
	/**
	 * 当前版本
	 */
	private static String version = "";

	/**
	 * 服务器所在的地址
	 */
	private static String HOST = "host";

	/**
	 * 当前系统已下载的图片
	 */
	private static Set<String> hasDownLoadImageName;
	/**
	 * 在线下载图片时使用
	 */
	private static Map<String, ImageView> imageViewMap;
	/**
	 * 数据库句柄
	 */
	public static SQLData sqlData;
	/**
	 * 数据库执行
	 */
	private static ExecuteSQL sqlExcute;

	/**
	 * 初始化数据
	 * 
	 * @param context
	 */
	public static void initData(Context context) {
		AppData.context = context;
		initImageData();

		imageViewMap = new HashMap<String, ImageView>();
		sqlData = new SQLData(context);
	}

	/**
	 * 初始化系统已下载的图片
	 */
	private static void initImageData() {
		hasDownLoadImageName = new HashSet<String>();
		String imagePath = getImageFileDir();
		File imageDir = new File(imagePath);
		imageDir.mkdirs();
		if (imageDir.list() != null) {
			hasDownLoadImageName.addAll(Arrays.asList(imageDir.list()));
		}

	}

	/**
	 * 获取存储图片的路径
	 * 
	 * @return
	 */
	public final static String getImageFileDir() {
		logger.v(getSDPath() + "/" + appName + "/image/");
		return getSDPath() + "/" + appName + "/image/";
	}

	/**
	 * 获取SDpath
	 * 
	 * @return
	 */
	private static String getSDPath() {
		String sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().toString();// 获取跟目录
		} else {
			sdDir = context.getFilesDir().getAbsolutePath();
		}
		return sdDir.toString();

	}

	/**
	 * 获取播放文件的路径
	 * 
	 * @return
	 */
	public static String getMap3FilePath() {
		logger.v(getSDPath() + "/" + appName + "/file/");
		return getSDPath() + "/" + appName + "/file/";
	}

	/**
	 * 获取文件在的路径
	 * 
	 * @param imageUrl
	 *            image的http路径
	 * @return null文件还没有下载
	 */
	public final static String getImagePathName(String imageUrl) {
		if (imageUrl == null) {
			return null;
		}

		String filename = FileUtil.getFileName(imageUrl);
		if (hasDownLoadImageName.contains(filename)) {
			return getImageFileDir() + filename;
		}

		return null;
	}

	/**
	 * 将已下载的图片名称放入hasDownLoadImageName中
	 * 
	 * @param imageName
	 */
	public final static void addImageName(String imageName) {
		hasDownLoadImageName.add(imageName);
	}

	public final static String getCurrentVersion() {
		return "1.1";
	}

	/**
	 * 将urlDataJson转换为urlMap
	 * 
	 * @param urlDatajson
	 * @throws JSONException
	 */
	public final static void setUrlData(String urlDatajson)
			throws Exception {
		JSONObject json = JSONObject.fromObject(urlDatajson);

		Iterator<String> jsonKeySet = json.keys();
		AppData.urlDataMap = new HashMap<String, String>();
		while (jsonKeySet.hasNext()) {
			String key = jsonKeySet.next();
			AppData.urlDataMap.put(key, json.getString(key));
		}

		if (AppData.urlDataMap.containsKey(URLDataUtil.HOST)) {
			AppData.HOST = AppData.urlDataMap.get(URLDataUtil.HOST);
		}

	}

	/**
	 * 通过key获取url
	 * 
	 * @param key
	 * @return
	 */
	public final static String getUrlByKey(String key) {
		if (AppData.urlDataMap == null) {
			return null;
		}
		if (AppData.urlDataMap.containsKey(key)) {
			return AppData.HOST + AppData.urlDataMap.get(key);
		}
		return null;
	}

	/**
	 * 数据缓存
	 * 
	 * @param entity
	 */
	public static void putContent(Entity entity) {
		String key = getKey(entity);
		if (AppData.appContentDataMap == null) {
			AppData.appContentDataMap = new HashMap<String, Entity>();
		}

		if (entity == null) {
			return;
		}

		AppData.appContentDataMap.put(key, entity);

	}

	public static String getKey(Entity entity) {
		if (entity == null) {
			return null;
		}
		String key = entity.getId() + entity.getClass().getSimpleName();
		return key;
	}

	/**
	 * 获取字体
	 * 
	 * @return
	 */
	public static Typeface getTypeFace() {
		if (AppData.typeface == null) {
			typeface = Typeface.createFromAsset(AppData.context.getAssets(),
					"font/xjlFont.ttf");
		}

		return typeface;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public static Entity getEntity(String key) {
		if (AppData.appContentDataMap.containsKey(key)) {
			return AppData.appContentDataMap.get(key);
		}

		return null;
	}

	/**
	 * 对ImageView设置图片
	 * 
	 * @param content
	 * @param imageView
	 */
	public static void setImageView(Handler handler, Content content,
			ImageView imageView, float scaleWidth) {
		try {
			String imagePath = AppData.getImagePathName(content.getIconPath());
			if (imagePath != null) {
				logger.d("imagePath\t" + imagePath);
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
				bitmap = BitmapUtil.sacalTo(bitmap, scaleWidth);
				imageView.setImageBitmap(bitmap);
			} else {
				// 开始下载图片
				if (content != null && content.getIconPath() != null) {
					logger.d("object.getIconPath()\t" + content.getIconPath());
					AppData.putImageView(content.getIconPath(), imageView);
					new DownLoadImageThread(handler,
							AppData.urlDataMap.get(URLDataUtil.HOST)
									+ "duoduoroom/" + content.getIconPath())
							.start();

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final static void putImageView(String fileName, ImageView imageView) {
		fileName = FileUtil.getFileName(fileName);
		imageViewMap.put(fileName, imageView);
	}

	/**
	 * 根据已下载的文件名称 获取ImageView
	 * 
	 * @param fileName
	 * @return
	 */
	public final static ImageView getImageViewByFileName(String fileName) {
		if (imageViewMap.containsKey(fileName)) {
			return imageViewMap.get(fileName);
		}
		return null;
	}

	/**
	 * 判断entity对应的播放文件是否下载
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean hasDownLoad(Entity entity) {

		return getHasDownLoadLearnEPath(entity) != null;
	}

	/**
	 * 获取已下载文件的路径
	 * 
	 * @param entity
	 * @return
	 */
	public static String getHasDownLoadLearnEPath(Entity entity) {
		String key = AppData.getKey(entity);
		if (AppData.hasDownLoadLearnEMap.containsKey(key)) {
			return AppData.hasDownLoadLearnEMap.get(key);
		}

		ExecuteSQL sqlExcute = new ExecuteSQL(AppData.sqlData);
		String path = sqlExcute.getJsonDataByKey(key);
		if (path != null) {
			AppData.hasDownLoadLearnEMap.put(key, path);
			return path;
		}
		return null;
	}

	/**
	 * 将下载路径放入缓存中
	 * 
	 * @param key
	 * @param path
	 */
	public static void putLearnePath(String key, String path) {
		ExecuteSQL sqlExcute = new ExecuteSQL(AppData.sqlData);
		sqlExcute.insertLearnE(key, path);
		AppData.hasDownLoadLearnEMap.put(key, path);
	}

	/**
	 * 设置当前版本
	 * 
	 * @param version
	 */
	public static void setVersion(String version) {
		AppData.version = version;
	}

	/**
	 * 是否需要升级
	 * 
	 * @param newVersion
	 * @return
	 */
	public static boolean isNeedUpdate(String newVersion) {
		return newVersion == AppData.version;
	}

	/**
	 * 获取名言警句
	 * 
	 * @return
	 */
	public static Content getFamousWords() {
		if (AppData.famousWords.isEmpty()) {
			try {
				initFamousWords();
				if (AppData.famousWords.isEmpty()) {
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		int index = (int) (Math.random() * AppData.famousWords.size());

		return AppData.famousWords.get(index);
	}

	/**
	 * 初始化话名言数据
	 * 
	 * @param json
	 * @throws Exception
	 */
	public static void initFamousWords() throws Exception {
		ExecuteSQL sqlExcute = new ExecuteSQL(AppData.sqlData);
		String json = sqlExcute.getJsonDataByKey(AppData.context.getString(R.string.famouse_words));
		AppData.famousWords.addAll(URLDataUtil.json2List(json));
	}

	/**
	 * 创建ExecuteSQL
	 * 
	 * @return
	 */
	public static ExecuteSQL createExecuteSQL() {
		if (sqlData == null) {
			return null;
		}
		if (sqlExcute == null) {
			sqlExcute = new ExecuteSQL(AppData.sqlData);
		}
		return sqlExcute;
	}

}
