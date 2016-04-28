package com.leqisoft.view.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;


import com.leqisoft.util.log.LOGGER;

import java.io.IOException;

/**
 * 播放工具条
 * 
 * @author guona
 * 
 */
public class LeQiPlayer implements Handler.Callback {
	public static LOGGER logger = new LOGGER(LeQiPlayer.class);
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

	public LeQiPlayer(String path) {
	   this.path = path;
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
				LeQiPlayer.this.isComplete = true;

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

