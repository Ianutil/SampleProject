/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ScreenApplication
 * Created by  ianchang on 2018-08-17 15:18:36
 * Last modify date   2018-08-17 15:18:35
 */

package com.ian.machine.screen;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by ianchang on 2018/8/17.
 */

public class ScreenApplication extends Application {

    private static ScreenApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        // FIXME 初始化Application
        instance = this;
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

    }


    public static ScreenApplication shareInstance(){
        return instance;
    }
}
