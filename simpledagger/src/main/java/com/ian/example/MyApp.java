/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MyApp
 * Created by  ianchang on 2018-07-06 15:15:28
 * Last modify date   2018-07-06 15:15:27
 */

package com.ian.example;

import android.app.Application;

import com.ian.example.di.component.DaggerComponentApp;
import com.ian.example.di.modules.ModuleApp;
import com.ian.example.tools.LogUtils;

/**
 * Created by ianchang on 2018/7/6.
 */

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils logUtils = DaggerComponentApp.builder().moduleApp(new ModuleApp(this)).build().getLog();
        logUtils.i("******************************");
    }


}
