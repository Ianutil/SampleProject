/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  SplashActivity
 * Created by  ianchang on 2018-07-19 09:41:41
 * Last modify date   2018-07-19 09:41:40
 */

package com.example.ianchang.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SplashActivity extends AppCompatActivity implements Animator.AnimatorListener {

    private ImageView mImgMask, mImgbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        setContentView(R.layout.activity_splash);

        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decoderView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decoderView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //or ?
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        mImgbackground = (ImageView) findViewById(R.id.img_background);

        mImgMask = (ImageView) findViewById(R.id.img_mask);

        String url = "http://attachments.gfan.com/forum/201710/10/141334dweyed7s2yweycvv.jpg";
        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("TAG", "onLoadFailed");

                // 加载失败后，直接跳转到主页
                mHandler.sendEmptyMessageDelayed(1, 3000);

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                Log.e("TAG", "onResourceReady");

                // 加载成功后，开始执行动画
                mHandler.sendEmptyMessage(0);
                return false;
            }
        }).into(mImgbackground);

    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 跳转流程
            if (msg.what == 1){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }else if (msg.what == 0){ // 表示加载图片完成

                // 从无到有
                mImgbackground.setAlpha(0f);
                ObjectAnimator objAnimator = ObjectAnimator.ofFloat(mImgbackground, "alpha", 0f, 1f);
                objAnimator.setDuration(2000);
                objAnimator.start();

                startAnimat();
            }
        }
    };

    private void startAnimat() {

        int imgHeight = mImgMask.getHeight() / 5;
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int curY = height / 2 + imgHeight / 2;
        int dy = (height - imgHeight) / 2;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorTranslate = ObjectAnimator.ofFloat(mImgMask, "translationY", 0, dy);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mImgMask, "ScaleX", 1f, 0.2f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mImgMask, "ScaleY", 1f, 0.2f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mImgMask, "alpha", 1f, 0.5f);
        set.play(animatorTranslate).with(animatorScaleX).with(animatorScaleY).with(animatorAlpha);
        set.setDuration(1200);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
        set.addListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
