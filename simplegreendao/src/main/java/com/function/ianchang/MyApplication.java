/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MyApplication
 * Created by  ianchang on 2018-05-07 17:33:48
 * Last modify date   2018-05-07 17:33:48
 */

package com.function.ianchang;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/5/7.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks{

    public static List<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("TAG", "---------------"+activity.getClass().getSimpleName());
        activities.add(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("TAG", "---------------"+activity.getClass().getSimpleName());

        activities.remove(activity);
    }
}
