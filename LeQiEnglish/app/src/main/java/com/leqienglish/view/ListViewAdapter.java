package com.leqienglish.view;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leqienglish.util.LOGGER;

import java.util.List;

import cn.duoduo.entity.Content;

public abstract class ListViewAdapter extends BaseAdapter  {
	public final static LOGGER logger = new LOGGER(ListViewAdapter.class);
	private Context context;
	private LayoutInflater layoutInflater;

	private Handler handler;
	private List<Content> items;
	private float scaleWidth;

	public ListViewAdapter(Context context, Handler handler, float scaleWidth) {
		this.context = context;
		layoutInflater = LayoutInflater.from(this.context);
		this.scaleWidth = scaleWidth;
		this.handler = handler;
	}

//	public String getKeyId(){
//		//Content content = this.getItem(this.getp)
//	}implements UpdateViewAdapterInterface
	/**
	 * 设置数据
	 * 
	 * @param items
	 */
	public void setItems(List<Content> items) {
		this.items = items;
		this.notifyDataSetChanged();
	}

	public List<Content> getItems() {
		return this.items;
	}

	/**
	 * 添加更多的数据
	 * 
	 * @param items
	 */
	public void addItems(List<Content> items) {
		
 		if (this.items == null) {
			this.setItems(items);
			return;
		} 
		//this.items.clear();
		this.items.addAll(items);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 添加更多的数据
	 * 
	 * @param items
	 */
	public void addItems(int index,List<Content> items) {
		
 		if (this.items == null) {
			this.setItems(items);
			return;
		}
		//this.items.clear();
		this.items.addAll(index,items);
		this.notifyDataSetChanged();
	}
	
	public void setAllItems(List<Content> items){
		if(this.items == null){
			this.setItems(items);
			return;
		}
		
		this.items.clear();
		this.items.addAll(items);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (this.getItems() == null) {
			return -1;
		}
		return this.getItems().size();
	}

	@Override
	public Content getItem(int position) {
		return items.get(position);
	}
	
	public Content getLastItem() {
		if(items == null || items.isEmpty()){
			return null;
		}
		return items.get(items.size()-1);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub

		return position;

	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the layoutInflater
	 */
	public LayoutInflater getLayoutInflater() {
		return layoutInflater;
	}

	/**
	 * @param layoutInflater
	 *            the layoutInflater to set
	 */
	public void setLayoutInflater(LayoutInflater layoutInflater) {
		this.layoutInflater = layoutInflater;
	}

	/**
	 * @return the handler
	 */
	public Handler getHandler() {
		return handler;
	}

	
	/**
	 * @return the scaleWidth
	 */
	public float getScaleWidth() {
		return scaleWidth;
	}

	/**
	 * @param scaleWidth the scaleWidth to set
	 */
	public void setScaleWidth(float scaleWidth) {
		this.scaleWidth = scaleWidth;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
