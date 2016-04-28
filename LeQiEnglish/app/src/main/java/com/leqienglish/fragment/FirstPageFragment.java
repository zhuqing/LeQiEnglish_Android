package com.leqienglish.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leqienglish.R;
import com.leqienglish.annotation.util.EntityAndJSON;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.PageUtil;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.util.bitmap.BitmapUtil;
import com.leqienglish.view.ListViewAdapter;
import com.leqienglish.view.adapter.FirstPageAdapterListView;
import com.leqisoft.toast.ToastUtil;
import com.leqisoft.view.list.LeQiListView;
import com.leqisoft.view.list.OnRefreshListener;


import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.duoduo.entity.Content;
import cn.duoduo.entity.PageParam;

public class FirstPageFragment extends Fragment implements
		Handler.Callback {
	public LOGGER logger = new LOGGER(FirstPageFragment.class);
	private LeQiListView listView;
	private Handler handler;
	private int type;
	/**
	 * 图片宽度的缩放比例
	 */
	private int scaleWidth;
	/**
	 * 分页
	 */
	private PageParam page;
	/**
	 * listView适配器
	 */
	private ListViewAdapter listViewAdapter;
	/**
	 * 下载的课程
	 */
	private Content downLoadLearnE;
	

	
	public FirstPageFragment(){
		
		this.handler = new Handler(this);
		type = AppType.NET_DATA;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.lesson_list, container,
				false);
	
		this.page = PageUtil.initPage(getActivity());
		this.getData();
	//	createListView();
		return contextView;
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		Thread dataThread = null;

		dataThread = new DownLoadJSONThread(this.handler,
				AppData.getUrlByKey(URLDataUtil.CONTENT_LIST),
				AppType.URL_ALLLESSION);

		if (dataThread != null) {
			dataThread.start();
		}
	}

	/**
	 * 根据数据创建listView
	 * 
	 * @param json
	 */
	private void createListView(JSONObject json) {
	
		this.listView = (LeQiListView) this.getActivity().findViewById(
				R.id.lesson_list);
		this.listView.setRefershListener(onRefreshListener);
		scaleWidth = listView.getWidth()-26;
		this.listViewAdapter = new FirstPageAdapterListView(this
				.getActivity().getApplicationContext(),  this.handler,scaleWidth);
		
		this.listView.setAdapter(listViewAdapter);
		try {
			this.listViewAdapter.addItems(this.createLearnEList(json));
		} catch (Exception e) {

		}
//		this.listView
//				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//					@Override
//					public boolean onItemLongClick(AdapterView<?> parent,
//							View view, int position, long id) {
//						// TODO Auto-generated method stub
//						Content content = listViewAdapter.getItem(position);
//						Intent in = new Intent();
//						in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						if (content.getContenttype() == LeQiType.WenZhang) {
//							in.setClass(getActivity().getApplicationContext(),
//									ReadContentActivity.class);
//						} else if (content.getContenttype() == LeQiType.FELLOW_ENGLISH_BOOK) {
//							in.setClass(getActivity().getApplicationContext(),
//									BookActivity.class);
//						}
//
//						in.putExtra(AppType.KEY, AppData.getKey(content));
//						getActivity().startActivity(in);
//						return true;
//					}
//				
//				});
	}
	
	/**
	 * 添加更多的数据
	 * @param json
	 */
	private void addMoreItems(JSONObject json) throws Exception {
		if(this.listView == null || this.listViewAdapter == null){
			return;
		}
		this.listView.hideFooterView();
		List list = this.createLearnEList(json);
		if(list.size()==0){
			ToastUtil.show(getActivity(), "没有更多信息");
			return;
		}
		Collections.reverse(list);
		
		this.listViewAdapter.addItems(list);
	}
	
	private void addNewItems(JSONObject json){
		if(this.listView == null || this.listViewAdapter == null){
			return;
		}
		this.listView.hideHeaderView();
		List list = null;
		try {
			list = this.createLearnEList(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.size()==0){
			ToastUtil.showTop(getActivity(), "已经是最新信息");
			return;
		}
		this.listViewAdapter.addItems(0,list);
	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		String jsonStr = data.getString("json");
		switch (msg.what) {
		case AppType.URL_ALLLESSION:
		
			try {
				JSONObject json = JSONObject.fromObject(jsonStr);		
				this.createListView(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AppType.URL_MORE_CONTENT:
			
			try {
				JSONObject json = JSONObject.fromObject(jsonStr);		
				this.addMoreItems(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 break;
		case AppType.URL_NEW_CONTENT:
			try {
				JSONObject json = JSONObject.fromObject(jsonStr);		
				this.addNewItems(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AppType.URL_DOWNLOAD:
			// Toast.makeText(this.getContext(), "downing!", 1000).show();
			break;
		case AppType.URL_DOWNLOAD_OVER:
			// Content learnE = data.getParcelable(AppType.DATA);
			// new ManageSQLThread(this.getContext(), this.handler,
			// learnE, DataBaseOperateEnum.INSERT).start();
			// 图片下载完成。。

			// Toast.makeText(this.getContext(), "downover!", 500).show();
			break;
		case AppType.URL_IMAGE_DOWNLOAD_OVER:
			String imageFileName = data.getString(AppType.URL_IMAGE_NAME);
			ImageView imageView = AppData.getImageViewByFileName(imageFileName);
			Bitmap bitmap = BitmapUtil.sacalTo(BitmapFactory.decodeFile(AppData
					.getImageFileDir() + imageFileName), scaleWidth);
			imageView.setImageBitmap(bitmap);
			AppData.addImageName(imageFileName);
			//AppData.setImageView(handler, null, imageView, scaleWidth);
			break;
		case AppType.SELECT:
		
			break;
		}
		return false;
	}

	private List<Content> createLearnEList(JSONObject json)
			throws Exception {

		List<Content> list = new ArrayList<Content>();
		JSONArray jsonArr = json.getJSONArray("list");
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject item = jsonArr.getJSONObject(i);
			logger.v(item.toString());
			try {
				Content learnE = EntityAndJSON.json2Object(item);
				list.add(learnE);
				AppData.putContent(learnE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return list;
	}
	
	
	private final OnRefreshListener onRefreshListener = new OnRefreshListener(){

		@Override
		public void onLoadingMore() {
			//int index = listView.getLastVisiblePosition();
			
			Content content = listViewAdapter.getLastItem();
			if(content==null){
				return;
			}
			DownLoadJSONThread dataThread = new DownLoadJSONThread(handler,
					getMoreUrl(content,URLDataUtil.CONTENT_MORE_CONTENT),
					AppType.URL_MORE_CONTENT);

			if (dataThread != null) {
				dataThread.start();
			}
			
		}
		
		private String getMoreUrl(Content content,String type){
			String url = AppData.getUrlByKey(type);
			url+="?id="+content.getId()+"&type="+content.getContenttype();
			return url;
		}
	
		@Override
		public void onLoadingNew() {
			
			Content content = listViewAdapter.getItem(0);
			if(content == null){
				return;
			}
			
			new DownLoadJSONThread(handler,
					getMoreUrl(content,URLDataUtil.CONTENT_NEW_CONTENT),
					AppType.URL_NEW_CONTENT).start();

			
		}};
	
}
