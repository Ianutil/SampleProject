/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ToastUtils
 * Created by  ianchang on 2018-07-06 15:49:01
 * Last modify date   2018-07-06 15:49:00
 */

package com.ian.example.tools;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by ianchang on 2018/7/6.
 */

public class ToastUtils {


    public Context mContext;

    @Inject
    public ToastUtils(Context context){
        this.mContext = context;
    }

    public void showToast(String msg){
        if (TextUtils.isEmpty(msg)){
            return;
        }

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
