/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  FaceRectangleView
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:41
 */

package com.ian.machine.member.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ian.machine.member.R;
import com.plattysoft.leonids.ParticleSystem;
import com.tzutalin.dlib.VisionDetRet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/8/15.
 */

public class FaceRectangleView extends View {
    private static final String TAG = FaceRectangleView.class.getSimpleName();

    private Paint mFacePaint;

    private List<Rect> data;

    public FaceRectangleView(Context context) {
        this(context, null);
    }

    public FaceRectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        data = new ArrayList<>();

        mFacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFacePaint.setColor(Color.RED);
        float stroke = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mFacePaint.setStrokeWidth(stroke);
        mFacePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(0x3f8E0000);
        canvas.drawColor(0x3FFF88FF);

        for (Rect rect : data) {
            canvas.drawRect(rect, mFacePaint);
        }

    }

    public void setViewSize(int width, int height) {
        Log.e(TAG, "setViewSize:" + width + " height=" + height + " width:" + getMeasuredWidth() + " height:" + getMeasuredHeight());

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        this.setLayoutParams(params);

    }


    /******
     * 更新UI
     * @param data
     */
    public void updateRectangle(List<Rect> data) {
        this.data.clear();
        this.data.addAll(data);
        postInvalidate();
    }

}
