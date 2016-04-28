package com.leqienglish.thread.playrecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.leqienglish.util.AppType;
import com.leqienglish.util.Util;

import java.util.List;

/***
 * 录音线程
 * 
 * @author guona
 * 
 */
public class RecordThread extends Thread {
	private List<short[]> byteBufferList;
	private long duration;
	private Handler handler;
	private boolean isRecording = false;

	public RecordThread(List<short[]> byteBufferList, long duration,
			Handler handler) {
		this.byteBufferList = byteBufferList;
		this.duration = duration;
		this.handler = handler;
	}

	public void run() {
		Log.d(this.getName(), "====start======");
		if (isRecording) {
			return;
		}

		isRecording = true;
		try {
			int bufferSize = AudioRecord.getMinBufferSize(Util.frequence,
					AudioFormat.CHANNEL_IN_MONO, Util.audioEncoding);
			
			
			AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC,
					Util.frequence,AudioFormat.CHANNEL_IN_MONO, Util.audioEncoding,
					bufferSize);
			// 实例AudioTrack
		
			short[] buffer = new short[bufferSize];
			
			long startTime = System.currentTimeMillis();
			record.startRecording();
			// 开始播放
			//track.setStereoVolume(0.7f, 0.7f);
			//track.play();
			while ((System.currentTimeMillis() - startTime) < this.duration) {
				int bufferReadResult = record.read(buffer, 0, buffer.length);
				short[] data = new short[bufferReadResult];
				System.arraycopy(buffer, 0, data, 0, bufferReadResult);
				this.byteBufferList.add(data);
			
			}
		
			record.stop();
			record.release();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.handler.sendEmptyMessage(AppType.RECORD_OVER);
		Log.d(this.getName(), "====end=====");
		isRecording = false;
	}
}
