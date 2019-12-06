/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  RefreshActivity
 * Created by  ianchang on 2018-05-31 10:11:01
 * Last modify date   2018-05-31 10:11:00
 */

package com.ian.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class RefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnScrollChangeListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);


        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.cardview_light_background
        );
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        setData();
        RefreshAdapter refreshAdapter = new RefreshAdapter(data, this);
        recyclerView.setAdapter(refreshAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
//        recyclerView.addOnScrollListener(this);
    }

    private void setData() {
        data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("第" + i + "个");
        }
    }

    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchingNewData();
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }, 3000);
    }

    private void fetchingNewData() {
        data.add(0, "下拉刷新出来的数据");
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }

    private class RefreshAdapter extends RecyclerView.Adapter<RefreshAdapter.ViewHolder>{

        private List<String> data;
        private Context context;

        public RefreshAdapter(List<String> data, Context context){
            this.data = data;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.item_text.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView item_text;
            public ViewHolder(View itemView) {
                super(itemView);

                item_text = (TextView)itemView.findViewById(R.id.item_text);

            }
        }
    }
}
