/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ScrollViewActivity
 * Created by  ianchang on 2018-05-04 13:45:52
 * Last modify date   2018-05-04 13:45:52
 */

package com.ian.widget;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ian.widget.view.PullToRefreshLayout;

public class ScrollViewActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);

        PullToRefreshLayout layout = (PullToRefreshLayout) findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 千万别忘了告诉控件刷新完毕了哦！
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 5000);

    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 千万别忘了告诉控件刷新完毕了哦！
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }, 5000);

    }
}
