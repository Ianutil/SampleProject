/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ListActivity
 * Created by  ianchang on 2018-04-28 09:22:38
 * Last modify date   2018-04-28 09:22:38
 */

package com.ian.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian.widget.fragment.MenuBothViewFragment;
import com.ian.widget.fragment.RefreshViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        data = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            data.add("Item:"+i);
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        MenuBothViewFragment fragment = new MenuBothViewFragment();
//        RefreshViewFragment fragment = new RefreshViewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

            ViewHolder holder = new ViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

//            holder.item_menu.setSwipeEnable(true);

            holder.item_text.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView item_text;
            public TextView item_menu_item;
            public MenuItemLayout item_menu;
            public ViewHolder(View itemView) {
                super(itemView);

                item_text = (TextView)itemView.findViewById(R.id.item_text);
                item_menu_item  = (TextView)itemView.findViewById(R.id.item_menu_item);
                item_menu  = (MenuItemLayout)itemView.findViewById(R.id.item_menu);
            }
        }
    }



}
