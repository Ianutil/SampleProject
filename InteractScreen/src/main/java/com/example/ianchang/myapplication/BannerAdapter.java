/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author Ian
 * Date 18-1-25 下午3:39
 */

package com.example.ianchang.myapplication;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by martindu on 08/09/2017.
 */

public class BannerAdapter extends PagerAdapter implements View.OnClickListener{

    private ImageView mPlaceholder;
    private boolean mLooper;
    private List<BannerInfo> datas;
    private ViewPager viewPager;

    public BannerAdapter(List<BannerInfo> been, boolean looper, ImageView placeholder) {
        this.datas = been;
        this.mLooper = looper;
        this.mPlaceholder = placeholder;
    }

    @Override
    public int getCount() {
        if (datas == null || datas.isEmpty())
            return 1;
        else if (mLooper && datas.size() > 1)
            return Integer.MAX_VALUE;
        else
            return datas.size();
    }

    public int getItemPosition(){
        int position = viewPager.getCurrentItem();
        return position % datas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (datas == null || datas.isEmpty()) {
            container.addView(mPlaceholder);
            return mPlaceholder;
        } else {

            int index = position % datas.size();

            BannerInfo info = datas.get(index);


            View contentView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, null, false);
            container.addView(contentView);

            contentView.setTag(position);

            ImageView placeView = contentView.findViewById(R.id.item_img);

            placeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(container.getContext()).load(info.file).into(placeView);

            ImageView startView = contentView.findViewById(R.id.item_btn);
            startView.setOnClickListener(this);
            startView.setImageResource(R.drawable.mediacontroller_play);

            // 图片
            if (info.type == 1){
                startView.setVisibility(View.GONE);
            }else if (info.type == 2){
                // 视频
                startView.setVisibility(View.VISIBLE);
            }

            startView.setTag(index);

            return contentView;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();

        BannerInfo info = datas.get(index);

        Log.e("TAG", "播放器:播放文件" + info.file);

        if (!TextUtils.isEmpty(info.file)){
            Intent intent = new Intent(v.getContext(), VideoActivity.class);
            if (index >= (datas.size() - 5)){
                intent.putExtra("index", 5);
            }else if (index >= (datas.size() - 4)){
                intent.putExtra("index", 4);
            }else if (index >= (datas.size() - 3)){
                intent.putExtra("index", 3);
            }else if (index >= (datas.size() - 2)){
                intent.putExtra("index", 2);
            }else if (index >= (datas.size() - 1)){
                intent.putExtra("index", 1);
            }else {
                intent.putExtra("index", 0);
            }
            v.getContext().startActivity(intent);

//            MediaPlayManager.shareInstance().startMediaPlayer(info.file);
        }

    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
    }


}
