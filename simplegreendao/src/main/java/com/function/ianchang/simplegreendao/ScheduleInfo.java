/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ScheduleInfo
 * Created by  ianchang on 2018-02-24 12:15:55
 * Last modify date   2018-02-24 12:15:55
 */

package com.function.ianchang.simplegreendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by ianchang on 2018/2/24.
 */

@Entity
public class ScheduleInfo {

    @Id
    private long id;
    @Unique
    @NotNull
//    public String startDate;
    public Date startDate;
    @NotNull
//    public String endDate;
    public Date endDate;
    @NotNull
    public String activityCode;

    @Override
    public String toString() {
        return "ScheduleInfo{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", activityCode='" + activityCode + '\'' +
                '}';
    }

    @Generated(hash = 1430333827)



    public ScheduleInfo(long id, @NotNull Date startDate, @NotNull Date endDate,
            @NotNull String activityCode) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityCode = activityCode;
    }
    @Generated(hash = 57358373)
    public ScheduleInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getActivityCode() {
        return this.activityCode;
    }
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

   
}
