package com.example.ianchang.myapplication.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ianchang.myapplication.bean.ScreenGroupInfo;

/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class ScreenStyleLayout extends LinearLayout {

    private ScreenGroupInfo mScreenGroupInfo;

    public ScreenStyleLayout(Context context) {
        super(context);
        initView();
    }

    public ScreenStyleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScreenStyleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        Log.d("TAG",  "->>>>>>>>>>>>>"+width +",  "+height);
        setMeasuredDimension(width, height);

        int childWidth, childHeight;

        if (getOrientation() == VERTICAL) {
            childWidth = width ;
            childHeight = height / count;
        } else {
            childWidth = width / count;
            childHeight = height;
        }

        View child;
        ViewGroup.LayoutParams params;

        for (int i = 0; i < count; i++){
            child = this.getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            Log.d("TAG",  "-----------------"+child.getMeasuredWidth() +",  "+child.getMeasuredHeight());

            setMeasuredDimension(childWidth, childHeight);
        }

    }
}
