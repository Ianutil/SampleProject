/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BaseFragment
 * Created by  ianchang on 2018-08-17 15:22:28
 * Last modify date   2018-08-17 15:22:07
 */

package com.ian.machine.screen.base;

/**
 * Created by ianchang on 2018/8/17.
 */

public interface IScreenResponse {

    void onSuccess(Object data, int code);

    void onFailure(Object data, String message, int code);
}
