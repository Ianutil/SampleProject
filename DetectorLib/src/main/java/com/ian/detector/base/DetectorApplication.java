/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MachineMemberApplication
 * Created by  ianchang on 2018-08-23 16:43:13
 * Last modify date   2018-08-21 10:20:32
 */

package com.ian.detector.base;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by ianchang on 2018/8/21.
 */

public class DetectorApplication extends MultiDexApplication {

    private static DetectorApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        // FIXME 初始化Application
        instance = this;
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

    }




    public static DetectorApplication shareInstance(){
        return instance;
    }
}
