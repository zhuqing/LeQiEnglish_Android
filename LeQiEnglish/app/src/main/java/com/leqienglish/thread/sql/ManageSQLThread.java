package com.leqienglish.thread.sql;

import android.content.Context;
import android.os.Handler;

import com.leqienglish.database.ExecuteSQL;
import com.leqienglish.database.SQLData;
import com.leqienglish.entity.DataBaseOperateEnum;

import cn.duoduo.entity.Content;

public class ManageSQLThread extends Thread {
	private SQLData sqlData;
	private Content learnE;
	private DataBaseOperateEnum type;
	private ExecuteSQL sqlExcute;
	private Handler handler;

	public ManageSQLThread(Context context,Handler handler, Content learnE,
			DataBaseOperateEnum type) {
		this.sqlData = new SQLData(context);
		this.learnE = learnE;
		this.type = type;
		this.handler = handler;
	}

	public void run() {
		this.sqlExcute = new ExecuteSQL(this.sqlData);
		switch (type) {
		case INSERT:
			//this.sqlExcute.insertLearnE(learnE);
			break;
		case UPDATE:
			//this.sqlExcute.updateLearnE(learnE);
			break;
		case DELETE:  
		//	this.sqlExcute.deleteLearnE(learnE.getId().toString());
			break;
		case SEARCH:
//			ArrayList<Content> list = this.sqlExcute.getLearnE(1, 15);
//			Bundle data = new Bundle();
//			data.putParcelableArrayList(AppType.DATALIST, list);
//			Message mess = new Message();
//			mess.setData(data);
//			mess.what = AppType.SELECT;
//			this.handler.sendMessage(mess);
			break;
		default:

		}
	}

}
