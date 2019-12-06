/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-01-25 15:59:09
 * Last modify date   2018-01-25 15:41:07
 */

package com.example.ianchang.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ianchang.myapplication.service.MyService;
import com.example.ianchang.myapplication.ui.TestUIActivity;
import com.example.ianchang.myapplication.vitamio.VideoBufferActivity;
import com.function.ianchang.apppush.TestPushActivity;
import com.function.ianchang.screenlibrary.TestScreenActivity;
import com.function.ianchang.simpleaidl.BLPackageInfo;
import com.function.ianchang.simpleaidl.BackgroundService;
import com.function.ianchang.simpleaidl.IBLPackageListener;
import com.function.ianchang.weblibrary.WebviewActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.activity.InitActivity;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private List<BannerInfo> datas;

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!LibsChecker.checkVitamioLibs(this)){
            boolean flag = Vitamio.initialize(this, getResources().getIdentifier("libarm", "raw", getPackageName()));

            Log.d("TAG", "初始化：LibsChecker.checkVitamioLibs："+flag);
        }

        datas = new ArrayList<>();
        BannerInfo info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482118&di=00d38522a4c45c90dbd6a85261e0ab30&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F50da81cb39dbb6fdf676feec0024ab18962b37ce.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=4653e59e3ab467c05d207e728d2990fa&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Ff9dcd100baa1cd11fca769eab012c8fcc2ce2de2.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=2fc558edcc855702188c9656c5d13907&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7dd98d1001e9390164e896f472ec54e737d19615.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=9af8552e7c24d86dacefc30bcb45a360&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7a899e510fb30f24f229f7c9c195d143ac4b034c.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=c8335f4be06819898dba9399a8d2f3b8&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F03087bf40ad162d91c0979d618dfa9ec8b13cd11.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=946b84a3f3a9416b8d81c3de07d8d3c4&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fc8177f3e6709c93df708153d963df8dcd0005405.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=3cdb9566171d29f300aa3192e2517438&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F1b4c510fd9f9d72a776a0ad7dd2a2834359bbbe9.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=25025c7d0a418d8ed8e2dcf30eb7726f&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7af40ad162d9f2d3b9673e25a0ec8a136227cc82.jpg";
        info.type = 1;
        datas.add(info);


        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=5aeddc6063f72f51d3e597e5516881a1&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b46f21fbe096b63f9a5459a05338744eaf8acbf.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=4b7d4c954b4c0d88c768f6653d77abf0&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Feac4b74543a9822607ec30ed8382b9014b90ebcf.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=63f9092ee3cad043073d87d5f33f6e1c&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb03533fa828ba61ed251d3e54834970a314e59b8.jpg";
        info.type = 1;
        datas.add(info);


        info = new BannerInfo();
        info.file = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=3c8a83a299b95c07bb178b2f461ee965&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F78310a55b319ebc472a06d538b26cffc1f17161b.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-17/941d4e3f4aa6b00e7d2713cf08e37f99.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-15/a6555e4aae18e24da344bdd7b33ddde0.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-16/0270343e6bcb48050a749cf10ae31407.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-17/7ebcf5e19d0d1d5fcc5b94645f1f46fd.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-17/a724d90ab06b5223dc6d3e9243f9ec7d.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-09-08/025c4cf374d3f67840edd180d2817068.mp4";
        info.type = 2;
        datas.add(info);

        viewPager = (ViewPager)findViewById(R.id.pager);



        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setSlowingScroller();

        ImageView mDefaultView = new ImageView(this);
        mDefaultView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int orientation = getResources().getConfiguration().orientation;
        if (ORIENTATION_LANDSCAPE == orientation) {
            Glide.with(this).load(R.mipmap.bailian_default_landscape).into(mDefaultView);
        } else {
            Glide.with(this).load(R.mipmap.bailian_default_portrait).into(mDefaultView);
        }

        // 设置上下文
        MediaPlayManager.shareInstance().setContext(this);

        BannerAdapter adapter = new BannerAdapter(datas, true, mDefaultView);
        adapter.setViewPager(viewPager);

//        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayManager.shareInstance().pauseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        MediaPlayManager.shareInstance().stopMediaPlayer();
        super.onDestroy();
    }

    private void setSlowingScroller() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    // 播放器
    public void startVitamio(View view){
        startActivity(new Intent(this, VideoBufferActivity.class));
    }

    // UI
    public void testUI(View view){
        startActivity(new Intent(this, TestUIActivity.class));
    }

    // UI
    public void testFragmentUI(View view){
        startActivity(new Intent(this, TestScreenActivity.class));
    }

    // UI
    public void testMarquee(View view){
        startActivity(new Intent(this, TestMarqueeTextActivity.class));
    }

    // UI
    public void testPush(View view){
        startActivity(new Intent(this, TestPushActivity.class));
    }

    // AIDL
    public void testAIDL(View view){

        Intent intent = new Intent();
        intent.setAction("com.ian.BackgroundService");
//        intent.setAction("com.ian.MyService");
        intent.setPackage("com.example.ianchang.myapplication");
//        intent.setPackage("com.function.ianchang.simpleaidl");
        startService(intent);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("TAG", "CLASS_NAME:"+name.getClassName());

                IBLPackageListener listener = IBLPackageListener.Stub.asInterface(service);

                try {
                    BLPackageInfo info = listener.getBLPackageInfo();
                    Log.d("TAG", "name:"+info.name);

                    listener.setBLPackageInfo(info);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("TAG", "CLASS_NAME:"+name.getClassName());

            }
        }, BIND_AUTO_CREATE);
    }

    // Web
    public void testWeb(View view){
        startActivity(new Intent(this, WebviewActivity.class));
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // state == 1的时表示正在滑动，state == 2的时表示滑动完毕了，state == 0的时表示什么都没做。
        if (state == 1){
            MediaPlayManager.shareInstance().pauseMediaPlayer();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }
}
