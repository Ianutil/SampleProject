/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MenuRecyclerView
 * Created by  ianchang on 2018-04-28 09:38:53
 * Last modify date   2018-04-28 09:29:59
 */

package com.ian.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ian.widget.view.RefreshRecyclerView;

/**
 * Created by Ian on 2018/4/28.
 * <p>
 * 带左/右滑动菜单的列表
 */
public class MenuRecyclerView extends RefreshRecyclerView {

    public MenuRecyclerView(Context context) {
        super(context);
    }

    public MenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        // 手指按下的时候，如果有开启的菜单，只要手指不是落在该Item上，则关闭菜单, 并且不分发事件。
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            View openItem = getOpenItem();
            if (openItem != null && openItem != getTouchItem(x, y)) {
                MenuItemLayout layout = findMenuItemLayout(openItem);
                if (layout != null) {
                    layout.closeMenu();
                    return false;
                }
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            // FIXME: 2017/3/22 不知道怎么解决多点触控导致可以同时打开多个菜单的bug，先暂时禁止多点触控
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取按下位置的Item
     */
    @Nullable
    private View getTouchItem(int x, int y) {
        Rect frame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * 找到当前屏幕中开启的的Item
     */
    @Nullable
    private View getOpenItem() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            MenuItemLayout MenuItemLayout = findMenuItemLayout(getChildAt(i));
            if (MenuItemLayout != null && MenuItemLayout.isOpen()) {
                return getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 获取该View
     */
    @Nullable
    private MenuItemLayout findMenuItemLayout(View view) {
        if (view instanceof MenuItemLayout) {
            return (MenuItemLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                MenuItemLayout layout = findMenuItemLayout(group.getChildAt(i));
                if (layout != null) {
                    return layout;
                }
            }
        }
        return null;
    }

}
