/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  RefreshRecyclerView
 * Created by  ianchang on 2018-06-01 18:03:51
 * Last modify date   2016-08-31 11:34:13
 */

package com.ian.widget.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ian.widget.R;

import java.util.ArrayList;

public class RefreshRecyclerView extends RecyclerView {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private RefreshWrapAdapter refreshWrapAdapter;
    private View headerView, footerView;

    private int mState = STATE_NORMAL;
    private int headerViewHeight;
    private TextView mStatusText;
    private boolean isOnTouching, isRefresh, isLoadMore;
    private int lastX, lastY;
    private boolean isRefreshEnable = true, isLoadMoreEnable = true;

    private OnRefreshViewListener onRefreshViewListener;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isRefreshEnable){
            moveHeader(e);
        }
        return super.onTouchEvent(e);
    }

    // 滑动 header
    private void moveHeader(MotionEvent e){
        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOnTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否滑动到了头部
                if (!canScrollVertically(-1)) {
                    int dy = lastY - y;
                    int dx = lastX - x;

                    if (Math.abs(dy) > Math.abs(dx)) {
                        isRefresh = true;
                        changeHeight(dy);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isRefresh = false;
                isOnTouching = false;
                if (mState == STATE_READY) {
                    onStatusChange(STATE_REFRESHING);
                }
                autoSize();
                break;
        }

        lastX = x;
        lastY = y;

    }

    private void changeHeight(int dy) {
        headerView.getLayoutParams().height -= dy;
        headerView.requestLayout();
        setStateByHeight(headerView.getHeight(), false);
    }

    public void autoSize() {
        int currentHeight = headerView.getHeight();

        int targetHeight = headerViewHeight;
        if (mState == STATE_READY || mState == STATE_REFRESHING) {
            targetHeight = headerViewHeight * 2;
        }

        if (mState == STATE_REFRESHING) {
            if (currentHeight < headerViewHeight * 2) {
                return;
            }
        }

        ValueAnimator objectAnimator = ValueAnimator.ofInt(currentHeight, targetHeight);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animatedValue = (int) animation.getAnimatedValue();
                setStateByHeight(animatedValue, true);
                headerView.getLayoutParams().height = animatedValue;
                headerView.requestLayout();
            }
        });
        objectAnimator.start();
    }

    @Override
    public void setLayoutManager(final LayoutManager layout) {
        super.setLayoutManager(layout);

        this.addOnScrollListener(mOnScrollListener);
    }


    public OnRefreshViewListener getOnRefreshViewListener() {
        return onRefreshViewListener;
    }

    public void setOnRefreshViewListener(OnRefreshViewListener onRefreshViewListener) {
        this.onRefreshViewListener = onRefreshViewListener;
    }


    /*******
     * 变更状态
     * @param height
     * @param isAuto
     */
    private void setStateByHeight(int height, boolean isAuto) {
        if (mState == STATE_REFRESHING) {
            return;
        }
        if (height - headerViewHeight < headerViewHeight) {
            onStatusChange(STATE_NORMAL);
        } else if (height - headerViewHeight > headerViewHeight) {
            onStatusChange(STATE_READY);
        } else if (height - headerViewHeight == headerViewHeight && !isOnTouching && !isAuto) {
            onStatusChange(STATE_REFRESHING);
        }
    }


    public void onStatusChange(int status) {
        mState = status;
        switch (status) {
            case STATE_READY:
                mStatusText.setText("松开刷新...");
                break;
            case STATE_NORMAL:
                mStatusText.setText("下拉刷新...");
                break;
            case STATE_REFRESHING:
                mStatusText.setText("正在刷新...");
                if (onRefreshViewListener != null) {
                    onRefreshViewListener.onRefresh();
                }
                break;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        ArrayList<View> headers = new ArrayList<>();
        ArrayList<View> footers = new ArrayList<>();

        // 是否增加下拉刷新
        if (isRefreshEnable) {

            View refreshView = LayoutInflater.from(getContext()).inflate(R.layout.layout_header, null);
            headerView = refreshView;

            RelativeLayout headerLayout = new RelativeLayout(getContext());
            headerLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            headerLayout.addView(headerView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            headerLayout.setGravity(Gravity.BOTTOM);

            mStatusText = (TextView) refreshView.findViewById(R.id.item_text);
            headerView.post(new Runnable() {
                @Override
                public void run() {
                    headerViewHeight = headerView.getHeight();
                    RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) headerView.getLayoutParams();
                    l.setMargins(0, -headerViewHeight, 0, 0);
                    headerView.requestLayout();
                }
            });
            headers.add(headerLayout);
        }


        // 是否增加上拉加载更多...
        if (isLoadMoreEnable) {
            LinearLayout footerLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_footer, null, false);
            footerLayout.setGravity(Gravity.CENTER);
            footerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            footers.add(footerLayout);

            footerView = footerLayout;
            footerView.setVisibility(GONE);
        }


        refreshWrapAdapter = new RefreshWrapAdapter(adapter, headers, footers);
        super.setAdapter(refreshWrapAdapter);
    }

    public interface OnRefreshViewListener {
        void onRefresh();

        void onLoadMore();
    }

    public void setLoadMoreComplete() {
        footerView.setVisibility(GONE);
        isLoadMore = false;
    }

    public void setRefreshComplete() {
        onStatusChange(STATE_NORMAL);
        autoSize();
    }

    public void setRefreshEnable(boolean refresh) {
        isRefreshEnable = refresh;
        refreshWrapAdapter.notifyDataSetChanged();
    }

    public void setLoadMoreEnable(boolean loadMore) {
        isLoadMoreEnable = loadMore;
    }

    // 上拉加载更多...
    private RecyclerView.OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (isRefresh || !isLoadMoreEnable) {
                return;
            }

            if (mState != STATE_NORMAL) {
                return;
            }
            //判断是否最后一item个显示出来
            LayoutManager layoutManager = getLayoutManager();

            //可见的item个数
            int visibleChildCount = layoutManager.getChildCount();
            if (visibleChildCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore) {
                View lastVisibleView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                if (lastVisiblePosition >= layoutManager.getItemCount() - 1) {
                    footerView.setVisibility(VISIBLE);
                    isLoadMore = true;
                    if (onRefreshViewListener != null) {
                        onRefreshViewListener.onLoadMore();
                    }
                } else {
                    footerView.setVisibility(GONE);
                }
            }

        }
    };
}
