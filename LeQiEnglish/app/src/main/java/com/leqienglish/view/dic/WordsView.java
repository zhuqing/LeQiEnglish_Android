package com.leqienglish.view.dic;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.leqienglish.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

import cn.duoduo.entity.Content;

public class WordsView extends  LinearLayout {

	private TableLayout tableLayout;
	private List<Content> items;
	private LayoutInflater layoutInflater;

	public WordsView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public WordsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public WordsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		layoutInflater = LayoutInflater.from(getContext());
		tableLayout = (TableLayout) layoutInflater.inflate(
				R.layout.words_tableview, null);
		// TODO Auto-generated constructor stub
		this.addView(tableLayout);
	}

	public void setItems(List<Content> items) {
		this.items = items;
		this.tableLayout.removeAllViews();
		tableLayout.setStretchAllColumns(true); 
		createTableRow(items);
		//tableLayout.seton
		
	}

	private void createTableRow(List<Content> items) {
		for (Content content : items) {

			try {
				TableRow row = (TableRow) layoutInflater.inflate(
						R.layout.word_information, null);
				TextView word = (TextView) row.findViewById(R.id.word_word);
				TextView us = (TextView) row.findViewById(R.id.word_US);
				TextView en = (TextView) row.findViewById(R.id.word_EN);
				TextView desc = (TextView) row.findViewById(R.id.word_mean);

			
				word.setText(content.getTitle());
				JSONObject json = JSONObject.fromObject(content.getContent());
				JSONObject dataArr = json.getJSONObject("data");

				JSONArray array = dataArr.getJSONArray("symbols");

				for (int i = 0; i < array.size(); i++) {
					JSONObject symbol = array.getJSONObject(i);
					String usStr = symbol.getString("ph_am");
					String enStr = symbol.getString("ph_en");
					if (usStr != null&&!usStr.isEmpty()) {
						
						us.setText(Html.fromHtml("美[<font color='#0000E3'>" + usStr + "</font>]"));
					}
					if (enStr != null&&!enStr.isEmpty()) {
						en.setText(Html.fromHtml("英[<font color='#0000E3'>" + enStr + "</font>]"));
						
					}

					JSONArray parts = symbol.getJSONArray("parts");
					if (parts == null ||parts.isEmpty()) {
						continue;
					}

					StringBuffer partStr = new StringBuffer();
					for (int j = 0; j < parts.size(); j++) { 
						JSONObject part = parts.getJSONObject(j);
						partStr.append(part.get("part"));
						JSONArray means = part.getJSONArray("means");

						for (int l = 0; l < means.size(); l++) {
							partStr.append(means.get(l).toString() + ";");
						}
						partStr.append("\t");
					}

					desc.setText(partStr.toString());
					this.tableLayout.addView(row);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

}
