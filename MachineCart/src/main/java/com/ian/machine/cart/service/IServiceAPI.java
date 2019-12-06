/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  IServiceAPI
 * Created by  ianchang on 2018-08-21 17:58:58
 * Last modify date   2018-08-17 14:16:35
 */

package com.ian.machine.cart.service;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ianchang on 2018/8/17.
 */

public interface IServiceAPI {


//    @POST("/istore")
//    @FormUrlEncoded
    @GET("/istore")
    Observable<String> getStuffData(/***@Field("deviceId")**/@Query("deviceId") String deviceId);

    @GET
    Observable<String> testAPP(@Url String url, @Query("deviceId") String deviceId);
}
