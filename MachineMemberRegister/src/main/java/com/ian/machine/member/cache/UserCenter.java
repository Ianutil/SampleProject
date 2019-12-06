/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  UserCenter
 * Created by  ianchang on 2018-08-23 16:07:42
 * Last modify date   2018-08-17 15:24:41
 */

package com.ian.machine.member.cache;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.ian.machine.member.MachineMemberApplication;
import com.ian.machine.member.bean.StuffResultInfo;

import java.lang.reflect.Type;

/**
 * Created by ianchang on 2018/8/17.
 */

public class UserCenter {

    private static UserCenter instance;

    private SharedPreferences mPreferences;

    private UserCenter(){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MachineMemberApplication.shareInstance());
    }

    public static UserCenter shareInstance(){

        if (instance == null){
            synchronized (UserCenter.class){
                if (instance == null){
                    instance = new UserCenter();
                }
            }
        }
        return instance;
    }


    public void saveStuff(StuffResultInfo info){
        saveObject(info, info.getClass().getSimpleName());
    }

    public StuffResultInfo getStuffResultInfo(){
        Object obj = getObject(StuffResultInfo.class, StuffResultInfo.class.getSimpleName());

        StuffResultInfo info;
        if (obj == null){
            info = null;
        }else {
            info = (StuffResultInfo)obj;
        }

        return info;
    }

    private void saveObject(Object obj, String key) {
        try {
            SharedPreferences.Editor editor = mPreferences.edit();
            if (obj == null){
                editor.putString(key, null);
            }else {
                String json = new Gson().toJson(obj);
                json = Base64.encodeToString(json.getBytes(), Base64.DEFAULT);
                editor.putString(key, json);
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Object getObject(Type type, String key) {
        try {
            String json = mPreferences.getString(key, "");
            if (TextUtils.isEmpty(json)) {
                return null;
            }

            json = new String(Base64.decode(json.getBytes(), Base64.DEFAULT), "UTF-8");

            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
