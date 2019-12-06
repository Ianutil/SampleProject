package com.ian.detector.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by ianchang on 2018/8/28.
 */

public class FocusView extends View {

    private int w;
    private int h;

    private Paint paint;

    private List<Rect> data;
    private int color;

    private Bitmap bitmap;

    public FocusView(Context context) {
        super(context);
        init();
    }

    public FocusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);


        color = 0x00000000;
        data = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        color = 0x3fff0000;

//        canvas.drawColor(color);

//        canvas.drawLine(0, 0, this.w, 0, paint);
//        canvas.drawLine(0, 0, 0, this.h, paint);
//        canvas.drawLine(this.w, 0, this.w, this.h, paint);
//        canvas.drawLine(0, this.h, this.w, this.h, paint);

        for (Rect rect : data) {
            canvas.drawRect(rect, paint);
        }


        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
//            canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2, getMeasuredHeight() / 2 - bitmap.getHeight() / 2, paint);
        }

    }


    public void updatePosition(List<Rect> rect) {
        data.clear();
        data.addAll(rect);

        color = 0x3fff0000;
        setVisibility(View.VISIBLE);
        postInvalidate();
    }


    public void updatePosition(Bitmap bitmap, List<Rect> rect) {
        data.clear();
//        data.addAll(rect);

        color = 0x9fff0000;
        setVisibility(View.VISIBLE);

        int x = (int) (getMeasuredWidth() * 1.0f / bitmap.getWidth());
        int y = (int) (getMeasuredHeight() * 1.0f / bitmap.getHeight());
        Rect dst;
        for (Rect src : rect) {
            dst = new Rect(src);
            dst.left *= x;
            dst.top *= y;
            dst.right *= x;
            dst.bottom *= y;
//            dst.left = src.bottom * y;
//            dst.right = dst.left + src.height() * y;
//            dst.top = src.right * x;
//            dst.bottom = dst.top + src.width() * x;

            Log.e("TAG", "更改后的位置" + src.toString());
            data.add(dst);
        }

        postInvalidate();
    }

    public void updatePosition(Bitmap bitmap) {


        if (bitmap != null && !bitmap.isRecycled()) {
            Matrix matrix = new Matrix();
            float x = 0.6f;
            float y = x;
//            float x = getMeasuredWidth() * 1.0f / bitmap.getWidth();
//            float y = getMeasuredHeight() * 1.0f / bitmap.getHeight();

            matrix.preScale(x, y);

            if (this.bitmap != null) {
                this.bitmap.recycle();
                this.bitmap = null;
            }

            this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        postInvalidate();
    }
}

