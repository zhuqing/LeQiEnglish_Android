package com.leqienglish.activity.behavor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.leqienglish.R;
import com.leqienglish.WelcomeActivity;
import com.leqienglish.database.ExecuteSQL;
import com.leqienglish.thread.url.DownLoadFileThread;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.LeQiType;
import com.leqienglish.util.URLDataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取更新文件
 * 
 * @author guona
 * 
 */
public class InitDataBehavor implements Handler.Callback {
	public LOGGER logger = new LOGGER(InitDataBehavor.class);
	private Handler handler;
	private WelcomeActivity activity;
	/**
	 * 加载基础URL是否完成
	 */
	private boolean isDownLoadOver;
	/**
	 * 时间是否结束
	 */
	private boolean isTimerOver;
	/**
	 * 完成延时后的跳转
	 */
	private static final int TIME_FINISHED = 1;
	/**
	 * 延时跳转
	 */
	private Timer timer;

	public InitDataBehavor(WelcomeActivity activity) {
		handler = new Handler(this);
		this.activity = activity;

	}

	/**
	 * 更新URL
	 */
	public void initURLData() {
		DownLoadJSONThread thread = new DownLoadJSONThread(this.handler,
				this.activity.getResources().getString(R.string.get_allUrl),
				AppType.DOWN_LOAD_URL);
		thread.start();

		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(TIME_FINISHED);
			}
		}, 5000L);
	}

	/**
	 * 检测是否需要更新
	 */
	public void checkIsNeedUpdate() {
		String url = AppData.getUrlByKey(URLDataUtil.UPDATE_APK);
		if (url == null) {
			logger.e("url==null");
			return;
		}
		new DownLoadJSONThread(handler, url, AppType.HAS_UPDATE).start();

	}
	
	/**
	 * 查找名言警句
	 */
	private void findAllFamousWrods(){
		new DownLoadJSONThread(handler, AppData.getUrlByKey(URLDataUtil.CONTENT_LIST_TYPE)+"?type="+LeQiType.TitleTip, AppType.FAMOUS_WORD_JSON).start();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle data = msg.getData();
		switch (msg.what) {
		case AppType.HAS_UPDATE:

			String json = data.getString(AppType.JSON);
			try {
				JSONObject jObject = new JSONObject(json);
				String version = jObject.getString("version");
				String updatePath = jObject.getString("updatePath");
				if (!AppData.getCurrentVersion().equals(version)) {
					new DownLoadFileThread(this.handler, updatePath, null)
							.start();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AppType.URL_DOWNLOAD_OVER:
			// 安装apk
			String path = data.getString(AppType.DOWNLOAD_PATH);
			logger.v("path=\t" + path);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(path)),
					"application/vnd.android.package-archive");
			// this.activity.getApplicationContext().startActivity(intent);
			break;

		case AppType.DOWN_LOAD_URL:
			try {
				AppData.setUrlData(data.getString(AppType.JSON));
			} catch (Exception e) {

			}
			logger.v("DOWN_LOAD_URL\t" + this.isTimerOver);
			if (this.isTimerOver) {

				this.activity.turn2NextActivity();
			}
			this.isDownLoadOver = true;
			this.findAllFamousWrods();
			checkIsNeedUpdate();
			break;

		case TIME_FINISHED:
			logger.v("TIME_FINISHED\t" + this.isDownLoadOver);
			this.timer.cancel();
			this.isTimerOver = true;
			if (this.isDownLoadOver) {
				this.activity.turn2NextActivity();
			}
			break;
		case AppType.FAMOUS_WORD_JSON:
			String wordsData = data.getString(AppType.JSON);
			ExecuteSQL sqlExcute = new ExecuteSQL(AppData.sqlData);
			Long id = sqlExcute.insertLearnE(this.activity.getString(R.string.famouse_words), wordsData);
			break;
		}
		return false;
	}
}
