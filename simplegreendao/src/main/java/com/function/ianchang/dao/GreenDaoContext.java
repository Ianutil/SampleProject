/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  GreenDaoContext
 * Created by  ianchang on 2018-05-07 17:05:35
 * Last modify date   2018-05-07 17:05:34
 */

package com.function.ianchang.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by ianchang on 2018/5/7.
 */

public class GreenDaoContext extends ContextWrapper {
    private Context mContext;

    public GreenDaoContext(Context base) {
        super(base.getApplicationContext());

        this.mContext = getApplicationContext();
    }

    @Override
    public File getDatabasePath(String name) {

        Log.d("TAG", "getDatabasePath"+name);
        File root = getRootDir();
        File database = new File(root, name);

        if (!database.exists()){
            try {
                database.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return database;
//        return super.getDatabasePath(name);
    }

    private File getRootDir(){
        File dir = new File(Environment.getExternalStorageDirectory(), "BL-Electronicscreen/database");

        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    /*********
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     * @param name
     * @param mode
     * @param factory
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
//        return super.openOrCreateDatabase(name, mode, factory);

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        Log.d("TAG", "openOrCreateDatabase:"+name);

        return database;

    }
}
