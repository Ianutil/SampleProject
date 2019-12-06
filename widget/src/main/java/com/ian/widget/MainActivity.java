/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-04-28 09:16:25
 * Last modify date   2018-04-28 09:16:25
 */

package com.ian.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void openMenu(View view){

        startActivity(new Intent(this, MenuActivity.class));
    }

    public void openScrollView(View view){

        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void openCircle(View view){

        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void uploadOrDownRefresh(View view){

        startActivity(new Intent(this, RefreshActivity.class));
    }

    public void refreshRecycleView(View view){

        startActivity(new Intent(this, RefreshRecycleActivity.class));
    }

    public void showViewPageAndRefreshRecycleView(View view){

        startActivity(new Intent(this, ViewPageAndRefreshRecycleActivity.class));
    }

    public void setWifiConnect(View view){

        Runtime mRuntime = Runtime.getRuntime();
        try {
            mRuntime.exec("su");
            mRuntime.exec("setprop service.adb.tcp.port 5555 ");
            mRuntime.exec("stop adbd ");
            mRuntime.exec("start adbd ");
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }
}
