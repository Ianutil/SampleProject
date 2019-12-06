package com.example.ianchang.myapplication.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ianchang.myapplication.R;
import com.example.ianchang.myapplication.bean.ScreenCompositionInfo;
import com.example.ianchang.myapplication.bean.ScreenGroupInfo;
import com.example.ianchang.myapplication.bean.ScreenStyleInfo;

/**
 * Created by ianchang on 2017/10/30.
 */
public abstract class BaseScreenLayout extends LinearLayout {

    public BaseScreenLayout(Context context) {
        super(context);
    }

    public BaseScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void setLayoutParams(LinearLayout layout, float weight) {
        LayoutParams params = (LayoutParams) layout.getLayoutParams();
        params.weight = weight;
        layout.setLayoutParams(params);
    }

    public void updateContentView(ScreenStyleInfo mScreenStyleInfo) {

    }

    /*********
     * 显示相对应的组件
     * @param info
     * @param layout
     */
    public void updateContentView(ScreenCompositionInfo info, ViewGroup layout) {

        // 展示内容名称：图片、视频、文本等组件名称或者代号
        int styleType = info.styleType;

        log("展示内容类型："+styleType);

        View childView;
        // 根据 styleType 加载不同类型的布局
        switch (styleType) {
            case 1: // 样式1
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_style_01, layout, false);
                break;
            case 2: // 样式2
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_style_02, layout, false);
                break;
            case 3: // 样式3
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_style_03, layout, false);
                break;
            default:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_style_01, layout, false);
                break;
        }

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(childView, params);
    }

    /*******
     * 创造一个屏
     * @param tag
     * @param screenGroupInfo
     * @param layout
     * @return
     */
    protected ScreenLayout createScreen(String tag, ScreenGroupInfo screenGroupInfo, LinearLayout layout) {

        ScreenLayout screenLayout = null;

        if (screenGroupInfo == null || screenGroupInfo.weight == 0) {
            setLayoutParams(layout, 0);
        } else {
            setLayoutParams(layout, screenGroupInfo.weight);

            screenLayout = new ScreenLayout(getContext());
            screenLayout.setTag(tag);
            screenLayout.updateContentView(screenGroupInfo);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(screenLayout, params);
        }

        return screenLayout;
    }


    protected void log(String msg){
        Log.e("TAG", msg);
    }
}
