package com.leqienglish.view.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leqienglish.R;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.file.LearnEnglishUtil;
import com.leqienglish.util.number.DecimalUtil;
import com.leqienglish.view.ViewItem;
import com.leqienglish.view.util.DownloadManager;
import com.leqienglish.view.util.DownloadManager.DownloadInfo;

import java.util.List;

import cn.duoduo.entity.Content;

public class LessonListViewAdapter extends BaseAdapter {
	public LOGGER logger = new LOGGER(LessonListViewAdapter.class);
	private List<Content> items;
	private Context context;
	private LayoutInflater layoutInflater;
	private DownloadManager downloadManager;

	// private static Map<String, ImageView> downloadingMap = new
	// HashMap<String, ImageView>();

	public LessonListViewAdapter(Context context, List<Content> items) {
		this.context = context;
		this.items = items;
		this.layoutInflater = LayoutInflater.from(this.context);
		downloadManager = DownloadManager.getInstance();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.items.size();
	}

	@Override
	public Content getItem(int position) {
		if (position < 0 || position >= this.items.size()) {
			return null;
		}
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Content content = this.getItem(position);
		if (content == null) {
			return -1;
		}
		return content.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Content content = this.getItem(position);
		LessonListViewItem listViewItem = null;
		if (convertView == null) {
			convertView = createView(content);
		}

		listViewItem = (LessonListViewItem) convertView.getTag();

		if (content != null) {
			String title = content.getTitle() == null ? "test" : content
					.getTitle();
			logger.d("【title=" + title + "】");
			listViewItem.textView.setText(title);

		} else {
			listViewItem.textView.setText("");
			listViewItem.downLoadImageView.setImageDrawable(null);
			listViewItem.showImageView.setImageDrawable(null);
		}

		return convertView;
	}

	/**
	 * 创建ListItem
	 * 
	 * @param content
	 * @return
	 */
	private View createView(Content content) {
		View convertView = this.layoutInflater.inflate(R.layout.lesson_item,
				null);
		final LessonListViewItem listViewItem = new LessonListViewItem();
		listViewItem.setContent(content);
		listViewItem.textView = (TextView) convertView
				.findViewById(R.id.lesson_title);
		listViewItem.downLoadImageView = (ImageView) convertView
				.findViewById(R.id.lesson_download_image);
		listViewItem.downloadBox = (LinearLayout) convertView
				.findViewById(R.id.lesson_download_box);
		if (!LearnEnglishUtil.hasDownloadFile(listViewItem.getKeyId())) {
			listViewItem.downLoadImageView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							listViewItem.downloadBox.removeAllViews();
							listViewItem.downloadText = new TextView(context);
							listViewItem.downloadText.setText("等待中...");
							listViewItem.downloadBox
									.addView(listViewItem.downloadText);
							downloadManager.startDownLoad(listViewItem);

						}
					});
		}else{
			//Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			listViewItem.downLoadImageView.setImageResource(R.drawable.next_activity);//setBackgroundResource(R.drawable.next_activity);
		}

		// 判断是否处于下载状态
		if (this.downloadManager.isDownloading(listViewItem)) {
			DownloadInfo downloadInfo = this.downloadManager
					.getDataDownInfo(listViewItem);
			if (downloadInfo == null) {
				startDownLoad(listViewItem);
			} else {
				changeToDownload(listViewItem);
				listViewItem.downloadUpdate(listViewItem.getKeyId(),
						downloadInfo.getHasDownloadSize(), -1L);
			}
		}
		convertView.setTag(listViewItem);
	
		return convertView;
	}

	/**
	 * 启动下载
	 * 
	 * @param listViewItem
	 */
	private void startDownLoad(LessonListViewItem listViewItem) {
		changeToDownload(listViewItem);
		listViewItem.downloadText.setText("等待中...");

	}

	/**
	 * 转换到下载模式
	 * 
	 * @param listViewItem
	 */
	private void changeToDownload(LessonListViewItem listViewItem) {
		listViewItem.downloadBox.removeAllViews();
		listViewItem.downloadText = new TextView(context);
		listViewItem.downloadBox.addView(listViewItem.downloadText);
	}

	public final class LessonListViewItem extends ViewItem {
		private ImageView showImageView;
		private TextView textView;
		private ImageView downLoadImageView;
		private LinearLayout downloadBox;
		private TextView downloadText;

		@Override
		public void updateDownLoad(long currentSize, Long totalSize) {
			// TODO Auto-generated method stub
			if (this.downloadText == null) {
				return;
			}
			double hasFinshed = currentSize * 1.0 / totalSize;
			this.downloadText.setText("已下载"
					+ DecimalUtil.getPercent(hasFinshed));

		}

		@Override
		public void downloadOver() {
			// TODO Auto-generated method stub
			if (this.downloadText == null) {
				return;
			}

			this.downloadText.setText("下载结束");

		}

	}

}
