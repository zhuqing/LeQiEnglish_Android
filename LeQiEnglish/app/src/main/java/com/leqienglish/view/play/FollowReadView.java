package com.leqienglish.view.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leqienglish.R;
import com.leqienglish.thread.playrecord.PlayRecordThread;
import com.leqienglish.thread.playrecord.RecordThread;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FollowReadView extends LinearLayout implements Handler.Callback {
	public static LOGGER logger = new LOGGER(PlayBarView.class);

	private PlayRecordInterface playRecordInterface;
	/**
	 * 麦克风button显示时的声音
	 */
	private MediaPlayer buttonShow;
	/**
	 * 录音时长
	 */
	private int recordLength;

	private ImageView play;
	private ImageView playNext;
	private ImageView record;
	private ImageView reback;
	/**
	 * 录音数据
	 */
	private List<short[]> recordList = new ArrayList<short[]>();
	private Handler handler;
	/**
	 * 当前是否为跟读
	 */
	private boolean isFollowRead;
	/**
	 * 是否为最后一条播放
	 */
	private boolean isLast;

	public FollowReadView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public FollowReadView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public FollowReadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub

		LayoutInflater lf = LayoutInflater.from(getContext());
		View view = lf.inflate(R.layout.playerrecord, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.addView(view);
		this.init();
	}

	public void init() {
		this.play = (ImageView) this.findViewById(R.id.playerRecord_play);
		this.play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAllAlpha();
				playRecordInterface.startPlay();
			}
		});
		this.playNext = (ImageView) this.findViewById(R.id.playerRecord_next);
		this.playNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAllAlpha();
				playRecordInterface.playNext();
			}
		});
		this.reback = (ImageView) this.findViewById(R.id.playerRecord_reback);
		this.reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAllAlpha();
				playRecordInterface.rebackPlay();
			}
		});

		this.record = (ImageView) this.findViewById(R.id.playerRecord_record);
		setAllAlpha();
		this.play.setAlpha(1.0f);
		this.handler = new Handler(this);
		try {
			buttonShowMedia();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setAllAlpha() {
		this.play.setAlpha(0.0f);
		this.playNext.setAlpha(0.0f);
		this.reback.setAlpha(0.0f);
		this.record.setAlpha(0.0f);
	}

	/**
	 * 初始化Mic显示时的声音
	 * 
	 * @throws IOException
	 */
	private void buttonShowMedia() throws Exception {
		
		this.buttonShow =  MediaPlayer.create(this.getContext(),R.raw.button_showing);
		
		//this.buttonShow.prepare();
		this.buttonShow.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				startUserRecord();
			}
		});
	}

	/**
	 * 开始用户录音
	 */
	private void startUserRecord() {
		RecordThread record = new RecordThread(this.recordList,
				this.recordLength, this.handler);
		record.start();
	}

	/**
	 * 开始录音
	 */
	public void startRecord(int recordLenth, boolean isLast) {
		// 比原录音长5秒
		this.isLast = isLast;
		this.recordLength = recordLenth + 500;
		logger.v("recordLength::" + recordLength);
		this.record.setAlpha(1.0f);
		this.buttonShow.start();
	}

	/**
	 * 播放录音
	 */
	public void playRecord() {
		this.isFollowRead = false;
		PlayRecordThread playRecord = new PlayRecordThread(this.recordList,
				this.handler);
		playRecord.start();
	}

	/**
	 * 开始觉定是走下一步还是 从新听
	 */
	public void startDecision(boolean hasNext) {
		this.setAllAlpha();
		if (hasNext) {
			this.playNext.setAlpha(1.0f);
		}

		this.reback.setAlpha(1.0f);
	}

	/**
	 * @return the playRecordInterface
	 */
	public PlayRecordInterface getPlayRecordInterface() {
		return playRecordInterface;
	}

	/**
	 * @param playRecordInterface
	 *            the playRecordInterface to set
	 */
	public void setPlayRecordInterface(PlayRecordInterface playRecordInterface) {
		this.playRecordInterface = playRecordInterface;
	}

	/**
	 * @return the isFollowRead
	 */
	public boolean isFollowRead() {
		return isFollowRead;
	}

	/**
	 * @param isFollowRead
	 *            the isFollowRead to set
	 */
	public void setFollowRead(boolean isFollowRead) {
		this.isFollowRead = isFollowRead;
	}

	/**
	 * 
	 * @author guona
	 * 
	 */
	public interface PlayRecordInterface {
		/**
		 * 开始播放
		 */
		public void startPlay();

		/**
		 * 播放下一个
		 */
		public void playNext();

		/**
		 * 结束录音
		 */
		public void endRecord();

		/**
		 * 回放
		 */
		public void rebackPlay();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case AppType.RECORD_OVER:
			this.isFollowRead = true;
			this.record.setAlpha(0.0f);
			logger.v("AppType.RECORD_OVER");
			this.playRecordInterface.endRecord();
			break;
		case AppType.PLAY_RECORD_OVER:

			startDecision(!this.isLast);
			break;
		default:

		}
		return false;
	}
}
