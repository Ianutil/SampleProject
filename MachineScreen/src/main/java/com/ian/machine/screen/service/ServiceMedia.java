/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ServiceMedia
 * Created by  ianchang on 2018-08-17 10:31:50
 * Last modify date   2018-08-17 10:31:49
 */

package com.ian.machine.screen.service;

import android.util.Log;

import com.ian.machine.screen.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

/**
 * Created by ianchang on 2018/8/17.
 */

public class ServiceMedia implements IServiceAPI, Interceptor {

    private static ServiceMedia instance;

    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;
    private IServiceAPI mServiceAPI;

    private ServiceMedia() {

        mHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(this)
                .addNetworkInterceptor(this)
                .readTimeout(6 * 1000, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(BuildConfig.serviceUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mServiceAPI = mRetrofit.create(IServiceAPI.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG", "intercept#url:"+chain.request().url());

        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "text/html; charset=UTF-8");
        builder.addHeader("Connection", "keep-alive");
        builder.addHeader("Accept", "*/*");
        builder.addHeader("Access-Control-Allow-Origin", "*");
        builder.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        builder.addHeader("Vary", "Accept-Encoding");

        return chain.proceed(builder.build());
    }

    public static ServiceMedia shareInstance() {

        if (instance == null) {

            synchronized (ServiceMedia.class) {
                if (instance == null) {
                    instance = new ServiceMedia();
                }
            }
        }

        return instance;
    }

    @Override
    public Observable<String> getStuffData(/***@Field("deviceId")**/ @Query("deviceId") String deviceId) {
        return mServiceAPI.getStuffData(deviceId);
    }

}
