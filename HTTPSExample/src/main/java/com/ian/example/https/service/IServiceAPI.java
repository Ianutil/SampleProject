/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  IServiceAPI
 * Created by  ianchang on 2018-08-23 16:07:42
 * Last modify date   2018-08-17 14:16:35
 */

package com.ian.example.https.service;


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


    String SERVICE_URL = "https://open.leshui365.com";


    @GET("/getToken")
    Observable<String> getToken(@Query("appKey") String appKey, @Query("appSecret") String appSecret);

}
