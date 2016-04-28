package com.leqienglish.view.bottombar;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leqienglish.R;

import cn.duoduo.entity.Content;

public class BottomBar extends LinearLayout implements Handler.Callback {

	private RelativeLayout hotDegree;
	private RelativeLayout comment;
	private RelativeLayout great;
	private RelativeLayout share;
	private RelativeLayout subscript;
	private ImageView greatImageView;
	private ImageView subscriptImageView;
	private Content content;
	private Handler handler;

	
	public BottomBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public BottomBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public BottomBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater lf = LayoutInflater.from(getContext());
		View view = lf.inflate(R.layout.bottombar, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.MATCH_PARENT));
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 40));
		this.handler = new Handler(this);
		// this.setAlpha(0.5f);
		this.addView(view);
		initView();

	}
	
	/**
	 * 更新显示数据
	 * @param content
	 */
	public void updateContent(Content content){
		this.content = content;
	}
	/**
	 * 更新显示
	 * @param content
	 */
	private void updateView(Content content){
		//content.get
	}
   
	private void initView() {
		this.hotDegree = (RelativeLayout) this
				.findViewById(R.id.bottombar_hotdegree_button);
		this.great = (RelativeLayout) this
				.findViewById(R.id.bottombar_great_button);
		this.comment = (RelativeLayout) this
				.findViewById(R.id.bottombar_comment_button);
		this.share = (RelativeLayout) this
				.findViewById(R.id.bottombar_share_button);
		this.subscript = (RelativeLayout) this
				.findViewById(R.id.bottombar_subscription_button);
		registAction(great);
		registAction(comment);
		registAction(share);
		registAction(hotDegree);
		registAction(subscript);
		
		this.greatImageView = (ImageView) this.findViewById(R.id.bottombar_great_image);
		this.subscriptImageView = (ImageView) this.findViewById(R.id.bottombar_subscription_image);

	}

	private void registAction(final View view) {
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// hotDegree.getBackground().
					view.setBackgroundResource(R.drawable.bottombar_button_press);
					if(view == subscript){
					
						subscriptImageView.setImageResource(R.drawable.red_heart);
					}
					if(view == great){
						greatImageView.setImageResource(R.drawable.blue_great);
					}
					break;

				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					view.setBackgroundResource(R.drawable.bottombar_button);
					break;

				}
				return true;
			}
		});
	}

	public void initContent(Content content) {

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
