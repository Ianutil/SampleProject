/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  DetectorManager
 * Created by  ianchang on 2018-08-08 18:25:06
 * Last modify date   2018-08-08 18:25:06
 */

package com.function.ianchang.simplemvp.detector;

/**
 * Created by ianchang on 2018/8/8.
 */

public class DetectorManager {


    //初始化模型
    public native static int initModel(String path);

    //人脸检测
    public native static String landMarks(long input,long output);

}
