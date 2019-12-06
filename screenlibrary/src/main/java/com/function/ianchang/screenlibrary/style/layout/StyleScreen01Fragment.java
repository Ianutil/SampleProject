package com.function.ianchang.screenlibrary.style.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;
import com.function.ianchang.screenlibrary.style.BaseFragment;

/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class StyleScreen01Fragment extends BaseFragment {

    private View containerView;

    private ScreenGroupInfo mScreenGroupInfo;

    public static StyleScreen01Fragment create(ScreenGroupInfo screenGroupInfo){
        StyleScreen01Fragment fragment = new StyleScreen01Fragment();
        fragment.mScreenGroupInfo = screenGroupInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        // 展示内容名称：图片、视频、文本等组件名称或者代号
        containerView = inflater.inflate(R.layout.layout_style_04, container, false);

        // 设置布局方向
        if (mScreenGroupInfo != null){
            // 设置屏幕方向
            setScreenOrientation((LinearLayout) containerView, mScreenGroupInfo.orientation);
        }

        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mScreenGroupInfo == null || mScreenGroupInfo.compositionInfos == null
                || mScreenGroupInfo.compositionInfos.isEmpty()
                || mScreenGroupInfo.compositionInfos.size() < 3){
            return;
        }

        View layout;

        ScreenGroupInfo screenGroupInfo;
        LinearLayout linearLayout = (LinearLayout)containerView;
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++){
            layout = linearLayout.getChildAt(i);

            // 设置布局和大小
            screenGroupInfo = mScreenGroupInfo.compositionInfos.get(i);
            setLayoutParams(screenGroupInfo, layout);

            // 设置加什么类型的组件
            // 优化：不占屏的，就不再增加组件显示了
            if (screenGroupInfo.weight != 0){
                updateContentView(screenGroupInfo, layout.getId());
            }
        }




    }
}
