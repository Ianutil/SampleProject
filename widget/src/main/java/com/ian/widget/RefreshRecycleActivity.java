/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  RefreshRecycleActivity
 * Created by  ianchang on 2018-06-04 09:12:20
 * Last modify date   2018-05-31 11:11:21
 */

package com.ian.widget;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian.widget.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class RefreshRecycleActivity extends AppCompatActivity implements RefreshRecyclerView.OnRefreshViewListener {

    private RefreshRecyclerView recyclerView;

    private List<String> data;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_recycle);

        recyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        setData();

        RefreshAdapter refreshAdapter = new RefreshAdapter(data, this);
        recyclerView.setAdapter(refreshAdapter);
        recyclerView.setOnRefreshViewListener(this);

        recyclerView.setRefreshEnable(true);
        recyclerView.setLoadMoreEnable(true);
    }

    private void setData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("第" + i + "个");
        }
    }

    @Override
    public void onRefresh() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.clear();
                for (int i = 0; i < 10; i++) {
                    data.add("第" + i + "个");
                }

                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.setRefreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLoadMoreComplete();
                int count = data.size();
                for (int i = 0; i < 10; i++) {
                    data.add("第" + (i +count) + "个");
                }
            }
        }, 2000);
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_refresh_item, parent, false);
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
