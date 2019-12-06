package com.example.ianchang.myapplication.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ianchang.myapplication.R;
import com.example.ianchang.myapplication.bean.ScreenGroupInfo;
import com.example.ianchang.myapplication.bean.ScreenStyleInfo;


/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class ScreenLayout extends BaseScreenLayout {

    private ScreenGroupInfo mScreenGroupInfo;
    private View containerView;

    public ScreenLayout(Context context) {
        super(context);
        initView();
    }

    public ScreenLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScreenLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        containerView = LayoutInflater.from(getContext()).inflate(R.layout.layout_portrait, null, true);
//        addView(containerView);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(containerView, params);
    }


    @Override
    public void updateContentView(ScreenStyleInfo mScreenStyleInfo) {
        super.updateContentView(mScreenStyleInfo);

        updateContentView(mScreenStyleInfo.screenGroupInfo);
    }

    /********
     *
     * @param mScreenGroupInfo
     */
    public void updateContentView(ScreenGroupInfo mScreenGroupInfo){
        this.mScreenGroupInfo = mScreenGroupInfo;

        LinearLayout layout;

        // 上
        layout = (LinearLayout) containerView.findViewById(R.id.layout_up);
        if (mScreenGroupInfo.upCompositionInfo == null || mScreenGroupInfo.upCompositionInfo.weight == 0){
            setLayoutParams(layout, 0);
        }else {

            layout.setTag("content_top");
            setLayoutParams(layout, mScreenGroupInfo.upCompositionInfo.weight);

            // 上边 加入组件
            updateContentView(mScreenGroupInfo.upCompositionInfo, layout);
        }


        // 中间内容
        if (mScreenGroupInfo.middleCompositionInfo != null){

            layout = (LinearLayout) containerView.findViewById(R.id.layout_middle);

           if(mScreenGroupInfo.middleCompositionInfo.weight == 0){
                setLayoutParams(layout, 1);
            }else {
                setLayoutParams(layout, mScreenGroupInfo.middleCompositionInfo.weight);
            }

            layout.setTag("content_middle");
            // 中间 加入组件
            updateContentView(mScreenGroupInfo.middleCompositionInfo, layout);
        }

        // 下
        layout = (LinearLayout) containerView.findViewById(R.id.layout_down);
        if (mScreenGroupInfo.downCompositionInfo == null || mScreenGroupInfo.downCompositionInfo.weight == 0){
            setLayoutParams(layout, 0);
        }else {
            setLayoutParams(layout, mScreenGroupInfo.downCompositionInfo.weight);

            layout.setTag("content_down");

            // 下面  加入组件
            updateContentView(mScreenGroupInfo.downCompositionInfo, layout);
        }

    }
}
