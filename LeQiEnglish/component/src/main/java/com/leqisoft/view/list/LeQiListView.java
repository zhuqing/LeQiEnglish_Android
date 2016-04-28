package com.leqisoft.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.leqisoft.inter.Callback;
import com.leqisoft.util.log.LOGGER;

public class LeQiListView extends ListView implements OnScrollListener {
	public static final LOGGER logger = new LOGGER(LeQiListView.class);
	private View footerView;
	private View headerView;
	private int footerViewHeight;
	private int headerViewHeight;
	private final int maxHeight = 400;
	/**
	 * 屏幕显示在第一个的item的索引
	 */
	private int firstVisibleItemPosition;
	/**
	 * 最后一个Item的索引
	 */
	private int lastVisibleItemPosition;




	private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
	private final int RELEASE_REFRESH = 1; // 松开刷新
	private final int REFRESHING = 2; // 正在刷新中
	private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态
	/**
	 * 触屏时的Y坐标
	 */
	private float downY;

	private OnRefreshListener refershListener;
	private boolean isScrollToBottom; // 是否滑动到底部
	private boolean isLoadingMore = false; // 是否正在加载更多中

	private Callback<LeQiListView,View> headerViewCallback;
	private Callback<LeQiListView,View> footViewCallback;


	public LeQiListView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public LeQiListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LeQiListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.initHeaderView();
		this.initFootView();
		this.setOnScrollListener(this);  
	}

	/**
	 * 当滚动时调用
	 * 
	 * @param firstVisibleItem
	 *            当前屏幕显示在顶部的item的position
	 * @param visibleItemCount
	 *            当前屏幕显示了多少个条目的总数
	 * @param totalItemCount
	 *            ListView的总条目的总数
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstVisibleItemPosition = firstVisibleItem;
		// 剩下不到十个时开始更新
		if (totalItemCount - this.getLastVisiblePosition() == 1) {
			this.isScrollToBottom = true;
		}else{
			this.isScrollToBottom = false;
		}

	}

	/**
	 * 初始化顶部View
	 */
	private void initHeaderView() {
		if(this.getHeaderView()==null){
			return ;
		}

		this.getHeaderView().measure(0, 0);
		this.headerViewHeight = this.getHeaderView().getMeasuredHeight();
		//maxHeight = headerViewHeight*2;
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		this.addHeaderView(this.getHeaderView());

	}
	
	/**
	 * 初始化顶部View
	 */
	private void initFootView() {
		if(this.getFooterView() == null){
			return ;
		}
		footerView.measure(0, 0);
		this.footerViewHeight = footerView.getMeasuredHeight();
		//maxHeight = headerViewHeight*2;
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.addFooterView(footerView);

	}

	private float newY = 0.0f;
	private float moveHeight = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moveHeight = 0;
			//this.newY = ev.getY();
			//this.downY = ev.getY();
			logger.v( "headerViewHeight=\t"+headerViewHeight);
			break;
		case MotionEvent.ACTION_MOVE:
			if(newY == 0){
				newY = ev.getY();
				break;
			}
			float movey = ev.getY() - this.newY;
			
			newY = ev.getY();
			
			if(moveHeight<maxHeight){
				moveHeight += movey * (1 - moveHeight / maxHeight);
			}
			if(this.firstVisibleItemPosition == 0){
				headerView.setPadding(0, (int) (-headerViewHeight + moveHeight), 0,
						0);
				currentState = RELEASE_REFRESH;
				break;
			}
		
			



			break;
		case MotionEvent.ACTION_UP:
			moveHeight = 0;
			this.newY = 0;
			// 判断当前的状态是松开刷新还是下拉刷新
			if (currentState == RELEASE_REFRESH) {
				logger.v("刷新数据.");
				// 把头布局设置为完全显示状态
				headerView.setPadding(0, 0, 0, 0);
				// 进入到正在刷新中状态
				currentState = REFRESHING;
				refreshHeaderView();

				if ( this.getRefershListener()!= null) {
					getRefershListener().onLoadingNew(); // 调用使用者的监听方法
				}
			} else if (currentState == DOWN_PULL_REFRESH) {
				// 隐藏头布局
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);

	}
	
	/**
	 * 获取更多数据结束
	 */
	public void addMoreOver(){
		hideFooterView();
	}
	
	/**
	 * 获取最新数据结束
	 */
	public void addNewOver(){
		
	}

	/**
	 * 根据currentState刷新头布局的状态
	 */
	private void refreshHeaderView() {
		
		switch (currentState) {
		case DOWN_PULL_REFRESH: // 下拉刷新状态
			// tvState.setText("下拉刷新");
			// ivArrow.startAnimation(downAnimation); // 执行向下旋转
			break;
		case RELEASE_REFRESH: // 松开刷新状态
			// tvState.setText("松开刷新");
			// ivArrow.startAnimation(upAnimation); // 执行向上旋转
			break;
		case REFRESHING: // 正在刷新中状态
			// ivArrow.clearAnimation();
			// ivArrow.setVisibility(View.GONE);
			// mProgressBar.setVisibility(View.VISIBLE);
			// tvState.setText("正在刷新中...");
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			// 判断当前是否已经到了底部
			if (isScrollToBottom && !isLoadingMore) {
				isLoadingMore = true;
				// 当前到底部
				logger.v("加载更多数据");
				footerView.setPadding(0, 0, 0, 0);
				this.setSelection(this.getCount());

				if (this.getRefershListener() != null) {
					getRefershListener().onLoadingMore();
				}
			}
		}
	}



	/**
	 * 隐藏头布局
	 */
	public void hideHeaderView() {
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		currentState = DOWN_PULL_REFRESH;
	}

	/**
	 * 隐藏脚布局
	 */
	public void hideFooterView() {
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		isLoadingMore = false;
	}

	public View getFooterView() {
		if(this.footerView == null&&this.getFootViewCallback()!=null){
			this.footerView = this.getFootViewCallback().call(this);
		}
		return footerView;
	}

	public void setFooterView(View footerView) {
		this.footerView = footerView;
	}

	public View getHeaderView() {
		if(this.headerView == null&&this.getHeaderViewCallback()!=null){
			this.headerView = this.getHeaderViewCallback().call(this);
		}
		return headerView;
	}

	public void setHeaderView(View headerView) {
		this.headerView = headerView;
	}

	public int getFooterViewHeight() {
		return footerViewHeight;
	}

	public void setFooterViewHeight(int footerViewHeight) {
		this.footerViewHeight = footerViewHeight;
	}

	public int getHeaderViewHeight() {
		return headerViewHeight;
	}

	public void setHeaderViewHeight(int headerViewHeight) {
		this.headerViewHeight = headerViewHeight;
	}


	public int getFirstVisibleItemPosition() {
		return firstVisibleItemPosition;
	}

	public void setFirstVisibleItemPosition(int firstVisibleItemPosition) {
		this.firstVisibleItemPosition = firstVisibleItemPosition;
	}

	public int getLastVisibleItemPosition() {
		return lastVisibleItemPosition;
	}

	public void setLastVisibleItemPosition(int lastVisibleItemPosition) {
		this.lastVisibleItemPosition = lastVisibleItemPosition;
	}



	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public float getDownY() {
		return downY;
	}

	public void setDownY(float downY) {
		this.downY = downY;
	}

	public OnRefreshListener getRefershListener() {
		return refershListener;
	}

	public void setRefershListener(OnRefreshListener refershListener) {
		this.refershListener = refershListener;
	}


	public Callback<LeQiListView, View> getFootViewCallback() {
		return footViewCallback;
	}

	public void setFootViewCallback(Callback<LeQiListView, View> footViewCallback) {
		this.footViewCallback = footViewCallback;
	}


	public Callback<LeQiListView, View> getHeaderViewCallback() {
		return headerViewCallback;
	}

	public void setHeaderViewCallback(Callback<LeQiListView, View> headerViewCallback) {
		this.headerViewCallback = headerViewCallback;
	}
}
