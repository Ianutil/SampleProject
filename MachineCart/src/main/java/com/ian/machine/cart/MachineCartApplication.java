/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MachineCartApplication
 * Created by  ianchang on 2018-08-21 10:18:54
 * Last modify date   2018-08-21 10:18:54
 */

package com.ian.machine.cart;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by ianchang on 2018/8/21.
 */

public class MachineCartApplication extends Application {

    private static MachineCartApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        // FIXME 初始化Application
        instance = this;
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

    }


    public static MachineCartApplication shareInstance(){
        return instance;
    }
}
