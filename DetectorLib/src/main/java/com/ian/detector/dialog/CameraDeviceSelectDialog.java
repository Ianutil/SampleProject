/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CameraDeviceSelectDialog
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:42
 */

package com.ian.detector.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ian.detector.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/5/14.
 * <p>
 * <p>
 * 选择摄像机对话框
 */

public class CameraDeviceSelectDialog implements PopupWindow.OnDismissListener {

    private Context mContext;
    private PopupWindow mWindow;

    private RecyclerView mRecyclerView;
    private List<UsbDevice> mData;

    private OnItemClickListener itemClickListener;
    private CameraDeviceAdapter mAdapter;
    private int mPosition;

    public CameraDeviceSelectDialog(Context context) {
        this.mContext = context;
    }


    public void createWindow() {

        mWindow = new PopupWindow(mContext);

        mWindow.setFocusable(true);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_camera_device, null);
        mWindow.setContentView(contentView);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int width = metrics.widthPixels / 3 * 2;
//        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, metrics);
        int height = metrics.heightPixels / 3;
        mWindow.setWidth(width);
        mWindow.setHeight(height);
        mWindow.setOutsideTouchable(true);
        mWindow.setOnDismissListener(this);
        mWindow.setBackgroundDrawable(new BitmapDrawable());

//        mWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        mWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setBackgroundColor(Color.WHITE);

        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        CameraDeviceAdapter adapter = new CameraDeviceAdapter();
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;

        mData = new ArrayList<>();
    }


    public void setData(List<UsbDevice> data) {
        if (!mData.isEmpty()) {
            mData.clear();
        }

        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public void showDialog(View view, int position) {
        int top = (int) view.getResources().getDimension(R.dimen.margin_15);
        mWindow.showAsDropDown(view, 0, top);

        mRecyclerView.getAdapter().notifyDataSetChanged();
        mPosition = position;
        // 产生背景变暗效果
        Activity activity = (Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    public void dismiss() {
        mWindow.dismiss();
        onDismiss();
    }


    public interface OnItemClickListener {
        void onItemClick(UsbDevice info, int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onDismiss() {
        // 产生背景变暗效果
        Activity activity = (Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }


    private class CameraDeviceAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera_device, null);
            AutoUtils.autoSize(contentView);
            ViewHolder holder = new ViewHolder(contentView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            UsbDevice info = mData.get(position);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);

            String name = String.format("USB外接相机:(序号%x  productId:%x  名称:%s)", info.getVendorId(), info.getProductId(), info.getDeviceName());
            holder.item_text.setText(name);


            if (mPosition == position) {
                holder.item_layout.setSelected(true);
            } else {
                holder.item_layout.setSelected(false);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();

            UsbDevice info = mData.get(position);


            Log.i("TAG", "deviceName---->" + info.getDeviceName());

            if (itemClickListener != null) {
                itemClickListener.onItemClick(info, position);
            }
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_text;
        public View item_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            item_text = (TextView) itemView.findViewById(R.id.item_text);
            item_layout = itemView.findViewById(R.id.item_layout);
        }
    }
}
