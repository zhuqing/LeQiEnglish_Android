package com.leqienglish;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.activity.behavor.InitDataBehavor;
import com.leqienglish.util.AppData;

import cn.duoduo.entity.Content;

/**
 * 欢迎页
 * 
 * @author guona
 * 
 */
public class WelcomeActivity extends Activity {
	private InitDataBehavor updateBehavor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.welcome_layout);
		// 初始化数据
		///AppData.initData(getApplicationContext());
		//this.updateBehavor = new InitDataBehavor(this);
		//this.updateBehavor.initURLData();

		init();
	}

	private void init() {

		///Content content = AppData.getFamousWords();
		TextView words = (TextView) this.findViewById(R.id.welcome_famous_word);
		TextView auther = (TextView) this
				.findViewById(R.id.welcome_famous_word_author);
		//words.setTypeface(AppData.getTypeFace());
		//auther.setTypeface(AppData.getTypeFace());
		//if (content == null) {
			words.setText("“We have always found that people are most productive in small teams with tight budgets, time lines and the freedom to solve their own problems.”");
			auther.setText("------Socrates 苏格拉底 ");
//		} else {
//			words.setText(content.getContent());
//			auther.setText("------"+content.getFromwhere());
//		}

		auther.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				turn2NextActivity();
			}
		});
		

	}

	/**
	 * 资源加载完成跳转界面 或时间到跳转界面
	 */
	public void turn2NextActivity() {

		Intent in = new Intent();


		in.setClass(this, MainActivity.class);
		// Intent in = new Intent();
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		this.startActivity(in);
		this.finish();

	}

}
