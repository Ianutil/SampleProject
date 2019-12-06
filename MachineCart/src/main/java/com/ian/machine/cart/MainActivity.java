/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-08-21 09:49:29
 * Last modify date   2018-08-21 09:49:28
 */

package com.ian.machine.cart;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ian.machine.cart.bean.StuffResultInfo;
import com.ian.machine.cart.cache.UserCenter;
import com.ian.machine.cart.service.ServiceMedia;
import com.ian.machine.cart.widget.BubbleLayout;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private BubbleLayout mBubbleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TAG", "MainActivity");

        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("TAG", "onConfigurationChanged");

    }

    public void initView(){
        mBubbleLayout = (BubbleLayout)findViewById(R.id.layout_bubble);

        ImageView imageView = (ImageView)findViewById(R.id.background_img);
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533027181762&di=6aba6bd63d62a8055b271ed79bef3b8d&imgtype=0&src=http%3A%2F%2Fpic.bizhi360.com%2Fbpic%2F41%2F5641.jpg";
        Glide.with(this).load(url).into(imageView);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mBubbleLayout != null){
            mBubbleLayout.startBubble();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBubbleLayout != null){
            mBubbleLayout.stopBubble();

        }
    }


    private void getData(String devices){

        Log.e("TAG", "getData");
        ServiceMedia.shareInstance()
                .getStuffData(devices)
                .map(new Function<String, StuffResultInfo>() {

                    @Override
                    public StuffResultInfo apply(@NonNull String s) throws Exception {

                        Log.e("TAG", "Response:"+s + "  --->"+Thread.currentThread().getName());

                        StuffResultInfo info = new Gson().fromJson(s, StuffResultInfo.class);
                        return info;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StuffResultInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StuffResultInfo data) {
                Log.e("TAG", "Response:"+data + "  --->"+Thread.currentThread().getName());

                UserCenter.shareInstance().saveStuff(data);

//                FragmentManager fm = getChildFragmentManager();
//                BaseFragment fragment = (CartFragment)fm.findFragmentByTag("CartFragment");
//                fragment.updateView(data);
//
//                fragment = (WarehouseFragment)fm.findFragmentByTag("WarehouseFragment");
//                fragment.updateView(data);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

//                handler.sendEmptyMessageAtTime(0, 1000 * 30);
            }
        });
    }
}
