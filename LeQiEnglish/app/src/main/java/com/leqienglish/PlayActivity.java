package com.leqienglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.activity.behavor.PlayActivityBehavor;
import com.leqienglish.util.AppType;

import cn.duoduo.entity.Content;

public class PlayActivity extends Activity implements  Handler.Callback {
	private TextView textView;
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		this.init();
	}  

	private void init(){
		this.textView = (TextView) this.findViewById(R.id.text);
		this.imageView = (ImageView) this.findViewById(R.id.playOrMirc);
		Intent in = this.getIntent();
		Content learnE = (Content) in.getParcelableExtra(AppType.LEARNE);
		new PlayActivityBehavor(this,learnE);
	}

	@Override
	public boolean handleMessage(Message message) {
		Bundle data = message.getData();
		
		switch(message.what){
		case AppType.CHANGE_TEXT:
			
			String txt = data.getString("txt");
			this.textView.setText(txt);
			break;
		case AppType.CHANGE_IMAGR:
			
			imageView.setImageResource(R.drawable.mira);
			
		case AppType.CHANGE_IMAGR_ALPH_0:
			imageView.setAlpha(0);
			break;
		case AppType.CHANGE_IMAGR_ALPH_1:
			imageView.setAlpha(1);
			break;
			default:
				
				
		}
		return false;
	}

}
