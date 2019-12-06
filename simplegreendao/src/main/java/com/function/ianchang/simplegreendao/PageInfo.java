/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  PageInfo
 * Created by  ianchang on 2018-02-24 12:20:18
 * Last modify date   2018-02-24 12:20:18
 */

package com.function.ianchang.simplegreendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ianchang on 2018/2/24.
 */

@Entity
public class PageInfo {

    @NotNull
    public String activityCode;

    public String name;

    public long playTime;

    @Generated(hash = 399696863)
    public PageInfo(@NotNull String activityCode, String name, long playTime) {
        this.activityCode = activityCode;
        this.name = name;
        this.playTime = playTime;
    }

    @Generated(hash = 40275086)
    public PageInfo() {
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "activityCode='" + activityCode + '\'' +
                ", name='" + name + '\'' +
                ", playTime=" + playTime +
                '}';
    }

    public String getActivityCode() {
        return this.activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
}
