/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StuffAdapter
 * Created by  ianchang on 2018-08-16 15:42:49
 * Last modify date   2018-08-16 15:42:49
 */

package com.ian.machine.screen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ian.machine.screen.bean.StuffInfo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/8/16.
 */

public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.ViewHolder> {

    public static final int CART_CODE = 0;
    public static final int WAREHOUSE_CODE = 1;

    private List<StuffInfo> data;
    private IOnItemClickListener listener;
    // FIXME: 2018/8/17 0：表示购物车；1：表示货架
    private int mType;


    public StuffAdapter(List<StuffInfo> data, int type) {
        this.data = data;
        this.mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stuff, parent, false);

        AutoUtils.autoSize(convertView);

        StuffAdapter.ViewHolder holder = new StuffAdapter.ViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final StuffInfo info = data.get(position);

        Glide.with(holder.itemView.getContext()).load(info.imageUrl).into(holder.item_img);

        holder.item_text_01.setText(String.format("商品名称：%s", info.name));

        holder.item_text_02.setText(String.format("价格(￥):%.2f", info.price));

        holder.item_text_03.setText(String.format("个（件）数：%d", info.count));

        holder.item_text_04.setText(String.format("总计：%.2f", info.price * info.count));

        // FIXME: 2018/8/17 0：表示购物车；1：表示货架
        if (mType == WAREHOUSE_CODE){
            holder.item_text_04.setVisibility(View.GONE);
        }else {
            holder.item_text_04.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listener != null){
                    listener.onItemClick(holder.itemView, position, data.get(position));
                }
            }
        });


        holder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), LandscapeActivity.class);
                intent.putExtra(LandscapeActivity.IMAGE_URL, info.imageUrl);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public List<StuffInfo> getData(){
        return data;
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateDate(StuffInfo info){
        if (data == null){
            data = new ArrayList<>();
        }

        data.add(info);
        notifyDataSetChanged();
    }

    public void updateDate(List<StuffInfo> info){
        if (data == null){
            data = new ArrayList<>();
        }

        data.clear();
        data.addAll(info);
        notifyDataSetChanged();
    }

    public void addItem(List<StuffInfo> info){
        if (data == null){
            data = new ArrayList<>();
        }

        data.addAll(info);
        notifyDataSetChanged();
    }

    public void addItem(StuffInfo info){
        if (data == null){
            data = new ArrayList<>();
        }

        data.add(info);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView item_img;
        public TextView item_text_01;
        public TextView item_text_02;
        public TextView item_text_03;
        public TextView item_text_04;
        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            item_img = (ImageView)itemView.findViewById(R.id.item_img);
            item_text_01 = (TextView)itemView.findViewById(R.id.item_text_01);
            item_text_02 = (TextView)itemView.findViewById(R.id.item_text_02);
            item_text_03 = (TextView)itemView.findViewById(R.id.item_text_03);
            item_text_04 = (TextView)itemView.findViewById(R.id.item_text_04);
        }
    }
}
