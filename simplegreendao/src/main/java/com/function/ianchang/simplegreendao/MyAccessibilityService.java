/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MyAccessibilityService
 * Created by  ianchang on 2018-06-12 17:32:23
 * Last modify date   2018-06-12 17:32:23
 */

package com.function.ianchang.simplegreendao;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

public class MyAccessibilityService extends AccessibilityService {

    public MyAccessibilityService() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.e("TAG", "onServiceConnected-------------->");

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();

        Log.e("TAG", "onAccessibilityEvent-------------->"+rootNodeInfo.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AccessibilityWindowInfo windowInfo = rootNodeInfo.getWindow();
            Rect rect = new Rect();
            windowInfo.getBoundsInScreen(rect);
            String resourceName = rootNodeInfo.getViewIdResourceName();

            Log.e("TAG", "onAccessibilityEvent-------------->"+rect.toString() + resourceName);
        }else {

            int count = rootNodeInfo.getChildCount();
            AccessibilityNodeInfo infoCompat;
            for (int i = 0; i < count; i++){
                infoCompat = rootNodeInfo.getChild(i);
                Log.e("TAG", "----------->"+infoCompat.getClassName());
            }

        }

    }

    @Override
    public void onInterrupt() {

        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();

        Log.e("TAG", "onInterrupt-------------->"+rootNodeInfo.getPackageName());

//        for (rootNodeInfo.get)

    }


    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {

        AccessibilityNodeInfo info = super.getRootInActiveWindow();

        return info;
    }
}
