package com.leqienglish.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.BookActivity;
import com.leqienglish.ReadContentActivity;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.LeQiType;
import com.leqienglish.view.ListViewAdapter;
import com.leqienglish.view.bottombar.BottomBar;

import cn.duoduo.entity.Content;

public class FirstPageAdapterListView extends ListViewAdapter {

	public FirstPageAdapterListView(Context context, Handler handler,int scaleWidth) {
		super(context, handler,scaleWidth);
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Content content = this.getItem(position);
		if (content == null) {
			return null;
		}
		if (convertView == null) {
			convertView = this.createItemView(content);
		}
		FirstPageView itemView = (FirstPageView) convertView.getTag();
		itemView.title.setText(content.getTitle());
		itemView.description.setText(content.getContent());
		itemView.setContent(content);
		itemView.buttomBar.updateContent(content);
		AppData.setImageView(this.getHandler(), content, itemView.imageView,this.getScaleWidth());
		return convertView;

	}

	/**
	 * 创建ListItem
	 * 
	 * @param content
	 * @return
	 */
	private View createItemView(Content content) {
		View view = this.getLayoutInflater().inflate(R.layout.first_page_item,
				null);
		FirstPageView firstPageViewItem = new FirstPageView();
		firstPageViewItem.catalogText = (TextView) view
				.findViewById(R.id.first_page_catalog_textview);
		firstPageViewItem.description = (TextView) view
				.findViewById(R.id.first_page_description_textview);
		firstPageViewItem.title = (TextView) view
				.findViewById(R.id.first_page_title_text);
		firstPageViewItem.imageView = (ImageView) view
				.findViewById(R.id.first_page_imageview);
		firstPageViewItem.openInfo = (TextView) view.findViewById(R.id.first_page_open_info_button);
		firstPageViewItem.buttomBar = (BottomBar) view.findViewById(R.id.fist_page_bottombar);
		

		view.setTag(firstPageViewItem);
		
		firstPageViewItem.openInfo.setOnClickListener(new Turn2ClickListner(
				firstPageViewItem));
		firstPageViewItem.imageView.setOnClickListener(new Turn2ClickListner(
				firstPageViewItem));
		firstPageViewItem.title.setOnClickListener(new Turn2ClickListner(
				firstPageViewItem));
		return view;
	}

	/**
	 * 点击事件监听
	 * 
	 * @author guona
	 * 
	 */
	private class Turn2ClickListner implements OnClickListener {
		private FirstPageView firstPageView;

		public Turn2ClickListner(FirstPageView firstPageView) {
			this.firstPageView = firstPageView;
		}

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			if(firstPageView.getContent()==null){
				return;
			}
			Intent in = new Intent();
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (firstPageView.getContent().getContenttype() == LeQiType.WenZhang) {
				in.setClass(getContext().getApplicationContext(),
						ReadContentActivity.class);
			} else if (firstPageView.getContent().getContenttype() == LeQiType.FELLOW_ENGLISH_BOOK) {
				in.setClass(getContext().getApplicationContext(),
						BookActivity.class);
			}

			in.putExtra(AppType.KEY, AppData.getKey(firstPageView.getContent()));
			getContext().startActivity(in);
		}

	}

	/**
	 * 实体对象的存储
	 * 
	 * @author guona
	 * 
	 */
	private class FirstPageView {
		/**
		 * 标题
		 */
		private TextView title;
		/**
		 * 
		 */
		private BottomBar buttomBar;

 
		/**
		 * 简单描述
		 */
		private TextView description;
		/**
		 * 图片
		 */
		private ImageView imageView;
		private TextView openInfo;
		/**
		 * @return the openInfo
		 */
		public TextView getOpenInfo() {
			return openInfo;
		}

		/**
		 * @param openInfo the openInfo to set
		 */
		public void setOpenInfo(TextView openInfo) {
			this.openInfo = openInfo;
		}

		/**
		 * 分类
		 */
		private TextView catalogText;

		private Content content;
		

		/**
		 * @return the buttomBar
		 */
		public BottomBar getButtomBar() {
			return buttomBar;
		}

		/**
		 * @param buttomBar the buttomBar to set
		 */
		public void setButtomBar(BottomBar buttomBar) {
			this.buttomBar = buttomBar;
		}

		/**
		 * @return the content
		 */
		public Content getContent() {
			return content;
		}

		/**
		 * @param content
		 *            the content to set
		 */
		public void setContent(Content content) {
			this.content = content;
		}

		/**
		 * @return the title
		 */
		public TextView getTitle() {
			return title;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setTitle(TextView title) {
			this.title = title;
		}

		/**
		 * @return the description
		 */
		public TextView getDescription() {
			return description;
		}

		/**
		 * @param description
		 *            the description to set
		 */
		public void setDescription(TextView description) {
			this.description = description;
		}

		/**
		 * @return the imageView
		 */
		public ImageView getImageView() {
			return imageView;
		}

		/**
		 * @param imageView
		 *            the imageView to set
		 */
		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}

		/**
		 * @return the catalogText
		 */
		public TextView getCatalogText() {
			return catalogText;
		}

		/**
		 * @param catalogText
		 *            the catalogText to set
		 */
		public void setCatalogText(TextView catalogText) {
			this.catalogText = catalogText;
		}

	}
}
