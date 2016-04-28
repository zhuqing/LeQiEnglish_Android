package com.leqienglish;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.util.file.LearnEnglishUtil;
import com.leqienglish.view.book.LessonListViewAdapter;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.duoduo.entity.Content;

public class BookActivity extends Activity implements Handler.Callback{
	/**
	 * ActionBar对象的引用
	 */
	private ActionBar mActionBar;
	/**
	 * ViewPager对象的引用
	 */
	private FragmentTabHost  fragmentTabHost;
	
	private ListView lessonListView;
	private TextView lessonDesciripton;
	private ImageView headerImageView;
	private TextView title;
	private Handler handler;
	

	/**
	 * 装载Fragment的容器，我们的每一个界面都是一个Fragment
	 */
	private List<Fragment> mFragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.book);
//		fragmentTabHost = (FragmentTabHost) this.findViewById(R.id.book_tabHostFrament);
//		fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);  
		mFragmentList = new ArrayList<Fragment>();
		this.handler = new Handler(this);
	
		Intent in = this.getIntent();
		String key = in.getStringExtra(AppType.KEY);
		Content content = (Content) AppData.getEntity(key);
		initFragment(content);
		this.initView(content);
		mActionBar = this.getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		//mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
		mActionBar.setDisplayShowTitleEnabled(false);  
		mActionBar.setDisplayShowHomeEnabled(true);  
		//mActionBar.
		
		// 设置ActionBar的导航模式为Tab
	//	mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getLessons(content);
	}
	
	private void initView(Content content){
		this.lessonListView = (ListView) this.findViewById(R.id.book_lessonlist);
		this.lessonDesciripton = (TextView) this.findViewById(R.id.book_descirption);
		if(content!=null){
			this.lessonDesciripton.setText(content.getContent());
		}
		lessonDesciripton.setTypeface(AppData.getTypeFace());
		
		this.headerImageView =  (ImageView) this.findViewById(R.id.book_header_image);
		this.title = (TextView) this.findViewById(R.id.book_title);
		if(content!=null){
			AppData.setImageView(this.handler,content,this.headerImageView,60);
			this.title.setText(content.getTitle());
		}
	}
	
	private void getLessons(Content content){
		if(content==null){
			return;
		}
		String url = AppData.getUrlByKey(URLDataUtil.CONTENT_LIST_PARENTID)+"?parentId="+content.getId();
		new DownLoadJSONThread(this.handler,url,AppType.BOOK_LESSION).start(); 
	}
	  
	public void initFragment(Content content){
//		Bundle data = new Bundle();
//		data.putString("info", content.getContent());
//		Bundle iddata = new Bundle();
//		iddata.putLong(AppType.ID, content.getId());
//		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("简介").setIndicator("简介"), BookInfoFragment.class, null);
//		
//		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("列表").setIndicator("列表"), LessionsFragment.class, null);
	}
	
	/**
	 * 用列表显示   
	 * @param jsonStr
	 */
	private void showLessonList(String jsonStr){
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		try {
			final List list = URLDataUtil.json2List(jsonObject);
			LessonListViewAdapter lessonListViewAdapter = new LessonListViewAdapter(this.getApplicationContext(),list);
			this.lessonListView.setAdapter(lessonListViewAdapter);
			this.lessonListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Content content = (Content) list.get(position);
					if(LearnEnglishUtil.hasDownloadFile(AppData.getKey(content))){
						Intent in = new Intent();
						in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						in.setClass(getApplicationContext(), PlayLearneActivity.class);
						in.putExtra(AppType.KEY, AppData.getKey(content));
						startActivity(in);
					}
				}
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case AppType.BOOK_LESSION:
			Bundle data = msg.getData();
			String jsonStr = data.getString(AppType.JSON);
			showLessonList(jsonStr);
			break;
		}
		return false;
	}
}
