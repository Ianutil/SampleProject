package com.function.ianchang.screenlibrary.style;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by ianchang on 2017/11/3.
 */

public class MarqueeText extends AppCompatTextView {


    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
//        自定义设置让focusable为true
//        这个方法相当于在layout中
//        android:focusable="true"
//        android:focusableInTouchMode="true"
    }
}