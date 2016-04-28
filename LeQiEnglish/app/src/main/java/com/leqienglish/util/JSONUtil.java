package com.leqienglish.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
	/**
	 * 获取JSON中的翻译信息
	 * @param json
	 * @return
	 */
	public static String getTransPlate(String json) {
		JSONObject jsonO = JSONObject.fromObject(json);
		JSONArray array = jsonO.getJSONArray("trans_result");
		StringBuffer result = new StringBuffer();
		String source = "";
		for (int i = 0; i < array.size(); i++) {
			JSONObject item = array.getJSONObject(i);
			result.append(item.get("dst"));
			source = item.getString("src");
			if (i != array.size() - 1) {
				result.append(";");
			}
		}
		
		result.insert(0, source+":");
		return result.toString();
	}
}
