package com.leqienglish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leqienglish.R;
import com.leqienglish.annotation.util.EntityAndJSON;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.DateUtil;
import com.leqienglish.util.JSONUtil;
import com.leqienglish.util.LOGGER;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.util.file.FileUtil;
import com.leqienglish.view.LeqiTextView;
import com.leqienglish.view.dic.WordsView;
import com.leqisoft.toast.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.duoduo.entity.Content;

/**
 * 普通文章阅读
 * 
 * @author guona
 * 
 */
public class ReadContentActivity extends Activity implements Handler.Callback {
	public static LOGGER logger = new LOGGER(ReadContentActivity.class);
	private Handler handler;
	private TextView titleTextView;
	private TextView fromWhereTextView;
	private LeqiTextView contentHtml;
	private RelativeLayout readContent;
	private WordsView wordsView;
	private Spanned showHtml;
	/**
	 * 创建日期
	 */
	private TextView createData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.read_content);
		this.handler = new Handler(this);
		this.init();

	}

	private void init() {
		this.titleTextView = (TextView) this.findViewById(R.id.content_title);
		this.fromWhereTextView = (TextView) this
				.findViewById(R.id.content_fromwhere);
		this.createData = (TextView) this.findViewById(R.id.content_createData);
		this.contentHtml = (LeqiTextView) this
				.findViewById(R.id.content_content);
		this.wordsView = (WordsView) this.findViewById(R.id.content_words);

		this.contentHtml.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					String select = contentHtml.getSelectText();
					logger.v("select\t" + select);
					if(select==null||select.isEmpty()){
						return false;
					}
					startTranslate(select);
				}
				return false;
			}
		});
		this.readContent = (RelativeLayout) this.findViewById(R.id.readContent);
		// this.playbarView = (PlayBarView) this.findViewById(R.id.playBarView);

		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setDisplayShowHomeEnabled(false);
		this.getActionBar().setDisplayShowTitleEnabled(true);
		this.getActionBar().setTitle(R.string.read_content_titile);

		Intent in = this.getIntent();
		String key = in.getStringExtra(AppType.KEY);
		Content content = (Content) AppData.getEntity(key);
		new DownLoadJSONThread(this.handler,
				AppData.getUrlByKey(URLDataUtil.CONTENT_INFO) + "?id="
						+ content.getId(), AppType.CONTENT_GET).start();
		
		new DownLoadJSONThread(this.handler,
				"http://www.leqienglish.com/dict_fetchWords!fetchWords.do" + "?id="
						+ content.getId(), AppType.DIC_GET).start();

	}
	
	private void startTranslate(String source){
		String url = this.getApplicationContext().getString(R.string.translate_url);
		url+="&from=en&to=zh&q="+source;
		new DownLoadJSONThread(this.handler,
				url, AppType.TRANSPLATE_GET).start();
	}

	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		Bundle data = message.getData();
		switch (message.what) {
		case AppType.CONTENT_GET:
			String jsonStr = data.getString("json");
			try {
				
				Content content = (Content) EntityAndJSON.json2Object(jsonStr);
				this.titleTextView.setText(content.getTitle());
				this.fromWhereTextView.setText("乐琪英语");
				
				
				this.createData.setText(DateUtil.toDataStr(content.getCreateTime(), this.getApplicationContext().getString(R.string.createDataFormat)));
				new DrawableText(content.getContent()).start();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AppType.DIC_GET:
			String wordsJson = data.getString("json");
			JSONObject json = JSONObject.fromObject(wordsJson);
			List<Content> words = null;
			try {
				words = this.createLearnEList(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.wordsView.setItems(words);
			break;
		case AppType.TRANSPLATE_GET:
			String translate = JSONUtil.getTransPlate(data.getString("json"));
			ToastUtil.showTop(this.getApplicationContext(), translate, Toast.LENGTH_LONG);
			break;
		case AppType.URL_ERROR:
			break;
		case AppType.SHOW_CONTENT:
			this.contentHtml.setText(this.showHtml);
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

	/**
	 * 加载图片，图片缓存未做
	 * 
	 * @author guona
	 * 
	 */
	class DrawableText extends Thread {
		private String html;

		public DrawableText(String html) {
			this.html = html;
		}

		public void run() {
			showHtml = Html.fromHtml(html, new Html.ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					logger.v("image source="+source);
					String imagePath = AppData.getImagePathName(source);
					int width = 800;
					if (readContent.getWidth() > 0) {
						width = readContent.getWidth();
					}
					if(imagePath!=null){
						logger.v("imagePath imagePath="+imagePath);
						Drawable drawable = BitmapDrawable.createFromPath(imagePath);
						drawable.setBounds(0,
								0,
								width,
								(int) (drawable.getIntrinsicHeight() * (width * 1.0 / drawable
										.getIntrinsicWidth())));
						return drawable;
					}
					InputStream is = null;
					try {
						
						is = (InputStream) new URL(source).getContent();
						Drawable d = Drawable.createFromStream(is, "src");
						
						d.setBounds(
								0,
								0,
								width,
								(int) (d.getIntrinsicHeight() * (width * 1.0 / d
										.getIntrinsicWidth())));
						is.close();
						new SaveImageThread(d,source).start();
						return d;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			}, null);
			handler.sendEmptyMessage(AppType.SHOW_CONTENT);
		}

	}

	/**
	 * 缓存网络图片
	 * 
	 * @author guona
	 * 
	 */
	class SaveImageThread extends Thread {
		private Drawable drawable;
		/**
		 * 图片源
		 */
		private String source;

		public SaveImageThread(Drawable drawable, String source) {
			this.drawable = drawable;
			this.source = source;
		}

		public void run() {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			String fileEnd = FileUtil.getFileEnd(source);
			CompressFormat compressFormat = this.getCompressFormat(fileEnd);
			if (compressFormat == null) {
				return;
			}
			String fileName = FileUtil.getFileName(source);
			File file = new File(AppData.getImageFileDir(), fileName);
			if (file.exists()) {
				return;
			}
			try {
				FileOutputStream out = new FileOutputStream(file);
				bitmap.compress(compressFormat, 90, out);
				out.flush();
				out.close();
				AppData.addImageName(fileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		/**
		 * 获取压缩格式
		 * 
		 * @param fileEnd
		 * @return
		 */
		private CompressFormat getCompressFormat(String fileEnd) {
			if (fileEnd.equals("JPG") || fileEnd.equals("jpg")
					|| fileEnd.equals("jpeg")) {
				return CompressFormat.JPEG;
			}

			if (fileEnd.equals("PNG") || fileEnd.equals("png")) {
				return CompressFormat.PNG;
			}

			return null;
		}
	}

}
