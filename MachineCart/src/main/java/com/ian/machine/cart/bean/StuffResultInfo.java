/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StuffResultInfo
 * Created by  ianchang on 2018-08-21 18:16:02
 * Last modify date   2018-08-17 15:24:41
 */

package com.ian.machine.cart.bean;

import java.util.List;

/**
 * Created by ianchang on 2018/8/17.
 */

public class StuffResultInfo {



    public List<StuffInfo> cart;
    public List<StuffInfo> shelf;
    public String state;

    @Override
    public String toString() {
        return "StuffResultInfo{" +
                "cart=" + cart +
                ", shelf=" + shelf +
                ", state='" + state + '\'' +
                '}';
    }
}
