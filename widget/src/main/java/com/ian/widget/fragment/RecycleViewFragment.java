/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  RecycleViewFragment
 * Created by  ianchang on 2018-06-04 17:40:26
 * Last modify date   2018-06-04 17:05:48
 */

package com.ian.widget.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/6/4.
 */

public class RecycleViewFragment extends Fragment {

    private List<String> data;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_recycleview, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("第"+i+"个数据");
        }

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

    }


    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_01, parent, false);
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
