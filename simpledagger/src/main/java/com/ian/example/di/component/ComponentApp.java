/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ComponentApp
 * Created by  ianchang on 2018-07-06 15:21:12
 * Last modify date   2018-07-06 15:20:02
 */

package com.ian.example.di.component;

import android.app.Application;
import android.content.Context;

import com.ian.example.MyApp;
import com.ian.example.di.modules.ModuleApp;
import com.ian.example.tools.LogUtils;
import com.ian.example.tools.ToastUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ianchang on 2018/7/6.
 */

@Singleton
@Component(modules = {ModuleApp.class})
public interface ComponentApp {

    Context getContext();

    ToastUtils getToast();

    LogUtils getLog();

}

