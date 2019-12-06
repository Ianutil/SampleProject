/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ModuleApp
 * Created by  ianchang on 2018-07-06 15:22:24
 * Last modify date   2018-07-06 15:22:24
 */

package com.ian.example.di.modules;

import android.content.Context;

import com.ian.example.tools.LogUtils;
import com.ian.example.tools.ToastUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ianchang on 2018/7/6.
 */

@Singleton
@Module
public class ModuleApp {

    private Context mContext;
    private boolean isDebug;

    public ModuleApp(Context context){
        this.mContext = context;
    }


    @Singleton
    @Provides
    public Context provideContext(){

        return mContext;
    }

    @Singleton
    @Provides
    public ToastUtils getToast(){

        return new ToastUtils(mContext);
    }

    @Singleton
    @Provides
    public LogUtils getLog(){
        return new LogUtils(mContext, isDebug);
    }

}
