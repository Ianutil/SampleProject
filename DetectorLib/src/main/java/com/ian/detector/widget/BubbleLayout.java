/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BubbleLayout
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:42
 */
package com.ian.detector.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ian.detector.bean.BubbleInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: BubbleLayout
 * @Description: TODO
 * @author: raot raotao.bj@cabletech.com.cn/719055805@qq.com
 * @date: 2015年3月2日 下午2:52:08
 */
public class BubbleLayout extends View implements Runnable{

    private int mBubbleRadius = 30;

    private List<BubbleInfo> mBubbles = new ArrayList<BubbleInfo>();
    private Random random = new Random();
    private int width, height;
    private boolean isRunning = false;
    private boolean isPause = false;

    private Paint mPaint;
    private ExecutorService mExecutor;

    public BubbleLayout(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        mPaint = new Paint();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        
        if (width == 0 || height == 0) return;

        isPause = false;

        // 绘制渐变正方形
//        Shader shader = new LinearGradient(0, 0, 0, height, new int[]{
//                getResources().getColor(R.color.blue_bright),
//                getResources().getColor(R.color.blue_light),
//                getResources().getColor(R.color.blue_dark)},
//                null, Shader.TileMode.REPEAT);
//        mPaint.setShader(shader);
//        canvas.drawRect(0, 0, width, height, mPaint);
//        mPaint.reset();

//        canvas.drawColor(0x3fffee22);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(200);
//
        // 绘制气球
        List<BubbleInfo> list = new ArrayList<BubbleInfo>(mBubbles);
        for (BubbleInfo bubble : list) {

            if (bubble.getY() - bubble.getSpeedY() <= 0) {
                mBubbles.remove(bubble);
            } else {

                int i = mBubbles.indexOf(bubble);

                if (bubble.getX() + bubble.getSpeedX() <= bubble.getRadius()) {
                    bubble.setX(bubble.getRadius());
                } else if (bubble.getX() + bubble.getSpeedX() >= width - bubble.getRadius()) {
                    bubble.setX(width - bubble.getRadius());
                } else {
                    bubble.setX(bubble.getX() + bubble.getSpeedX());
                }

                bubble.setY(bubble.getY() - bubble.getSpeedY());
                mBubbles.set(i, bubble);

                // 随机颜色
                mPaint.setColor(bubble.color);
                canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), mPaint);
            }
        }


        invalidate();
    }

    @Override
    public void invalidate() {
        // TODO Auto-generated method stub
        super.invalidate();
        isPause = true;
    }

    public void startBubble(){
        
        if (isRunning) return;

        try {
            isRunning = !isRunning;
            mExecutor.execute(this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    public void stopBubble(){
        try {
            if(!mExecutor.isShutdown()){
                mExecutor.shutdown();
                isRunning = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            if (isPause) {
                continue;
            }
            
            try {
                Thread.sleep(random.nextInt(3) * 300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            BubbleInfo bubble = new BubbleInfo();
            
            int radius = random.nextInt(mBubbleRadius);
            
            while (radius == 0) {
                radius = random.nextInt(mBubbleRadius);
            }
            
            float speedY = random.nextFloat() * 5;
            
            while (speedY < 1) {
                speedY = random.nextFloat() * 5;
            }
            
            bubble.setRadius(radius);
            bubble.setSpeedY(speedY);
            bubble.setX(width / 2);
            bubble.setY(height);

            float speedX = random.nextFloat() - 0.5f;
            
            while (speedX == 0) {
                speedX = random.nextFloat() - 0.5f;
            }
            
            bubble.setSpeedX(speedX * 2);

            bubble.color = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(100)+120);

            mBubbles.add(bubble);
        }
    }
}
