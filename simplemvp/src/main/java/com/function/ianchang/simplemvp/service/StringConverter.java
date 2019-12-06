/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StringConverter
 * Created by  ianchang on 2018-01-30 14:53:14
 * Last modify date   2017-12-11 10:54:27
 */

package com.function.ianchang.simplemvp.service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by martin.du on 2017/10/23.
 */

public class StringConverter implements Converter<ResponseBody, String> {

    public static final StringConverter INSTANCE = new StringConverter();

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
