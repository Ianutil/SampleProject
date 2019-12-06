/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  HeaderScrollView
 * Created by  ianchang on 2018-05-04 13:50:22
 * Last modify date   2018-05-04 13:50:22
 */

package com.ian.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ianchang on 2018/5/4.
 */

public class HeaderScrollView extends ScrollView implements Pullable{

    public HeaderScrollView(Context context) {
        super(context);
    }

    public HeaderScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {

        int childHeight = 0;
        int count = this.getChildCount();
        if (count > 0){
            childHeight = getChildAt(0).getHeight();
        }
        if (getScrollY() >= (childHeight - getMeasuredHeight())){
            return true;
        }

        return false;
    }


}
