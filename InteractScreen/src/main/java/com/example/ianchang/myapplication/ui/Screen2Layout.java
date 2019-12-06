package com.example.ianchang.myapplication.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.ianchang.myapplication.R;
import com.example.ianchang.myapplication.bean.ScreenStyleInfo;

/**
 * Created by ianchang on 2017/10/30.
 */

public class Screen2Layout extends BaseScreenLayout {

    private ScreenStyleInfo mScreenStyleInfo;

    private LinearLayout containerView;

    public Screen2Layout(Context context) {
        super(context);
    }

    public Screen2Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Screen2Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /*********
     * 更新UI并，绑定数据
     * @param mScreenStyleInfo
     */
    @Override
    public void updateContentView(ScreenStyleInfo mScreenStyleInfo) {
        this.mScreenStyleInfo = mScreenStyleInfo;

        log("显示二屏");

        // 根据 orientation 加载不同类型的布局
        switch (mScreenStyleInfo.orientation){
            case ScreenStyleInfo.LANDSCAPE:  // 横屏 相关类型的
                containerView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_landscape, this, false);
                showLandscape();
                break;
            case ScreenStyleInfo.PORTRAIT: // 竖屏 相关类型的
                containerView = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.layout_portrait, this, false);
                showPortrait();
                break;
            default:  // 默认全屏展示
                containerView = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.layout_landscape, this, false);
                showLandscape();
                break;
        }

        containerView.setBackgroundColor(Color.RED);
        // 每次更新UI，都remove掉所有的子View，并增加新的子View进来
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        addView(containerView, params);
        addView(containerView);
        containerView.postInvalidate();
        log("显示二屏->"+containerView.getChildCount());
    }

    // 显示竖屏
    private void showPortrait(){
        log("显示二屏->showPortrait");

        LinearLayout layout;

        // 上边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_up);
        createScreen("screen_up", mScreenStyleInfo.leftScreenGroupInfo, layout);

        // 中
        layout = (LinearLayout) containerView.findViewById(R.id.layout_middle);
        setLayoutParams(layout, 0);

        // 下边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_down);
        createScreen("screen_down", mScreenStyleInfo.rightScreenGroupInfo, layout);
    }


    // 显示横屏
    private void showLandscape(){
        log("显示二屏->showLandscape");

        LinearLayout layout;

        // 左边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_left);
        createScreen("screen_left", mScreenStyleInfo.leftScreenGroupInfo, layout);

        // 中
        layout = (LinearLayout) containerView.findViewById(R.id.layout_middle);
        setLayoutParams(layout, 0);

        // 右边 单屏展示
        layout = (LinearLayout) containerView.findViewById(R.id.layout_right);
        createScreen("screen_right", mScreenStyleInfo.rightScreenGroupInfo, layout);
    }

}
