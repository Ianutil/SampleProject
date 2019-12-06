/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MenuBothViewFragment
 * Created by  ianchang on 2018-06-05 10:34:02
 * Last modify date   2018-06-05 10:33:47
 */

package com.ian.widget.fragment;

import android.graphics.Color;
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
import android.widget.Toast;

import com.ian.widget.MenuItemLayout;
import com.ian.widget.MenuRecyclerView;
import com.ian.widget.R;
import com.ian.widget.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/6/4.
 */

public class MenuBothViewFragment extends Fragment implements RefreshRecyclerView.OnRefreshViewListener{

    private List<String> data;
    private MyAdapter adapter;

    private Handler handler = new Handler();
    private MenuRecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_menu, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("第"+i+"个数据");
        }

        recyclerView = (MenuRecyclerView)view.findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setOnRefreshViewListener(this);

    }


    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_04, parent, false);
            itemView.setBackgroundColor(Color.parseColor("#3f23df"));
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.item_text.setText(data.get(position));

            holder.item_menu.setSwipeEnable(true);

            holder.item_menu_item_01.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.item_menu.closeMenu();
                    Toast.makeText(v.getContext(), "删除成功"+position, Toast.LENGTH_SHORT).show();
                }
            });

            holder.item_menu_item_02.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.item_menu.closeMenu();
                    Toast.makeText(v.getContext(), "增加成功"+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
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

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_text;
        public TextView item_menu_item_01;
        public TextView item_menu_item_02;
        public MenuItemLayout item_menu;
        public ViewHolder(View itemView) {
            super(itemView);

            item_menu = (MenuItemLayout)itemView.findViewById(R.id.item_menu);
            item_text = (TextView)itemView.findViewById(R.id.item_text);
            item_menu_item_01 = (TextView)itemView.findViewById(R.id.item_menu_item_01);
            item_menu_item_02 = (TextView)itemView.findViewById(R.id.item_menu_item_02);
        }
    }
}
