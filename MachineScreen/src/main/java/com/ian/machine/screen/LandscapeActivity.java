/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  LandscapeActivity
 * Created by  ianchang on 2018-08-17 17:01:20
 * Last modify date   2018-08-17 17:01:20
 */

package com.ian.machine.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

public class LandscapeActivity extends AppCompatActivity {
    public static final String IMAGE_URL = "IMAGE_URL";
    private String url;

    private PhotoView mPhotoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);


        url = getIntent().getStringExtra(IMAGE_URL);

        mPhotoView = (PhotoView) findViewById(R.id.phote_view);

        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(mPhotoView);
        }


        // 启用图片缩放功能
        mPhotoView.enable();
        // 禁用图片缩放功能 (默认为禁用，会跟普通的ImageView一样，缩放功能需手动调用enable()启用)
        //        mPhotoView.disenable();
        // 获取图片信息
        Info info = mPhotoView.getInfo();
        // 从普通的ImageView中获取Info
        //        Info info = mPhotoView.getImageViewInfo(ImageView);
        // 从一张图片信息变化到现在的图片，用于图片点击后放大浏览，具体使用可以参照demo的使用
        //        mPhotoView.animaFrom(info);
        // 从现在的图片变化到所给定的图片信息，用于图片放大后点击缩小到原来的位置，具体使用可以参照demo的使用
        mPhotoView.animaTo(info, new Runnable() {
            @Override
            public void run() {
                //动画完成监听
            }
        });
        // 获取/设置 动画持续时间
        mPhotoView.setAnimaDuring(600);
//        int d = mPhotoView.getAnimaDuring();
        // 获取/设置 最大缩放倍数
        mPhotoView.setMaxScale(24);
//        float maxScale = mPhotoView.getMaxScale();
        // 设置动画的插入器
        mPhotoView.setInterpolator(new AccelerateDecelerateInterpolator());
    }


    public void comeBack(View view) {
        finish();
    }
}
