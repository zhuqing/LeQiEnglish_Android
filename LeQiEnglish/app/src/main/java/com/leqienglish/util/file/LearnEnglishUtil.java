package com.leqienglish.util.file;

import java.util.HashMap;
import java.util.Map;

import com.leqienglish.util.AppData;

public class LearnEnglishUtil {
	/**
	 * 已下载的跟读文件及其路径
	 */
	private static Map<String, String> learneMap = new HashMap<String, String>();

	/**
	 * 文件是否已经下载
	 * 
	 * @param keyId
	 * @return
	 */
	public static Boolean hasDownloadFile(String keyId) {
		if (keyId == null) {
			return false;
		}
		if (learneMap.containsKey(keyId)) {
			return true;
		}
		String path = LearnEnglishUtil.getPath(keyId);
		return path != null;
	}

	/**
	 * put已下载的文件
	 * 
	 * @param keyId
	 * @param path
	 */
	public static void addDownLoadFile(String keyId, String path) {
		if(keyId==null||path==null){
			return;
		}
		learneMap.put(keyId, path);
		AppData.createExecuteSQL().insertLearnE(keyId, path);
	}

	/**
	 * 通过keyId获取路径
	 * 
	 * @param keyId
	 * @return
	 */
	public static String getPath(String keyId) {
		if (!learneMap.containsKey(keyId)) {
			String path = AppData.createExecuteSQL().getJsonDataByKey(keyId);
			if(path!=null){
				learneMap.put(keyId, path);
			}
		}

		String path = learneMap.get(keyId);
		return path;
	}

}
