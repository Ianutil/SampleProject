package com.function.ianchang.screenlibrary.style.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.style.BaseFragment;
import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2017/10/30.
 * <p>
 * 单屏布局结构
 */
public class TextFragment extends BaseFragment {

    private View containerView;

    private ScreenGroupInfo mScreenGroupInfo;

    private List<String> mDatas;

    public static TextFragment create(ScreenGroupInfo screenGroupInfo) {
        TextFragment fragment = new TextFragment();
        fragment.mScreenGroupInfo = screenGroupInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        // 展示内容名称：图片、视频、文本等组件名称或者代号
        containerView = inflater.inflate(R.layout.layout_style_03, container, false);

        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mDatas = savedInstanceState.getStringArrayList("TEXT_DATAS");
            log("获取文本信息状态："+ mDatas);

        } else if (mScreenGroupInfo != null && mScreenGroupInfo.datas != null) {
            mDatas = (List<String>) mScreenGroupInfo.datas;
        } else {
            mDatas = new ArrayList<>();
        }


        TextView textView = view.findViewById(R.id.text_label);

        StringBuffer stringBuffer = new StringBuffer();
        for (String str : mDatas) {
            stringBuffer.append(str);
            stringBuffer.append("\n");
        }

        String str = stringBuffer.toString();
        if (TextUtils.isEmpty(str)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setText(str);
            log("--------->"+str);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("TEXT_DATAS", (ArrayList<String>) mDatas);

        log("文本信息保存状态："+ mDatas);
    }
}
