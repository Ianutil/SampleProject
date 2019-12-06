/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  PannelRoseView
 * Created by  ianchang on 2018-06-19 13:13:44
 * Last modify date   2018-05-04 17:07:19
 */

package com.ian.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/5/4.
 */

public class PanelRoseView extends View {

    public PanelRoseView(Context context) {
        super(context);
    }

    private final List<String> data = new ArrayList<>();
    public void onDraw(Canvas canvas) {
        //画布背景
        canvas.drawColor(Color.BLACK);

        Log.d("TAG", "before size="+data.size());
        data.add("13234");
        Log.d("TAG", "after size="+data.size());

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width == 0 || height == 0) {
            return;
        }


        Log.e("TAG", "-------------->width=" + width + " height=" + height);

        height = height / 2;

        Path path = new Path();

        path.moveTo(0, 0);

        path.lineTo(width, 0);

        path.lineTo(width / 2, height);
        path.close();

        // 设置渐变
        LinearGradient shader = new LinearGradient(
                0, 0,
                width, height,
                Color.parseColor("#4977F1"), // 8168CC
                Color.parseColor("#FB497C"),  // 4A77F0
                Shader.TileMode.CLAMP);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);

        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        canvas.drawPath(path, paint);

        path.moveTo(width/2 - width / 2, height / 3 * 2);
        path.lineTo(width/2 + width / 2, height / 3 * 2);
        path.lineTo(width / 2, height);
        path.close();
        canvas.drawPath(path, paint);

        path.moveTo(80, height / 3);
        path.lineTo(width - 80, height / 3);
        path.lineTo(width / 2, height);
        path.close();
        canvas.drawPath(path, paint);

        paint.setAlpha(120);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
    }

}
