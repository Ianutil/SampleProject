/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  RefreshRecycleViewFragment
 * Created by  ianchang on 2018-06-04 17:40:26
 * Last modify date   2018-06-04 17:05:48
 */

package com.ian.widget.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian.widget.R;
import com.ian.widget.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/6/4.
 */

public class RefreshViewFragment extends Fragment implements RefreshRecyclerView.OnRefreshViewListener{

    private List<String> data;
    private MyAdapter adapter;
    private Handler handler = new Handler();
    private RefreshRecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_refreshview, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("第"+i+"个数据");
        }

        recyclerView = (RefreshRecyclerView)view.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setOnRefreshViewListener(this);

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

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_02, parent, false);
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


    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_text;
        public ViewHolder(View itemView) {
            super(itemView);

            item_text = (TextView)itemView.findViewById(R.id.item_text);
        }
    }
}
