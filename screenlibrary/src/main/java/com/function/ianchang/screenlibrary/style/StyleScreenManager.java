package com.function.ianchang.screenlibrary.style;

import android.support.v4.app.Fragment;

import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;
import com.function.ianchang.screenlibrary.style.layout.StyleScreen01Fragment;
import com.function.ianchang.screenlibrary.style.widget.ImageFragment;
import com.function.ianchang.screenlibrary.style.widget.TextFragment;
import com.function.ianchang.screenlibrary.style.widget.VideoFragment;

/**
 * Created by ianchang on 2017/11/1.
 */

public class StyleScreenManager {

    public static Fragment createScreen(ScreenGroupInfo info){
        Fragment fragment;

        if (info == null) return null;

        int styleType = info.styleType;

        switch (styleType) {
            case 1: // 样式1 图片
                fragment = ImageFragment.create(info);
                break;
            case 2: // 样式2 视频
                fragment = VideoFragment.create(info);
                break;
            case 3: // 样式3 文本
                fragment = TextFragment.create(info);
                break;
            case 4: // 样式4 图片、视频、文本
                fragment = StyleScreen01Fragment.create(info);
                break;
            default:
                fragment = ImageFragment.create(info);
                break;
        }
        return fragment;

    }
}
