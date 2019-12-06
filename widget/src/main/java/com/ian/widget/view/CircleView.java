/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CircleView
 * Created by  ianchang on 2018-05-04 16:29:36
 * Last modify date   2018-05-04 16:29:36
 */

package com.ian.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ianchang on 2018/5/4.
 */

public class CircleView extends View {

    private Paint mPaint;

    private RectF mRectF;
    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, Resources.getSystem().getDisplayMetrics());
        mPaint.setStrokeWidth(size);

        mRectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width, height;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (width <= 0 || height <= 0) return;

        int dimension = Math.min(width, height);

        float cx = dimension / 2;
        float cy = dimension / 2;

        float radius = dimension / 2 - mPaint.getStrokeWidth();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(cx, cy , radius, mPaint);


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.GREEN);
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = width;
        mRectF.left = height;
        canvas.drawArc(mRectF, 90, 270 , true, mPaint);

    }
}
