/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StringConverter
 * Created by  ianchang on 2018-08-17 10:47:59
 * Last modify date   2017-12-11 10:54:27
 */

package com.ian.machine.screen.service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
/**
 * Created by ianchang on 2018/8/17.
 */
public class StringConverter implements Converter<ResponseBody, String> {

    public static final StringConverter INSTANCE = new StringConverter();

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
