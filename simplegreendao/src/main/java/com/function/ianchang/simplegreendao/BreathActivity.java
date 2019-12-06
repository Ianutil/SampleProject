/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BreathActivity
 * Created by  ianchang on 2018-07-12 14:18:33
 * Last modify date   2018-07-12 14:18:33
 */

package com.function.ianchang.simplegreendao;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.function.ianchang.BreathInterpolator;

/**
 * Created by ianchang on 2018/7/12.
 */

public class BreathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_breath);

        startAlphaBreathAnimation();

        startScaleBreathAnimation();
    }

    /**
     * 开启透明度渐变呼吸动画
     */
    private void startAlphaBreathAnimation() {
        View view = findViewById(R.id.text_view);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(4000);
        alphaAnimator.setInterpolator(new BreathInterpolator());//使用自定义的插值器
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.start();
    }


    /**
     * 开启缩放渐变呼吸动画
     */
    private void startScaleBreathAnimation() {
        View view = findViewById(R.id.text_view_scale);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.4f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.4f, 1f);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.setDuration(4000 );
        animatorSet.setInterpolator(new BreathInterpolator());
        animatorSet.start();

    }
}
