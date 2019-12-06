/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BlApiManager
 * Created by  ianchang on 2018-01-30 14:53:14
 * Last modify date   2018-01-19 17:41:05
 */

package com.function.ianchang.simplemvp.service;

import com.function.ianchang.simplemvp.utils.SignUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by martin.du on 2017/9/21.
 */

public class BlApiManager implements BlApi {

    private static BlApiManager mManager;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;
    private BlApi mApi;

    private Interceptor signInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();

            RequestBody originBody = originRequest.body();

            //add sign information
            if (originBody instanceof FormBody) {
                FormBody fb = ((FormBody) originBody);
                final int size = fb.size();
                if (size > 0) {

                    Map<String, String> nameValues = new HashMap<>();
                    List<String> names = new ArrayList<>(size);

                    for (int i = 0; i < size; i++) {
                        nameValues.put(fb.name(i), fb.value(i));
                        names.add(fb.name(i));
                    }

                    Collections.sort(names);         //sort key

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < size; i++) {        //generate form string
                        if (i > 0) sb.append('&');

                        String nameKey = names.get(i);
                        sb.append(nameKey).append('=').append(nameValues.get(nameKey));
                    }

                    String signValue = SignUtils.sign(sb.toString()).replace("\n", "");   //sign the string

                    FormBody.Builder formBuilder = new FormBody.Builder();

                    for (int i = 0; i < size; i++) {        //re add to originBody
                        formBuilder.add(fb.encodedName(i), fb.encodedValue(i));
                    }

                    // sing签名增加到Header信息中去了
//                    formBuilder.add("sign", signValue);     //add sign

                    RequestBody body = formBuilder.build();

                    Request.Builder newRequestBuilder = originRequest.newBuilder();

                    newRequestBuilder
                            .post(body)
                            .header("sign", signValue)
                            .header("Content-Length", Long.toString(body.contentLength()));     //change Content-Length header

                    Request newRequest = newRequestBuilder.build();

                    return chain.proceed(newRequest);
                }

            }

            return chain.proceed(chain.request());
        }
    };

    private BlApiManager() {

        mClient = new OkHttpClient.Builder().addNetworkInterceptor(signInterceptor).build();
        Retrofit.Builder builder = new Retrofit.Builder();
        mRetrofit = builder
                .baseUrl(END_POINT)
                .callFactory(mClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(BlApi.class);
    }

    public static BlApiManager getInstance() {
        if (mManager == null) {
            mManager = new BlApiManager();
        }
        return mManager;
    }


    @Override
    public Observable<String> registerDevice(@Field("code") String deviceId,
                                                         @Field("macAddress") String macAddress,
                                                         @Field("name") String deviceName,
                                                         @Field("resolutionX") int widthPixels,
                                                         @Field("resolutionY") int heightPixels,
                                                         @Field("osVersion") String version,
                                                         @Field("storeCode") String storeCode) {
        return mApi.registerDevice(deviceId, macAddress, deviceName, widthPixels, heightPixels, version, storeCode);
    }


}
