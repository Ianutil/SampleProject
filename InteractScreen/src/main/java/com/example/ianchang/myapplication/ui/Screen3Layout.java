package com.example.ianchang.myapplication.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ianchang.myapplication.R;
import com.example.ianchang.myapplication.bean.ScreenStyleInfo;

/**
 * Created by ianchang on 2017/10/30.
 */

public class Screen3Layout extends BaseScreenLayout {

    public ScreenStyleInfo mScreenStyleInfo;
    private View containerView;

    public Screen3Layout(Context context) {
        super(context);
    }

    public Screen3Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Screen3Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*********
     * 更新UI并，绑定数据
     * @param mScreenStyleInfo
     */
    @Override
    public void updateContentView(ScreenStyleInfo mScreenStyleInfo) {
        if (mScreenStyleInfo == null) return;

        this.mScreenStyleInfo = mScreenStyleInfo;

        log("显示三屏");

        // 根据 orientation 加载不同类型的布局
        switch (mScreenStyleInfo.orientation){
            case ScreenStyleInfo.LANDSCAPE:  // 横屏 相关类型的
                containerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_landscape, this, false);
                showLandscape();
                break;
            case ScreenStyleInfo.PORTRAIT: // 竖屏 相关类型的
                containerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_portrait, this, false);
                showPortrait();
                break;
            default:  // 默认全屏展示
                containerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_landscape, this, false);
                showLandscape();
                break;
        }

        containerView.setBackgroundColor(Color.DKGRAY);

        // 每次更新UI，都remove掉所有的子View，并增加新的子View进来
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(containerView, params);
        containerView.requestLayout();
        invalidate();
    }

    // 显示横屏
    private void showLandscape(){
        log("显示三屏->showLandscape");

        LinearLayout layout;

        // 左边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_left);
        createScreen("screen_left", mScreenStyleInfo.leftScreenGroupInfo, layout);

        //  中间 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_middle);
        createScreen("screen_middle", mScreenStyleInfo.middleScreenGroupInfo, layout);


        // 右边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_right);
        createScreen("screen_right", mScreenStyleInfo.rightScreenGroupInfo, layout);
    }

    // 显示竖屏
    private void showPortrait(){
        log("显示三屏->showPortrait");

        LinearLayout layout;

        // 上边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_up);
        createScreen("screen_up", mScreenStyleInfo.leftScreenGroupInfo, layout);

        // 中间 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_middle);
        createScreen("screen_middle", mScreenStyleInfo.middleScreenGroupInfo, layout);

        // 下边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_down);
        createScreen("screen_down", mScreenStyleInfo.rightScreenGroupInfo, layout);
    }

}
