/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  FFmpegUtils
 * Created by  ianchang on 2018-01-25 16:28:01
 * Last modify date   2018-01-25 14:12:34
 */

package com.function.ianchang.libffmpeg;

import android.util.Log;

/**
 * Created by ianchang on 2018/1/24.
 */

public class FFmpegUtils {

    static {
        try {
            System.loadLibrary("MyFFmpeg");
            Log.d("TAG", "load library success");
        }catch (Exception e){
            Log.d("TAG", "load library failure");
        }
    }

    public FFmpegUtils(){

   }
    public native static String testFFmpeg(String file);


    /**
     * 音视频解码播放
     * @param path
     * @param view
     */
    public native static void paly(String path,Object view);
}
