/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ServiceMedia
 * Created by  ianchang on 2018-08-23 16:07:42
 * Last modify date   2018-08-17 14:16:35
 */

package com.ian.example.https.service;

import android.util.Log;

import com.ian.example.https.utils.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
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

        ServiceTrustManager trustManager = new ServiceTrustManager();
        SSLSocketFactory socketFactory = HttpsUtils.getSSLSocketFactory(trustManager);

        // 加载外部证书
//        Buffer buffer = new Buffer().writeUtf8(HttpsUtils.CER_12306);
//        SSLSocketFactory socketFactory = HttpsUtils.getSSLSocketFactory(new InputStream[]{buffer.inputStream()});

        mHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(this)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .sslSocketFactory(socketFactory, trustManager)
                .hostnameVerifier(new ServiceHostnameVerifier())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .baseUrl(SERVICE_URL)
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
        builder.addHeader("Content-Type", "application/json; charset=UTF-8");
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
    public Observable<String> getToken(@Query("appKey") String appKey, @Query("appSecret") String appSecret) {
        return mServiceAPI.getToken(appKey, appSecret);
    }

}
