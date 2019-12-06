/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  IServiceAPI
 * Created by  ianchang on 2018-08-17 10:25:41
 * Last modify date   2018-08-17 10:25:41
 */

package com.ian.machine.screen.service;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ianchang on 2018/8/17.
 */

public interface IServiceAPI {


//    @POST("/istore")
//    @FormUrlEncoded
    @GET("/istore")
    Observable<String> getStuffData(/***@Field("deviceId")**/@Query("deviceId") String deviceId);
}
