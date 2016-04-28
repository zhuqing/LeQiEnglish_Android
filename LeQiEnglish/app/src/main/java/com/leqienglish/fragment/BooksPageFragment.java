package com.leqienglish.fragment;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import cn.duoduo.entity.Catalog;
import cn.duoduo.entity.Content;

import com.leqienglish.BookActivity;
import com.leqienglish.R;
import com.leqienglish.ReadContentActivity;

import com.leqienglish.entity.DataBaseOperateEnum;
import com.leqienglish.thread.sql.ManageSQLThread;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.LeQiType;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.view.BooksGridViewAdapter;
import com.leqienglish.view.ListViewAdapter;
import com.leqienglish.view.book.BookListViewAdapter;
import com.leqienglish.annotation.util.EntityAndJSON;

public class BooksPageFragment extends Fragment implements
		Handler.Callback {
	private static final LOGGER LOGG = new LOGGER(BooksPageFragment.class);

	private Handler handler;
	/**
	 * 分类列表
	 */
	private GridView gridView;
	/**
	 * 课程列表
	 */
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View contextView = inflater.inflate(R.layout.books, container, false);
		this.gridView = (GridView) contextView
				.findViewById(R.id.books_gridView);
		this.listView = (ListView) contextView
				.findViewById(R.id.books_listview);
		this.handler = new Handler(this);
		this.init();

		return contextView;
	}

	private void init() {

		this.getDataType();
	}

	/**
	 * 初始化表格
	 *

	 * @throws Exception
	 */
	private void initGridView(JSONObject jsonObject) throws Exception {
		List catalogList = URLDataUtil.json2List(jsonObject);

		BooksGridViewAdapter<Catalog> booksGridViewAdapter = new BooksGridViewAdapter<Catalog>(
			
				this.getActivity().getApplicationContext(), catalogList);

		this.gridView.setAdapter(booksGridViewAdapter);
		
	
		setListViewHeightBasedOnChildren(gridView);

	}
	

    public static void setListViewHeightBasedOnChildren(GridView listView) {  
        // 获取listview的adapter  
           ListAdapter listAdapter = listView.getAdapter();  
           if (listAdapter == null) {  
               return;  
           }  
           // 固定列宽，有多少列  
           int col = 6;// listView.getNumColumns();  
           int totalHeight = 10;  
           // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，  
           // listAdapter.getCount()小于等于8时计算两次高度相加  
           for (int i = 0; i < listAdapter.getCount(); i += col) {  
            // 获取listview的每一个item  
               View listItem = listAdapter.getView(i, null, listView);  
               listItem.measure(0, 0);  
               // 获取item的高度和  
               totalHeight += listItem.getMeasuredHeight()+10;  
           }  
      
           // 获取listview的布局参数  
           ViewGroup.LayoutParams params = listView.getLayoutParams();  
           // 设置高度  
           params.height = totalHeight;  
           // 设置margin  
           ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);  
           // 设置参数  
           listView.setLayoutParams(params);  
       }  
	
	/**
	 * 初始化 book列表
	 * @param jsonObject
	 * @throws Exception
	 */ 
	private void initListView(JSONObject jsonObject) throws Exception{
		List bookList = URLDataUtil.json2List(jsonObject);
		final BookListViewAdapter listViewAdapter = new BookListViewAdapter(
				this.getActivity().getApplicationContext(),  this.handler,this.listView.getWidth()*2.0f/5);
		listViewAdapter.setItems(bookList);
		this.listView.setAdapter(listViewAdapter);
		this.listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long id) {
				Content content = listViewAdapter.getItem(index);
				Intent in = new Intent();
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				in.setClass(getActivity().getApplicationContext(), BookActivity.class);
				in.putExtra(AppType.KEY, AppData.getKey(content));
				getActivity().startActivity(in);
			}});
	}

	/**
	 * 获取数据
	 */
	private void getDataType() {
		new DownLoadJSONThread(this.handler,
				AppData.getUrlByKey(URLDataUtil.GET_CATALOG),
				AppType.CONTENT_CATALOG).start();
		String getBooks = AppData.getUrlByKey(URLDataUtil.CONTENT_LIST_TYPE)
				+ "?type=" + LeQiType.FELLOW_ENGLISH_BOOK;
		new DownLoadJSONThread(this.handler, getBooks, AppType.CONTENT_BOOK)
				.start();

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle data = msg.getData();
		String json = data.getString(AppType.JSON);
		switch (msg.what) {
		case AppType.CONTENT_CATALOG:

			try {
				initGridView(JSONObject.fromObject(json));
				//Button button = new Button(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AppType.CONTENT_BOOK:

			try {
				initListView(JSONObject.fromObject(json));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return false;
	}

}
