package com.function.ianchang.screenlibrary.style.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.bean.ScreenStyleInfo;
import com.function.ianchang.screenlibrary.style.BaseFragment;

/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class StyleScreenFragment extends BaseFragment {

    private ScreenStyleInfo mScreenStyleInfo;
    private View containerView;

    // 屏幕
    private LinearLayout mLeftScreen, mMiddleScreen, mRightScreen;

    public static StyleScreenFragment create(ScreenStyleInfo mScreenStyleInfo){
        StyleScreenFragment fragment = new StyleScreenFragment();
        fragment.mScreenStyleInfo = mScreenStyleInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("_____________onCreateView___________");
        containerView = inflater.inflate(R.layout.layout_landscape, null, false);
        initView();
        return containerView;
    }


    private void initView(){
        if (mScreenStyleInfo == null) return;

        updateContentView(mScreenStyleInfo);
    }

    /********
     *
     * @param screenStyleInfo
     */
    public void updateContentView(ScreenStyleInfo screenStyleInfo){

        // 设置屏幕方向
        setScreenOrientation((LinearLayout) containerView, screenStyleInfo.orientation);

        // 上边/左边屏
        mLeftScreen = (LinearLayout) containerView.findViewById(R.id.layout_left);
        setLayoutParams(screenStyleInfo.leftScreenGroupInfo, mLeftScreen);
        //  加入组件
        updateContentView(screenStyleInfo.leftScreenGroupInfo, R.id.layout_left);

        // 中间屏
        mMiddleScreen = (LinearLayout) containerView.findViewById(R.id.layout_middle);
        setLayoutParams(screenStyleInfo.middleScreenGroupInfo, mMiddleScreen);
        // 中间 加入组件
        updateContentView(screenStyleInfo.middleScreenGroupInfo, R.id.layout_middle);

        // 下面/右边屏
        mRightScreen = (LinearLayout) containerView.findViewById(R.id.layout_right);
        setLayoutParams(screenStyleInfo.rightScreenGroupInfo, mRightScreen);
        // 下面  加入组件
        updateContentView(screenStyleInfo.rightScreenGroupInfo, R.id.layout_right);
    }

    public LinearLayout getLeftScreen(){
        return mLeftScreen;
    }

    public LinearLayout getMiddleScreen() {
        return mMiddleScreen;
    }

    public LinearLayout getRightScreen() {
        return mRightScreen;
    }
}
