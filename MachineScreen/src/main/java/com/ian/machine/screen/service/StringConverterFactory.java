/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StringConverterFactory
 * Created by  ianchang on 2018-08-17 10:47:59
 * Last modify date   2017-12-11 10:54:27
 */

package com.ian.machine.screen.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by ianchang on 2018/8/17.
 */
public class StringConverterFactory extends Converter.Factory {

    private static final StringConverterFactory INSTANCE = new StringConverterFactory();

    public static StringConverterFactory create() {
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return StringConverter.INSTANCE;
        }
        return null;
    }
}
