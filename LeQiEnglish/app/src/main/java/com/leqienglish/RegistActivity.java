package com.leqienglish;

import org.json.JSONException;
import org.json.JSONObject;

import com.leqienglish.R;
import com.leqienglish.entity.User;
import com.leqienglish.thread.url.DownLoadJSONThread;
import com.leqienglish.util.AppData;
import com.leqienglish.util.AppType;
import com.leqienglish.util.URLDataUtil;
import com.leqienglish.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistActivity extends Activity implements Handler.Callback {
	private EditText emailE;
	private EditText passwordE;
	private EditText rePasswordE;
	private RadioGroup rg;
	private Button cancel;
	private Button submit;
	private Handler handler;
	private String reg = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";

	private boolean requr = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.regist);
		this.handler = new Handler(this);
		this.init();
	}

	private void init() {
		this.emailE = (EditText) this.findViewById(R.id.username);
		this.passwordE = (EditText) this.findViewById(R.id.password);
		this.rePasswordE = (EditText) this.findViewById(R.id.repassword);
		this.rg = (RadioGroup) this.findViewById(R.id.rg);
		this.cancel = (Button) this.findViewById(R.id.cancel);
		this.submit = (Button) this.findViewById(R.id.submit);

		this.cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailE.setText("");
				passwordE.setText("");
				rePasswordE.setText("");

			}
		});

		this.submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User user = new User();
				user.setEmail(emailE.getEditableText().toString());
				user.setPassword(passwordE.getEditableText().toString());
				rg.getCheckedRadioButtonId();
				user.setSex(1);
				// user.setCreateTime(new Date().getTime()+"");
//				URLThread url = new URLThread(handler, getResources()
//						.getString(R.string.test), AppType.REGIST);
//				url.setUser(user);
//				url.start();
//				if (requr) {
//
//				}
			}
		});
		this.emailE.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					String value = emailE.getEditableText().toString();
					if (value.matches(reg)) {
						User user = new User();
						user.setEmail(value);
						DownLoadJSONThread url = new DownLoadJSONThread(handler, AppData.getUrlByKey(URLDataUtil.CHECK_EMAIL),
								AppType.CHECKEMAIL);
						
						url.start();
					} else {
						Toast.makeText(com.leqienglish.RegistActivity.this,
								"mail have problem", Toast.LENGTH_LONG).show();
						requr = false;
					}
				}
			}
		});

		this.emailE.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case AppType.REGIST:
			Bundle data = msg.getData();
			String str = data.getString("json");
			JSONObject json;
			try {
				json = new JSONObject(str);
				if (json.getString("msg").equals("ok")) {

				} else {

				}
				break;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return true;
	}
}
