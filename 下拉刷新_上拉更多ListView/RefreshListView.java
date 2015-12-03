package com.itlowly.gdpulib.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新_上拉更多的listview
 * 
 * @author itlowly
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		OnItemClickListener {
	// 下拉刷新3个状态
	private static final int STATE_PULL_REFRESH = 0;
	private static final int STATE_RELEASE_REFRESH = 1;
	private static final int STATE_REFRESHING = 2;

	private int mCurrenState = STATE_PULL_REFRESH;

	private int startY = -1;
	private int dY;
	private View headerView;
	private int mHeaderViewHeigth;
	private TextView tv_title;
	private TextView tv_refresh_date;
	private ImageView iv_arrow;
	private ProgressBar pb_process;
	private RotateAnimation animationUp;
	private RotateAnimation animationDown;

	public RefreshListView(Context context) {
		super(context);
		initView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initFooterView();

	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		initFooterView();
	}

	public void initView() {
		headerView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(headerView);

		headerView.measure(0, 0);

		mHeaderViewHeigth = headerView.getMeasuredHeight();

		headerView.setPadding(0, -mHeaderViewHeigth, 0, 0);

		tv_title = (TextView) headerView.findViewById(R.id.tv_title);
		tv_refresh_date = (TextView) headerView
				.findViewById(R.id.tv_refresh_date);

		iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pb_process = (ProgressBar) headerView.findViewById(R.id.pb_process);

		tv_refresh_date.setText("未刷新");

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			if (startY == -1) {
				startY = (int) ev.getRawY();
			}

			if (mCurrenState == STATE_REFRESHING) {
				break;
			}

			dY = (int) (ev.getRawY() - startY);

			if (dY > 0 && getFirstVisiblePosition() == 0) {
				int padding = dY - mHeaderViewHeigth;

				headerView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mCurrenState != STATE_RELEASE_REFRESH) {
					mCurrenState = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (padding <= 0 && mCurrenState != STATE_PULL_REFRESH) {
					mCurrenState = STATE_PULL_REFRESH;
					refreshState();
				}

				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;

			if (mCurrenState == STATE_RELEASE_REFRESH) {
				mCurrenState = STATE_REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
			} else if (mCurrenState == STATE_PULL_REFRESH) {
				headerView.setPadding(0, -mHeaderViewHeigth, 0, 0);

			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		initArrowAnim();
		switch (mCurrenState) {
		case STATE_PULL_REFRESH:
			tv_title.setText("下拉刷新");
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.VISIBLE);
			pb_process.setVisibility(View.INVISIBLE);

			iv_arrow.setAnimation(animationDown);
			break;

		case STATE_RELEASE_REFRESH:
			tv_title.setText("松开刷新");
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.VISIBLE);
			iv_arrow.setAnimation(animationUp);
			pb_process.setVisibility(View.INVISIBLE);

			break;

		case STATE_REFRESHING:
			tv_title.setText("正在刷新");
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.INVISIBLE);
			pb_process.setVisibility(View.VISIBLE);

			if (mListener != null) {
				mListener.onRefresh();
			}

			break;

		default:
			break;
		}
	}

	private void initArrowAnim() {
		animationUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationUp.setDuration(200);
		animationUp.setFillAfter(true);

		animationDown = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationDown.setDuration(200);
		animationDown.setFillAfter(true);

	}

	OnRefreshListener mListener;
	private View mFootView;
	private int mFootHeight;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public void onRefreshLoadMoreCompleted() {
			mFootView.setPadding(0, -mFootHeight, 0, 0);
			isLoadMore = false;
	}

	public void onRefreshCompleted(Boolean success) {
		if (isLoadMore) {// 表示加载更多

		} else {// 表示下拉刷新
			mCurrenState = STATE_PULL_REFRESH;
			refreshState();
			iv_arrow.setVisibility(View.VISIBLE);
			headerView.setPadding(0, -mHeaderViewHeigth, 0, 0);

			if (success) {
				tv_refresh_date.setText("最后刷新时间:" + getCurrenTime());
			}
		}
	}

	/**
	* 自定义接口，刷新完毕监听器
	**/
	public interface OnRefreshListener {
		public void onRefresh();
		public void onLoadMore();
	}

	public String getCurrenTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFootView = View.inflate(getContext(), R.layout.refresh_foot, null);
		this.addFooterView(mFootView);

		mFootView.measure(0, 0);

		mFootHeight = mFootView.getMeasuredHeight();

		mFootView.setPadding(0, -mFootHeight, 0, 0);

		this.setOnScrollListener(this);
	}

	private boolean isLoadMore = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) {
				mFootView.setPadding(0, 0, 0, 0);

				// System.out.println("----------->到底了!!!!");

				setSelection(getCount() - 1);

				isLoadMore = true;

				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	OnItemClickListener mItemClickListener;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);

		mItemClickListener = listener;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position - 2, id);
		}
	}

}
