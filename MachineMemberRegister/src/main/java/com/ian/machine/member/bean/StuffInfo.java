/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  StuffInfo
 * Created by  ianchang on 2018-08-23 16:07:42
 * Last modify date   2018-08-17 15:24:41
 */

package com.ian.machine.member.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ianchang on 2018/8/16.
 */

public class StuffInfo {


    @SerializedName("goods_id")
    public String id;
    @SerializedName("goods_name")
    public String name;
    public float price;
    @SerializedName("amount")
    public int count;

    @SerializedName("pic")
    public String imageUrl;


    @Override
    public String toString() {
        return "StuffInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
