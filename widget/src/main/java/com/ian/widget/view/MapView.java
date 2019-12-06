/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MapView
 * Created by  ianchang on 2018-06-07 15:23:57
 * Last modify date   2018-05-04 17:07:19
 */

package com.ian.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ianchang on 2018/5/4.
 */

public class MapView extends AppCompatImageView {

    /*****
     * RGB和Alpha的终值计算方法如下：
     Red通道终值= a[0] * srcR + a[1] * srcG + a[2] * srcB + a[3] * srcA + a[4]
     Green通道终值= a[5] * srcR + a[6] * srcG + a[7] * srcB + a[8] * srcA + a[9]
     Blue通道终值= a[10] * srcR + a[11] * srcG + a[12] * srcB + a[13] * srcA + a[14]
     Alpha通道终值= a[15]*srcR+a[16]*srcG + a[17] * srcB + a[18] * srcA + a[19]

     备注：
     srcR为原图Red通道值，srcG为原图Green通道值，srcB为原图Blue通道值，srcA为原图Alpha通道值。
     每个通道的源值和终值都在0到255的范围内。即使计算结果大于255或小于0，值都将被限制在0到255的范围内。
     */
    private final static float[] MATRIX = new float[]{
            0.5f, 0, 0, 0, 0,
            0, 0.5f, 0, 0, 0,
            0, 0, 0.5f, 0, 0,
            0, 0, 0, 1, 0};

    private Matrix mMatrix;
    private Paint mPaint;
    private RectF mRectF;
    private Bitmap mBitmap;

    private float distance;

    private static final int ACTION_MOVE = 1;
    private static final int ACTION_ZOOM = 2;

    private int actionStatus;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float downX, downY;

    private PointF centerPointF;
    private float scale;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = event.getPointerCount();

        // 一点，表示移动
        if (count == 1) {
            actionStatus = ACTION_MOVE;
            move(event);
        } else if (count == 2) {
            actionStatus = ACTION_ZOOM;
            zoom(event);
        }

        return true;
//        return super.onTouchEvent(event);
    }

    /***
     * 缩放
     * @param event
     */
    private void zoom(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:

                // 第一个点与第二个点之间的距离
                distance = (float) getDistanceOfPoint(event);

                // 缩放的中心点
                centerPointF = getPointOfCenter(event);

                break;
            case MotionEvent.ACTION_MOVE:

                // 滑动的点到起始点的距离
                float dist = (float) getDistanceOfPoint(event);
                scale = dist / distance;
//                    mMatrix.setScale(scale, scale, centerPointF.x, centerPointF.y);

                Log.d("TAG", "dist：" + dist + " distance：" + distance + " scale：" + scale);

                postInvalidate();

                distance = dist;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

    }

    /**********
     * 移动
     * @param event
     */
    private void move(MotionEvent event) {
        mMatrix.reset();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 落下的第一个点
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE: // 滑动的点
                float moveX = event.getX(0) - downX;
                float moveY = event.getY(0) - downY;

                mRectF.left += moveX;
                mRectF.top += moveY;
                mRectF.right += moveX;
                mRectF.bottom += moveY;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        postInvalidate();

        downX = event.getX();
        downY = event.getY();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, Resources.getSystem().getDisplayMetrics());
        mPaint.setStrokeWidth(size);

        mRectF = new RectF();

        InputStream in = null;
        try {
            in = getResources().getAssets().open("bg_store.png");
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            drawHotPoint(mBitmap);
            this.setImageBitmap(mBitmap);

            mMatrix = new Matrix(getMatrix());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#3f3f77"));
        super.onDraw(canvas);

        int width, height;
//        Rect rect = getDrawable().getBounds();
        width = getDrawable().getIntrinsicHeight();
        height = getDrawable().getIntrinsicWidth();
//        Log.d("TAG", "-------->width:"+width+" height:"+height);
//        Log.d("TAG", "-------->"+rect.toString());

        canvas.save();
//        mMatrix.setTranslate(0, getMeasuredHeight()- 100);
//        mMatrix.setScale(0.7f, 0.7f);
        RectF src = new RectF();
        src.left = 0;
        src.top = 0;
        src.right = mBitmap.getWidth();
        src.bottom = mBitmap.getHeight();

        RectF dst = new RectF();
        dst.left = 0;
        dst.top = 0;
        dst.right = getWidth();
        dst.bottom = getMeasuredHeight();


        mPaint.setAlpha(255 / 2);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.START);
        mPaint.setColorFilter(new LightingColorFilter(0x00FF00, 0xFF0000));
//        mPaint.setColorFilter(new LightingColorFilter(0x888888, 0x000000));
        canvas.drawBitmap(mBitmap, matrix, mPaint);


        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.END);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(MATRIX);
        mPaint.setColorFilter(filter);
        canvas.drawBitmap(mBitmap, matrix, mPaint);
        canvas.restore();


//        if (scale != 0){
//            mMatrix.setScale(scale, scale, centerPointF.x, centerPointF.y);
//        }

        switch (actionStatus) {
            case ACTION_MOVE:
                mMatrix.setScale(scale, scale);
                mMatrix.postTranslate(mRectF.centerX(), mRectF.centerY());
                break;
            case ACTION_ZOOM:
                mMatrix.setScale(scale, scale);
                mMatrix.postTranslate(mRectF.centerX(), mRectF.centerY());
                break;
            default:
                float max = Math.max(width, height);
                //
                scale = max / Math.max(getMeasuredWidth(), getMeasuredHeight());
                mMatrix.setScale(scale, scale);
                width *= scale;
                height *= scale;
                mMatrix.postTranslate(getWidth() /2 - width/2, getHeight()/2 - height/2);
                break;
        }

        mPaint.setColorFilter(new LightingColorFilter(0x00FFFF, 0xFF0000));
        mPaint.setAlpha(255);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        canvas.restore();
    }


    private void drawHotPoint(Bitmap srcBitmap) {
        Canvas canvas = new Canvas(srcBitmap);
        int width, height;
        width = srcBitmap.getWidth();
        height = srcBitmap.getHeight();

        int dimension = Math.min(width, height);

        float cx = dimension / 2;
        float cy = dimension / 2;

        mPaint.setStrokeWidth(1);
        float radius = dimension / 10 - mPaint.getStrokeWidth();

        RadialGradient shader;
        mPaint.setStyle(Paint.Style.STROKE);
        shader = new RadialGradient(cx, cy, radius,
                new int[]{
                        Color.parseColor("#3FFC9BB7"),
                        Color.parseColor("#9FFB497C")
                },
                new float[]{
//                    1.0f,0.5f
                        0.5f, 0.5f
                },
                RadialGradient.TileMode.MIRROR);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setShader(shader);

//        canvas.drawCircle(cx, cy , radius, mPaint);


        float posX, posY, colorX;
        int alpha;
        for (int i = 0; i < 30; i++) {

            posX = (float) Math.random() * 1000 % width;
            posY = (float) Math.random() * 1000 % height;

            colorX = (float) Math.random();
            alpha = (int) (Math.random() * 1000 % 100) + 140;
            if (alpha > 255) alpha = 255;
            Log.d("TAG", "posX=" + posX + " posY:" + posY + " colorX:" + colorX + " alpha:" + alpha);

            shader = new RadialGradient(posX, posY, radius,
                    new int[]{
                            Color.parseColor("#3FFC9BB7"), // 3FFC9BB7
                            Color.parseColor("#9FFB497C") // 9FFB497C
                    },
                    new float[]{
//                            colorX,1f
                            0.25f, 1f
                    },
                    RadialGradient.TileMode.MIRROR);

            mPaint.setShader(shader);
            mPaint.setAlpha(alpha);
            canvas.save();
            canvas.clipRect(posX - radius, posY - radius, posX + radius, posY + radius);
            canvas.drawCircle(posX, posY, radius, mPaint);
            canvas.restore();
        }
    }


    private PointF getPointOfCenter(MotionEvent event) {
        PointF pointF = new PointF();

        pointF.x = (event.getX(0) + event.getX(1)) / 2;
        pointF.y = (event.getY(0) + event.getY(1)) / 2;

        return pointF;
    }

    /*****
     * 计算两点之间的距离
     * @param event
     * @return
     */
    private double getDistanceOfPoint(MotionEvent event) {

        float x = event.getX(0);
        float y = event.getY(0);

        float x1 = event.getX(1);
        float y1 = event.getY(1);

        // 求a^2 + b^2 = c^2求斜边的长度
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
}
