/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  LogUtils
 * Created by  ianchang on 2018-07-06 15:51:11
 * Last modify date   2018-07-06 15:51:10
 */

package com.ian.example.tools;

import android.content.Context;
import android.util.Log;

/**
 * Created by ianchang on 2018/7/6.
 */

public class LogUtils {


    private Context mContext;
    private String TAG;
    private boolean isDebug;

    public LogUtils(Context context, boolean isDebug){
        this.mContext = context;
        this.isDebug = isDebug;

        TAG = context.getClass().getSimpleName();
    }


    public void i(String msg){
        if (isDebug){
            Log.i(TAG, msg);
        }
    }
}
