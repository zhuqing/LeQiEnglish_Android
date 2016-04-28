package com.leqienglish.view.book;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.duoduo.entity.Content;

import com.leqienglish.R;
import com.leqienglish.util.AppData;
import com.leqienglish.view.ListViewAdapter;
import com.leqienglish.view.inter.UpdateViewItemInterface;


public class BookListViewAdapter extends ListViewAdapter {

	public BookListViewAdapter(Context context, Handler handler ,float scaleWidth) {
		super(context, handler,scaleWidth);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Content object = this.getItems().get(position);
		ListItemView listView = new ListItemView();

		if (convertView == null) {
			convertView = CreateView(object);
		}
		listView = (ListItemView) convertView.getTag();

		listView.title.setText(object.getTitle());

		AppData.setImageView(this.getHandler(), object, listView.imageView,this.getScaleWidth());
		// this.handler.postAtTime(new runTest(), 1000);
		return convertView;
	}

	private View CreateView(Content content) {
		View convertView = null;
		ListItemView listView = new ListItemView();

		convertView = this.getLayoutInflater().inflate(R.layout.book_item, null);
		listView.imageView = (ImageView) convertView
				.findViewById(R.id.content_image_imageView);
		listView.title = (TextView) convertView
				.findViewById(R.id.content_image_title);
		listView.catalogText = (TextView) convertView
				.findViewById(R.id.content_image_catalogtext);
		listView.content = content;
		convertView.setTag(listView);

		return convertView;
	}

	public final class ListItemView {
		private TextView title;
		private TextView catalogText;
		private ImageView imageView;
		private Content content;
	
	}

}
