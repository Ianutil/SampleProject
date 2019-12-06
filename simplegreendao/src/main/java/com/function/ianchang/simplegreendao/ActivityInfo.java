/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ActivityInfo
 * Created by  ianchang on 2018-02-24 10:53:03
 * Last modify date   2018-02-24 10:53:03
 */

package com.function.ianchang.simplegreendao;

import android.graphics.pdf.PdfRenderer;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.generator.DaoGenerator;

/**
 * Created by ianchang on 2018/2/24.
 */

@Entity
public class ActivityInfo {

    @Unique
    public String activityCode;

    public String name;

    @Generated(hash = 725370414)
    public ActivityInfo(String activityCode, String name, List<ScheduleInfo> scheduleInfos,
            List<PageInfo> pageInfos) {
        this.activityCode = activityCode;
        this.name = name;
        this.scheduleInfos = scheduleInfos;
        this.pageInfos = pageInfos;
    }

    @Generated(hash = 1559071709)
    public ActivityInfo() {
    }

    @Convert(converter = ScheduleInfoConverter.class ,columnType = String.class)
    public List<ScheduleInfo> scheduleInfos;

    @Convert(converter = PageInfoConverter.class ,columnType = String.class)
    public List<PageInfo> pageInfos;

    public static class ScheduleInfoConverter implements PropertyConverter<List<ScheduleInfo>, String> {

        @Override
        public List<ScheduleInfo> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }

            Type type = new TypeToken<List<ScheduleInfo>>(){}.getType();
            List<ScheduleInfo> data = new Gson().fromJson(databaseValue, type);
            return data;
        }

        @Override
        public String convertToDatabaseValue(List<ScheduleInfo> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }

    public static class PageInfoConverter implements PropertyConverter<List<PageInfo>, String> {

        @Override
        public List<PageInfo> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }

            Type type = new TypeToken<List<PageInfo>>(){}.getType();
            List<PageInfo> data = new Gson().fromJson(databaseValue, type);
            return data;
        }

        @Override
        public String convertToDatabaseValue(List<PageInfo> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }

    @Override
    public String toString() {
        return "ActivityInfo{" +
                "activityCode='" + activityCode + '\'' +
                ", name='" + name + '\'' +
                ", scheduleInfos=" + scheduleInfos +
                ", pageInfos=" + pageInfos +
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

    public List<ScheduleInfo> getScheduleInfos() {
        return this.scheduleInfos;
    }

    public void setScheduleInfos(List<ScheduleInfo> scheduleInfos) {
        this.scheduleInfos = scheduleInfos;
    }

    public List<PageInfo> getPageInfos() {
        return this.pageInfos;
    }

    public void setPageInfos(List<PageInfo> pageInfos) {
        this.pageInfos = pageInfos;
    }
}
