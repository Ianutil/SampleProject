/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BlApi
 * Created by  ianchang on 2018-01-30 14:53:14
 * Last modify date   2018-01-30 14:48:40
 */

package com.function.ianchang.simplemvp.service;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by martin.du on 2017/9/21.
 */

public interface BlApi {

    // 测试Server   192.168.25.239
    String END_POINT = "http://182.254.131.39:9000/";
//    String END_POINT = "http://122.152.212.40:5439/";

    /**
     * 注册/更新设备信息
     *
     * @param deviceName 设备信息
     * @param macAddress mac地址
     * @param storeCode 门店code
     * @param orientationType 屏幕方向
     * @param ipAddress 设备ip地址
     * @param density 屏幕密度
     * @param widthPixels 屏幕宽
     * @param heightPixels 屏幕高
     * @param version 系统版本号
     * @return 设备ID信息
     */
    @FormUrlEncoded
    @POST("/appDevice/registerDevice")
    Observable<String> registerDevice(@Field("code") String deviceId,
                                          @Field("macAddress") String macAddress,
                                          @Field("name") String deviceName,
                                          @Field("resolutionX") int widthPixels,
                                          @Field("resolutionY") int heightPixels,
                                          @Field("osVersion") String version,
                                          @Field("storeCode") String storeCode);


}
