/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MyApplication
 * Created by  ianchang on 2018-02-06 16:27:57
 * Last modify date   2018-02-06 16:27:51
 */

package com.example.ianchang.myapplication.bean;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by ianchang on 2018/2/6.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}
