/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  IScreenResponse
 * Created by  ianchang on 2018-08-23 16:07:42
 * Last modify date   2018-08-17 15:30:27
 */

package com.ian.machine.member.base;

/**
 * Created by ianchang on 2018/8/17.
 */

public interface IScreenResponse {

    void onSuccess(Object data, int code);

    void onFailure(Object data, String message, int code);
}
