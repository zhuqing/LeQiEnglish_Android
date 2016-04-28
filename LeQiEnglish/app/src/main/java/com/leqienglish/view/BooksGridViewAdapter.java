package com.leqienglish.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqisoft.inter.Operator;


import java.util.List;

import cn.duoduo.entity.Catalog;

public class BooksGridViewAdapter<T extends Catalog> extends BaseAdapter {

	private List<T> items;
	private Context context;
	private LayoutInflater layoutInflater;
	/**
	 * 点击事件后执行操作的回调
	 */
	private Operator<T> operator;
	/**
	 * @return the operator
	 */
	public Operator<T> getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator<T> operator) {
		this.operator = operator;
	}

	/**
	 * 当前点击后的Button
	 */
	private View selectedButton;
	
	public BooksGridViewAdapter(Context context, List<T> items){
		this.context = context;
		this.items = items;
		this.layoutInflater = LayoutInflater.from(this.context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.items.size();
	}

	@Override
	public T getItem(int index) {
		// TODO Auto-generated method stub
		if(this.items.size()<=index||index<0){
			return null;
		}
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		if(this.getItem(index)==null){
			return -1;
		}else{
			return this.getItem(index).getId();
		}
		
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		T catalog = this.getItem(position);
		GridViewItem item = null;
		if(convertView==null){
			item = new GridViewItem();
			convertView = layoutInflater.inflate(R.layout.books_type_item, null);
			
			item.textView = (TextView) convertView.findViewById(R.id.books_type_item_textview);
			
			item.textView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(selectedButton!=null){
						selectedButton.setBackgroundDrawable(null);
					}
					v.setBackgroundResource(R.drawable.gridview_item_selected);
					selectedButton = v;
					if(operator==null){  
						return;
					}
					operator.operator((T) (v.getTag(R.id.selected_button_gridView)));
				}});
			convertView.setTag(item);
		}else{
			item = (GridViewItem) convertView.getTag();
		}
		if(catalog==null||catalog.getName()==null){
			item.textView.setText("null");
		}else{
			item.textView.setText(catalog.getName());
		}
		//item.button.getTag();
		item.textView.setTag(R.id.selected_button_gridView,catalog);
	
		
		return convertView;
	}
	
	public final class GridViewItem {
		public TextView textView;
		public Button button;
	}

}
