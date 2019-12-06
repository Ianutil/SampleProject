package com.function.ianchang.screenlibrary;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.function.ianchang.screenlibrary.bean.BannerInfo;
import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;
import com.function.ianchang.screenlibrary.bean.ScreenStyleInfo;
import com.function.ianchang.screenlibrary.style.MarqueeText;
import com.function.ianchang.screenlibrary.style.layout.StyleScreenFragment;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

/**
 * Created by ianchang on 2017/11/1.
 */
public class TestScreenActivity extends FragmentActivity {
    private StyleScreenFragment fragment;
    private ScreenStyleInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_screen);

        // 设备配置信息
        if (savedInstanceState == null) {
            info = new ScreenStyleInfo();
            info.orientation = 1; // "LANDSCAPE";

            info.middleScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
            info.middleScreenGroupInfo.styleType = 1; // 显示图片
            info.middleScreenGroupInfo.datas = getImageData();

            Log.d("TAG", "Activity：onCreate中创建一个设备信息");
        } else {

            info = (ScreenStyleInfo) savedInstanceState.getSerializable("info");

            Log.d("TAG", "从当前Activity：onCreate中获取设备信息");
        }


        fragment = StyleScreenFragment.create(info);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_container, fragment);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("info", info);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        Log.d("TAG", "onConfigurationChanged:" + orientation);
        // 横屏
        if (orientation == SCREEN_ORIENTATION_LANDSCAPE) {
            info.orientation = 1; // "LANDSCAPE";
        } else if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
            // 竖屏
            info.orientation = 2; // "PORTRAIT";
        }

        fragment.updateContentView(info);
    }

    // 更新UI
    public void updateView(View view) {

        // 左边屏
        EditText editText = findViewById(R.id.edit_left);
        String weight = editText.getText().toString();

        editText = findViewById(R.id.edit_left_style);
        ;
        String style = editText.getText().toString();

        if (info.leftScreenGroupInfo == null) {
            info.leftScreenGroupInfo = new ScreenGroupInfo();
        }

        setScreenGroupInfo(info.leftScreenGroupInfo, weight, style);


        // 中间屏
        editText = findViewById(R.id.edit_middle);
        weight = editText.getText().toString();

        editText = findViewById(R.id.edit_middle_style);
        style = editText.getText().toString();

        if (info.middleScreenGroupInfo == null) {
            info.middleScreenGroupInfo = new ScreenGroupInfo();
        }

        setScreenGroupInfo(info.middleScreenGroupInfo, weight, style);

        // 右边屏
        editText = findViewById(R.id.edit_right);
        weight = editText.getText().toString();


        editText = findViewById(R.id.edit_right_style);
        style = editText.getText().toString();

        if (info.rightScreenGroupInfo == null) {
            info.rightScreenGroupInfo = new ScreenGroupInfo();
        }

        setScreenGroupInfo(info.rightScreenGroupInfo, weight, style);

        fragment.updateContentView(info);

        FrameLayout frameLayout = findViewById(R.id.layout_container);

        if(frameLayout.findViewWithTag("lyric_leave_summer") == null){
            MarqueeText text = new MarqueeText(this);
            text.setTag("lyric_leave_summer");
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            frameLayout.addView(text, params);

            text.setSelected(true);
            text.setMarqueeRepeatLimit(-1);
            text.setText(getString(R.string.lyric_leave_summer));
            text.setTextColor(Color.WHITE);
            text.setSingleLine();
            text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            text.setFocusableInTouchMode(true);
        }
    }


    // 更新屏幕模式
    public void updateMode(View view) {
        info.orientation = info.orientation == 1 ? 2 : 1;
        Button btn = (Button) view;
        if (info.orientation == 1) {
            btn.setText("横屏模式");
        } else {
            btn.setText("竖屏模式");
        }

        fragment.updateContentView(info);
    }

    // 增加数据
    public void showToolbar(View view) {


        View layout;

        // 屏
        layout = findViewById(R.id.layout_screen);
        layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

        // 样式
        layout = findViewById(R.id.layout_style);
        layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

        // 增加数据
        layout = findViewById(R.id.layout_add_data);
        layout.setVisibility(layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    // 增加数据
    public void addLeftData(View view) {
        if (info.leftScreenGroupInfo == null) {
            info.leftScreenGroupInfo = new ScreenGroupInfo();
        }

        // 判断是否为配置样式4
        setStyle4GroupInfo(info.leftScreenGroupInfo);


        String json = new Gson().toJson(info);
        fragment.log("--------------------->"+json);

        // 更新UI
        fragment.updateContentView(info);
    }

    public void addRightData(View view) {
        if (info.rightScreenGroupInfo == null) {
            info.rightScreenGroupInfo = new ScreenGroupInfo();
        }

        // 判断是否为配置样式4
        setStyle4GroupInfo(info.rightScreenGroupInfo);

        // 更新UI
        fragment.updateContentView(info);
    }

    public void addMiddleData(View view) {
        if (info.middleScreenGroupInfo == null) {
            info.middleScreenGroupInfo = new ScreenGroupInfo();
        }

        // 判断是否为配置样式4
        setStyle4GroupInfo(info.middleScreenGroupInfo);

        // 更新UI
        fragment.updateContentView(info);
    }


    public void setStyle4GroupInfo(ScreenGroupInfo info) {

        int styleType = info.styleType;

        // 当多布局时
        if (styleType != 4) return;

        if (info.compositionInfos == null) {
            info.compositionInfos = new ArrayList<>();

            info.compositionInfos.add(new ScreenGroupInfo());
            info.compositionInfos.add(new ScreenGroupInfo());
            info.compositionInfos.add(new ScreenGroupInfo());
        }

        EditText editText;
        String weight;
        String style;


        editText = findViewById(R.id.edit_left);
        weight = editText.getText().toString();

        editText = findViewById(R.id.edit_left_style);
        style = editText.getText().toString();

        ScreenGroupInfo screenGroupInfo = info.compositionInfos.get(0);
        setScreenGroupInfo(screenGroupInfo, weight, style);

        editText = findViewById(R.id.edit_middle);
        weight = editText.getText().toString();

        editText = findViewById(R.id.edit_middle_style);

        style = editText.getText().toString();

        screenGroupInfo = info.compositionInfos.get(1);
        setScreenGroupInfo(screenGroupInfo, weight, style);

        editText = findViewById(R.id.edit_right);
        weight = editText.getText().toString();

        editText = findViewById(R.id.edit_right_style);

        style = editText.getText().toString();

        screenGroupInfo = info.compositionInfos.get(2);
        setScreenGroupInfo(screenGroupInfo, weight, style);

        int orientation = info.orientation;
        info.orientation = orientation == ScreenStyleInfo.LANDSCAPE ? ScreenStyleInfo.PORTRAIT : ScreenStyleInfo.LANDSCAPE;

    }

    /*******
     * 绑定数据
     * @param screenGroupInfo
     * @param weight
     * @param style
     */
    public void setScreenGroupInfo(ScreenGroupInfo screenGroupInfo, String weight, String style) {

        if (screenGroupInfo == null) return;

        if (TextUtils.isEmpty(weight)) {
            screenGroupInfo.weight = 0;
        } else {
            screenGroupInfo.weight = Float.valueOf(weight);
        }

        if (TextUtils.isEmpty(style)) {
            screenGroupInfo.styleType = 0;
        } else {
            screenGroupInfo.styleType = Integer.valueOf(style);
        }

        if (screenGroupInfo.compositionInfos == null) {
            screenGroupInfo.compositionInfos = new ArrayList<>();
        } else if (!screenGroupInfo.compositionInfos.isEmpty()) {
            // 清空组件列表
            screenGroupInfo.compositionInfos.clear();
        }

        ScreenGroupInfo screenCompositionInfo;

        // 根据需要显示的样式，进行配置相对应的数据
        switch (screenGroupInfo.styleType) {
            case 1: // 图片样式的数据
                screenGroupInfo.datas = getImageData();
                break;
            case 2: // 视频样式的数据
                screenGroupInfo.datas = getVidoData();
                break;
            case 3: // 文本样式
                screenGroupInfo.datas = getTextData();
                break;
            case 4: // 上面是图片、中间是视频、下是文本信息
                // 当没有组件时，创建一个
                screenCompositionInfo = new ScreenGroupInfo();
                screenCompositionInfo.styleType = 1;
                screenCompositionInfo.weight = 2;
                screenCompositionInfo.datas = getImageData();
                screenGroupInfo.compositionInfos.add(screenCompositionInfo);

                screenCompositionInfo = new ScreenGroupInfo();
                screenCompositionInfo.styleType = 2;
                screenCompositionInfo.weight = 3;
                screenCompositionInfo.datas = getVidoData();
                screenGroupInfo.compositionInfos.add(screenCompositionInfo);

                screenCompositionInfo = new ScreenGroupInfo();
                screenCompositionInfo.styleType = 3;
                screenCompositionInfo.weight = 3;
                screenCompositionInfo.datas = getTextData();
                screenGroupInfo.compositionInfos.add(screenCompositionInfo);

                // 我设置，1，2屏幕的布局方向
                screenGroupInfo.orientation = ScreenStyleInfo.PORTRAIT;
                break;
            default:
                screenGroupInfo.datas = getImageData();
                break;
        }

    }

    public List<String> getTextData() {
        ArrayList<String> datas = new ArrayList<>();
        datas.add("商品\t\t价格(元/斤)\t\t\t促销价格(元/斤)");

        for (int i = 0; i < 10; i++) {
            datas.add("香蕉\t\t\t\t10\t\t\t\t\t\t\t\t\t8.0");
        }

        return datas;
    }

    public List<BannerInfo> getImageData() {
        ArrayList<BannerInfo> datas = new ArrayList<>();
        BannerInfo info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482118&di=00d38522a4c45c90dbd6a85261e0ab30&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F50da81cb39dbb6fdf676feec0024ab18962b37ce.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=4653e59e3ab467c05d207e728d2990fa&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Ff9dcd100baa1cd11fca769eab012c8fcc2ce2de2.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=2fc558edcc855702188c9656c5d13907&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7dd98d1001e9390164e896f472ec54e737d19615.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=9af8552e7c24d86dacefc30bcb45a360&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7a899e510fb30f24f229f7c9c195d143ac4b034c.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=c8335f4be06819898dba9399a8d2f3b8&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F03087bf40ad162d91c0979d618dfa9ec8b13cd11.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=946b84a3f3a9416b8d81c3de07d8d3c4&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fc8177f3e6709c93df708153d963df8dcd0005405.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=3cdb9566171d29f300aa3192e2517438&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F1b4c510fd9f9d72a776a0ad7dd2a2834359bbbe9.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482117&di=25025c7d0a418d8ed8e2dcf30eb7726f&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F7af40ad162d9f2d3b9673e25a0ec8a136227cc82.jpg";
        info.type = 1;
        datas.add(info);


        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=5aeddc6063f72f51d3e597e5516881a1&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F0b46f21fbe096b63f9a5459a05338744eaf8acbf.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=4b7d4c954b4c0d88c768f6653d77abf0&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Feac4b74543a9822607ec30ed8382b9014b90ebcf.jpg";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=63f9092ee3cad043073d87d5f33f6e1c&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb03533fa828ba61ed251d3e54834970a314e59b8.jpg";
        info.type = 1;
        datas.add(info);


        info = new BannerInfo();
        info.file = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508316482116&di=3c8a83a299b95c07bb178b2f461ee965&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F78310a55b319ebc472a06d538b26cffc1f17161b.jpg";
        info.type = 1;
        datas.add(info);

        return datas;
    }


    public List<BannerInfo> getVidoData() {
        ArrayList<BannerInfo> datas = new ArrayList<>();

        BannerInfo info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-17/941d4e3f4aa6b00e7d2713cf08e37f99.mp4";
        info.type = 1;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-09-08/025c4cf374d3f67840edd180d2817068.mp4";
        info.type = 2;
        datas.add(info);

        info = new BannerInfo();
        info.file = "http://mp4.vjshi.com/2017-10-15/a6555e4aae18e24da344bdd7b33ddde0.mp4";
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
        return datas;
    }

}
