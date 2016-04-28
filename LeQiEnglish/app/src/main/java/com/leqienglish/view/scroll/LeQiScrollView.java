package com.leqienglish.view.scroll;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LeQiScrollView extends ScrollView implements Handler.Callback{

	private OnScrollListener onScrollListener;
	private Handler handler;
	/**
	 * 上一次Y值
	 */
	private int lastScrollY;
	/**
	 * 延时的时间
	 */
	private int delayTime = 5;
	private final static String ONSCROLL="onScroll";
	public LeQiScrollView(Context context) {
		this(context, null);
	}

	public LeQiScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LeQiScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.handler = new Handler(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(this.onScrollListener != null){
			this.onScrollListener.onScroll(this.getScrollY());
		}
		
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
			//延时5毫秒发送消息
			this.handler.sendMessageDelayed(this.handler.obtainMessage(), delayTime);
			break;
		}
		return super.onTouchEvent(ev);

	}
	

	/**
	 * @param onScrollListener the onScrollListener to set
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	@Override
	public boolean handleMessage(Message msg) {
		int scrolly = this.getScrollY();
		if(this.lastScrollY!=scrolly){
			this.lastScrollY = scrolly;
			//延时5毫秒发送消息
			this.handler.sendMessageDelayed(this.handler.obtainMessage(), delayTime);
		}
		
		if(this.onScrollListener!=null){
			this.onScrollListener.onScroll(scrolly);
		}
			
		return false;
	}

}
