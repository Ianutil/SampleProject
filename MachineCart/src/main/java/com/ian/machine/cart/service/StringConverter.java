/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StringConverter
 * Created by  ianchang on 2018-08-21 17:58:58
 * Last modify date   2018-08-17 10:48:25
 */

package com.ian.machine.cart.service;

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
