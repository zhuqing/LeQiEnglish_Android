package com.leqienglish.activity.behavor;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.leqienglish.PlayActivity;
import com.leqienglish.thread.playrecord.PlayRecordThread;
import com.leqienglish.thread.playrecord.RecordThread;
import com.leqienglish.util.AppType;
import com.leqienglish.util.Util;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.leqienglish.R;
import cn.duoduo.entity.Content;

@SuppressLint("NewApi")
public class PlayActivityBehavor implements Handler.Callback{
	private PlayActivity playerActivity;

	/**
	 * 播放按钮
	 */
	private Button playButton;
	private Button rebackButton;
	private Button textRebackButton;
	private ImageView playOrMic;
	private Content learnE;
	private MediaPlayer mediaPlayer;// 播放文件
	private MediaPlayer buttonShow;// 播放提示音
	private Handler handler;
	private Handler behavorHandler;
	private Timer timer = new Timer();
	/**
	 * 是否录音完成
	 */
	private boolean isRecorderPlay = false;
	private String filePath;
	private int playIndex = 0;
	private List<short[]> recordList = new ArrayList<short[]>();
	
	/**
	 * 录音时长
	 */
	private int recordTime;

	// 时间点列表
	private List<Integer> timeList;
	// 时间点 与时间点该显示的句子
	private Map<Integer, String> englishMap;

	public PlayActivityBehavor(PlayActivity playerActivity, Content learnE) {
		this.playerActivity = playerActivity;
		this.learnE = learnE;
		this.handler = new Handler(this.playerActivity);
		this.behavorHandler = new Handler(this);
		this.init();
		this.initFilePath();
		this.addListener();
		this.initMediaPlayer();

		this.initMicPicMusic();
		this.englishText();

		this.initTime();
		this.sendText(playIndex);
		playIndex++;
	}

	/**
	 * 初始化组件
	 */
	private void init() {

		this.playButton = (Button) this.playerActivity.findViewById(R.id.play);
		this.rebackButton = (Button) this.playerActivity
				.findViewById(R.id.reback);
		this.textRebackButton = (Button) this.playerActivity
				.findViewById(R.id.text_reback);
		this.playOrMic = (ImageView) this.playerActivity
				.findViewById(R.id.playOrMirc);
	}

	/**
	 * 初始化提示音
	 */
	private void initMicPicMusic() {

		try {
			FileDescriptor fd = this.playerActivity.getAssets()
					.openFd("button1.mp3").getFileDescriptor();
			this.buttonShow = new MediaPlayer();
			this.buttonShow.setDataSource(fd);
			this.buttonShow.prepare();
			this.buttonShow
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							handler.sendEmptyMessage(AppType.CHANGE_IMAGR_ALPH_1);
							startRecord();
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 开始录音
	 */
	private void startRecord() {
		this.recordTime = this.countRecorderDuration(this.playIndex);
		RecordThread record = new RecordThread(this.recordList, recordTime,this.behavorHandler);
		record.start();

	}
	/**
	 * 	
	 * 计算录音的时长
	 * @param playIndex 播放的位置
	 * @return
	 */
	private int countRecorderDuration(int playIndex){
		int duration = 0;
		if (playIndex < this.timeList.size()) {
			duration = this.timeList.get(playIndex)
					- this.timeList.get(playIndex - 1);
		} else {
			duration = this.mediaPlayer.getDuration()
					- this.timeList.get(playIndex - 1);
		}
		
		return duration+500;
	}

	/**
	 * 停止录音
	 */
	private void stopRecorder() {
		handler.sendEmptyMessage(AppType.CHANGE_IMAGR_ALPH_0);
		isRecorderPlay = true;
		play(this.playIndex-1);
	}

	/**
	 * 添加事件
	 */
	private void addListener() {
		this.playOrMic.setOnClickListener(onclickHandler);
		this.playButton.setOnClickListener(onclickHandler);
		this.rebackButton.setOnClickListener(onclickHandler);
		this.textRebackButton.setOnClickListener(onclickHandler);
	}

	private void initMediaPlayer() {
		this.mediaPlayer = new MediaPlayer();

		String path = this.filePath + "/leqi.mp3";

		try {
			this.mediaPlayer.setDataSource(path);
			this.mediaPlayer.prepare();
			this.mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.d("test", what + ":" + extra);
					return false;
				}
			});

			this.mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							timer.cancel();
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initTime() {
		this.timer.schedule(timerTask, 0, 100);
		// this.timer.
	}

	/**
	 * 获取文件所在路径
	 */
	private void initFilePath() {
		String pathName = this.learnE.getAudioPath().substring(
				this.learnE.getAudioPath().lastIndexOf("\\") + 1);
		pathName = Util.urlEncode(pathName);
		this.filePath = Util.getLearnePath() + pathName;
	}

	/**
	 * 获取文本
	 */
	private void englishText() {
		String textPath = this.filePath + "/leqi.txt";
		String english = Util.readFile(textPath);
		String[] englishArr = english.split("\r\n");
		this.getTime(englishArr);
	}

	/**
	 * 解析时间与句子
	 * 
	 * @param englishArr
	 */
	private void getTime(String[] englishArr) {
		this.timeList = new ArrayList<Integer>();
		this.englishMap = new HashMap<Integer, String>();
		for (String str : englishArr) {
			str = str.trim();
			int index = str.indexOf(']');
			String timeStr = str.substring(1, index);
			Integer time = Util.getNumber(timeStr);
			timeList.add(time);
			String engSen = str.substring(index + 1);
			this.englishMap.put(time, engSen);
		}
	}

	/**
	 * 发送文本到Activity
	 * 
	 * @param index
	 */
	private void sendText(int index) {
		Message mess = new Message();
		Bundle bund = mess.getData();

		bund.putCharSequence("txt", englishMap.get(timeList.get(index)));
		mess.what = AppType.CHANGE_TEXT;
		handler.sendMessage(mess);
	}

	private void recorderPlay() throws IllegalStateException {
	   PlayRecordThread playRecord = new PlayRecordThread(this.recordList,this.behavorHandler);
	   playRecord.start();
	}

	/**
	 * 监听 mp3播放
	 */
	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			int currentTime = mediaPlayer.getCurrentPosition();
			if (currentTime > timeList.get(playIndex)) {
				if (isConn) {
					mediaPlayer.pause();
					if (isRecorderPlay) {
						try {
							recorderPlay();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						handler.sendEmptyMessage(AppType.CHANGE_IMAGR_ALPH_1);
						isRecorderPlay = false;
						buttonShow.start();
					}

					isConn = false;
				}

			}

		}
	};

	public boolean isConn = true;

	private View.OnClickListener onclickHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			isRecorderPlay = false;
			switch (v.getId()) {
			case R.id.playOrMirc:
				mediaPlayer.start();
				handler.sendEmptyMessage(AppType.CHANGE_IMAGR);
				break;
			case R.id.play:
			
				play(playIndex);
				sendText(playIndex);
				if (playIndex < timeList.size()) {
					playIndex++;
				}

				// mediaPlayer.
				break;
			case R.id.text_reback:

			case R.id.reback:
				play(playIndex - 1);
				break;

			}
		}
	};

	private void play(int index) {
		isConn = true;
		mediaPlayer.seekTo(timeList.get(index));
		mediaPlayer.start();
	}

	@Override
	public boolean handleMessage(Message msg) {
		isRecorderPlay = false;
		switch(msg.what){
		case AppType.RECORD_OVER:
			this.stopRecorder();
			break;
		case AppType.PLAY_RECORD_OVER:
			break;
		default:
			
		}
		return false;
	}

}
