package com.leqienglish.commpent;

import com.leqienglish.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


/**
 * 评论组件
 * @author guona
 *
 */
public  class Comment extends LinearLayout {

	public Comment(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	public Comment(Context context, AttributeSet attrs) {
		super(context, attrs);
		 ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
	                R.layout.comment, this);
		// TODO Auto-generated constructor stub
	}

	
	
}
