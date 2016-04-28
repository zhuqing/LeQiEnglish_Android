package com.leqienglish;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.Util;
import com.leqienglish.view.play.FollowReadView;
import com.leqienglish.view.play.FollowReadView.PlayRecordInterface;
import com.leqienglish.view.play.PlayBarView;
import com.leqienglish.view.play.PlayBarView.OnPlayBarListener;
import com.leqienglish.view.scroll.LeQiScrollView;
import com.leqienglish.view.scroll.OnScrollListener;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import cn.duoduo.entity.Content;

public class PlayLearneActivity extends Activity implements
		PlayRecordInterface, OnPlayBarListener, OnScrollListener,
		Handler.Callback {
	public static LOGGER logger = new LOGGER(PlayBarView.class);
	private PlayBarView playbarView;
	private Map<Integer, TextView> textViewMap;
	private LeQiScrollView leqiScroll;
	private Handler handler;
	private LinearLayout learneContent;
	/**
	 * 当前时间点的高度
	 */
	private int currenHeight;
	/**
	 * 当前正在播放的文本
	 */
	private TextView currentPlay;
	/**
	 * 上一次播放的文本
	 */
	private TextView lastPlay;
	private FollowReadView followReadView;
	/**
	 * 当前播放的时间点
	 */
	private int currentTimePoint;
	/**
	 * 下一个时间点
	 */
	private int nextTimePoint;

	private static final int CURRENT_PLAY_TIME = 1;
	private static final String KEY = "key";
	/**
	 * 是否是跟读
	 */
	private boolean isFollow = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.playerlearne);
		
		//this.getActionBar().setDisplayShowCustomEnabled(true);
		String key = this.getIntent().getStringExtra(AppType.KEY);
		Content content = (Content) AppData.getEntity(key);
		//this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		//TextView textView = (TextView) this.getWindow().findViewById(R.id.android_title);
	//	textView.setText("adfasdfsd");
		this.handler = new Handler(this);
		this.init(content);
		initPlay();

		// setContentView(R.layout.main);

	}

	protected void init(Content content) {
		if(content==null){
			return;
		}
		TextView textView = (TextView) this.findViewById(R.id.learne_title);
		textView.setText(content.getTitle());
		this.leqiScroll = (LeQiScrollView) this
				.findViewById(R.id.learne_scrollview);
		this.learneContent = (LinearLayout) this
				.findViewById(R.id.learne_contet);
		this.playbarView = (PlayBarView) this
				.findViewById(R.id.learne_playBarView);
		this.followReadView = (FollowReadView) this
				.findViewById(R.id.learne_followReadView);

		this.playbarView.setOnPlayBarListener(this);
		this.followReadView.setPlayRecordInterface(this);

	}

	private void initPlay() {
		String key = this.getIntent().getStringExtra(AppType.KEY);
		//Content content = (Content) AppData.getEntity(key);
		//getWindow().setTitle(content.getTitle());
		String fileName = AppData.getHasDownLoadLearnEPath(AppData
				.getEntity(key));
		String map3Path = fileName + "/leqi.mp3";
		String contentPath = fileName + "/leqi.txt";

		this.createLearneList(Util.readFile(contentPath));

		try {
			this.playbarView.setMediaPath(map3Path);

			this.currentPlay.setTextColor(0xff1874CD);
			this.currentPlay.setTextSize(16);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createLearneList(String content) {
		String[] strArr = content.split("\\[");
		this.textViewMap = new LinkedHashMap<Integer, TextView>();
		for (int i = 1; i < strArr.length; i++) {
			TextView textView = new TextView(this);
			textView.setText(removeTime(strArr[i]));
			textView.setTextColor(0xff000000);
			textView.setTextSize(16);
			textView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			this.textViewMap.put(this.findTime(strArr[i]), textView);

			this.learneContent.addView(textView);
			if (i == 1) {
				this.currentPlay = textView;
				this.currentTimePoint = this.findTime(strArr[i]);
			}
		}
	}

	private int findTime(String stence) {
		int index = stence.indexOf("]");
		String timeStr = stence.substring(0, index);
		return Util.getNumber(timeStr);
	}
	private String removeTime(String stence){
		int index = stence.indexOf("]");
		if(index==-1){
			return stence;
		}
		return stence.substring(index+1, stence.length());
	}

	@Override
	public void timeChange(int time) {
		// TODO Auto-generated method stub
		this.nextTimePoint = this.getNewTimePoint(time);
		if (this.isFollow) {
			if (this.nextTimePoint != this.currentTimePoint) {
				this.playbarView.stop();
				if (this.followReadView.isFollowRead()) {
					this.followReadView.playRecord();
				} else {
					this.followReadView.startRecord(this.nextTimePoint
							- this.currentTimePoint, false);
				}

				return;
			}
		}

		if (this.currentPlay != this.textViewMap.get(nextTimePoint)) {
			Bundle data = new Bundle();
			data.putInt(KEY, nextTimePoint);

			Message message = this.handler.obtainMessage(CURRENT_PLAY_TIME);
			message.setData(data);
			this.handler.sendMessage(message);
		}
	}

	@Override
	public void playComplete() {
		// TODO Auto-generated method stub
		if (this.followReadView.isFollowRead()) {
			this.followReadView.playRecord();
		} else {
			this.nextTimePoint = this.playbarView.getMediaLength();
			this.followReadView.startRecord(this.nextTimePoint
					- this.currentTimePoint, true);
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(int scrolly) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startplay() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取当前正在播放的时间
	 * 
	 * @param time
	 * @return
	 */
	private int getNewTimePoint(int time) {
		Set<Integer> key = this.textViewMap.keySet();
		if (time == this.playbarView.getMediaLength()) {
			return this.playbarView.getMediaLength();
		}
		int currentPlayTime = this.currentTimePoint;
		for (Integer t : key) {

			if (t > time) {
				break;
			}
			currentPlayTime = t;
		}

		return currentPlayTime;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case CURRENT_PLAY_TIME:
			int currentKey = msg.getData().getInt(KEY);
			logger.d("KEY="+KEY+"\tcurrentKey="+currentKey);
			if (this.currentPlay != this.textViewMap.get(currentKey)) {
				this.lastPlay = this.currentPlay;
				if (this.lastPlay != null) {
					this.lastPlay.setTextColor(0xff000000);
				}

				this.currentPlay = this.textViewMap.get(currentKey);
				if(this.currentPlay==null){
					break;
				}
				this.currentPlay.setTextColor(0xff1874CD);
				if (this.leqiScroll.getHeight() / 2 < currentPlay.getY()) {
					logger.d("(int)(currentPlay.getY()-this.leqiScroll.getHeight()/2) \t"
							+ (int) (currentPlay.getY() - this.leqiScroll
									.getHeight() / 2));
					// this.leqiScroll.setScaleY(currentPlay.getY());
					this.leqiScroll.scrollTo(0,
							(int) (currentPlay.getY() - this.leqiScroll
									.getHeight() / 2));
				}

			}
			break;
		}
		return false;
	}

	@Override
	public void startPlay() {
		this.playbarView.play();

	}

	@Override
	public void playNext() {
		// TODO Auto-generated method stub

		this.currentTimePoint = this.nextTimePoint;
		this.playbarView.playFrom(this.nextTimePoint);

	}

	@Override
	public void endRecord() {
		// TODO Auto-generated method stub
		logger.v("endRecord:" + this.currentTimePoint);

		this.playbarView.playFrom(this.currentTimePoint);

	}

	@Override
	public void rebackPlay() {
		// TODO Auto-generated method stub
		this.playbarView.playFrom(this.currentTimePoint);
	}
}
