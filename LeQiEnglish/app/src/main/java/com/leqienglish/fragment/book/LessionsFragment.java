package com.leqienglish.fragment.book;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.leqienglish.R;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.view.book.LessonListViewAdapter;

import net.sf.json.JSONObject;

import java.util.List;

import cn.duoduo.entity.Content;

public class LessionsFragment extends Fragment implements
		Handler.Callback {
	private final static LOGGER LOG= new LOGGER(LessionsFragment.class);
	private Handler handler;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.lesson_list, container, false);
		this.listView = (ListView) contextView.findViewById(R.id.lesson_list);
		this.handler = new Handler(this);
		Bundle mBundle = getArguments();
		String key = mBundle.getString(AppType.KEY);
		Content content = (Content) AppData.getEntity(key);
		getAllLession( content );
		return contextView;

	}
	
	/**
	 * 获取book下的所有课程
	 * @param content
	 */
	private void getAllLession(Content content ){
		if(content==null){
			LOG.d("Content == null");
			return;
		}
		String lessons = AppData.getUrlByKey(URLDataUtil.CONTENT_LIST_TYPE)
				+ "?parentId=" + content.getId();
		new DownLoadJSONThread(this.handler, lessons, AppType.BOOK_LESSION)
				.start();
	}
	
	/**
	 * 初始化 book列表
	 * @param jsonObject
	 * @throws Exception
	 */
	private void initListView(JSONObject jsonObject) throws Exception{
		List bookList = URLDataUtil.json2List(jsonObject);
		LessonListViewAdapter listViewAdapter = new LessonListViewAdapter(
				this.getActivity().getApplicationContext(), bookList);
		this.listView.setAdapter(listViewAdapter);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle data = msg.getData();
		switch(msg.what){
		case AppType.BOOK_LESSION:
			JSONObject json = JSONObject.fromObject(data.getString(AppType.JSON));
			try {
				this.initListView(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return false;
	}

}
