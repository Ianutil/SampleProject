/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  LogUtil
 * Created by  ianchang on 2018-01-25 15:58:23
 * Last modify date   2018-01-25 15:57:35
 */

package com.example.ianchang.myapplication;

import android.nfc.Tag;
import android.util.Log;

/**
 * Created by ianchang on 2017/10/19.
 */

public class LogUtil {

    public static final String TAG = "TAG";

    public static boolean isDebug = true;


    /********
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg){
        if (isDebug){
            Log.e(tag, msg);
        }
    }


    /******
     *
     * @param msg
     */
    public static void e(String msg){
        e(TAG, msg);
    }

}
