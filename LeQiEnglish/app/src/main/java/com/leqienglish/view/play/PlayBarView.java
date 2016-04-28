package com.leqienglish.view.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.leqienglish.R;
import com.leqienglish.util.LOGGER;

import java.io.IOException;

/**
 * 播放工具条
 * 
 * @author guona
 * 
 */
public class PlayBarView extends LinearLayout implements Handler.Callback {
	public static LOGGER logger = new LOGGER(PlayBarView.class);
	/**
	 * 播放器的时间进度条
	 */
	private ProgressBar player;
	/**
	 * 播放的最大时长
	 */
	private int max;
	/**
	 * 播放的路径
	 */
	private String path;
	/**
	 * 文件播放器
	 */
	private MediaPlayer mediaPlayer;// 播放文件
	/**
	 * 是否播放结束
	 */
	private boolean isComplete;
	/**
	 * 播放器的监听
	 */
	private OnPlayBarListener onPlayBarListener;

	private Handler handler;
	/**
	 * 延时5毫秒
	 */
	private int delay = 5;

	public PlayBarView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public PlayBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PlayBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater lf = LayoutInflater.from(getContext());
		View view = lf.inflate(R.layout.playerbar, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.handler = new Handler(this);
		this.setAlpha(0.5f);
		this.addView(view);
		this.init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		this.player = (ProgressBar) this.findViewById(R.id.player_progress);
		//this.player.setMax(max);

	}

	/**
	 * 
	 * 设置Media播放路径
	 * 
	 * @param path
	 * @throws IllegalStateException
	 * @throws IOException
	 */

	public void setMediaPath(String path) throws IllegalStateException,
			IOException {
		logger.d(path);
		if(this.mediaPlayer==null){
			this.mediaPlayer = new MediaPlayer();
		}
		this.mediaPlayer.setDataSource(path);
		this.mediaPlayer.prepare();
		this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				PlayBarView.this.isComplete = true;

			}
		});
		this.mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener(){

			@Override
			public void onSeekComplete(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.start(); 
			}});
		
		this.player.setProgress(0);
		this.player.setMax(this.mediaPlayer.getDuration());
	}

	public void playFrom(int time) {
		this.isComplete = false;
		this.mediaPlayer.seekTo(time);
		this.handler.sendMessageDelayed(this.handler.obtainMessage(),
				this.delay);
	}

	public void play() {
		this.isComplete = false;
		this.mediaPlayer.start();
		this.handler.sendMessageDelayed(this.handler.obtainMessage(),
				this.delay);
	}

	public void stop() {
		this.mediaPlayer.pause();
	
	}

	@Override
	public boolean handleMessage(Message msg) {
		//logger.d("getCurrentPosition\t"+this.mediaPlayer.getCurrentPosition());
		//logger.d("isComplete\t"+this.isComplete);
		//this.player.setProgress((int) (this.mediaPlayer.getCurrentPosition()*1.0/this.mediaPlayer.getDuration()*100));
		// TODO Auto-generated method stub
		this.player.setMax(this.mediaPlayer.getDuration());
		this.player.setProgress(this.mediaPlayer.getCurrentPosition());
	
		if(!this.isComplete){
			this.handler.sendMessageDelayed(this.handler.obtainMessage(),
					this.delay);
		}
		if (this.onPlayBarListener == null) {
			return false;
		}
		if (this.isComplete) {
			this.onPlayBarListener.playComplete();
			return false;
		}
		if (!this.mediaPlayer.isPlaying()) {
			this.onPlayBarListener.stop();
			return false;
		}

		
		
		this.onPlayBarListener
				.timeChange(this.mediaPlayer.getCurrentPosition());

		return false;
	}
	/**
	 * 获取播放视频的长度
	 * @return
	 */
	public int getMediaLength(){
		if(this.mediaPlayer!=null){
			return this.mediaPlayer.getDuration();
		}
		return -1;
	}
	
	
	
	/**
	 * @return the onPlayBarListener
	 */
	public OnPlayBarListener getOnPlayBarListener() {
		return onPlayBarListener;
	}

	/**
	 * @param onPlayBarListener the onPlayBarListener to set
	 */
	public void setOnPlayBarListener(OnPlayBarListener onPlayBarListener) {
		this.onPlayBarListener = onPlayBarListener;
	}



	/**
	 * 播放时间改变
	 * 
	 * @author guona
	 * 
	 */
	public interface OnPlayBarListener {
		/**
		 * 正在播放播放 的时间 点
		 * 
		 * @param time
		 */
		public void timeChange(int time);

		/**
		 * 播放完成
		 */
		public void playComplete();

		/**
		 * 停止播放
		 */
		public void stop();
		/**
		 * 开始播放
		 */
		public void startplay();
	}

}

