package com.example.ianchang.myapplication.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.ianchang.myapplication.R;
import com.example.ianchang.myapplication.bean.ScreenCompositionInfo;
import com.example.ianchang.myapplication.bean.ScreenGroupInfo;
import com.example.ianchang.myapplication.bean.ScreenStyleInfo;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

/**
 * Created by ianchang on 2017/10/23.
 * <p>
 * <li>
 * 设备名称：
 * 分辨率密度、宽高：
 * MAC地址：
 * IP地址：
 * 版本号：
 * </li>
 * </p>
 */
public class TestUIActivity extends AppCompatActivity {
    
    private ScreenStyleInfo mScreenStyleInfo; // 屏幕配置信息
    private FrameLayout contentView;
    private BaseScreenLayout screenLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_screen);
        
        // 设备配置信息
        if (savedInstanceState == null) {
            mScreenStyleInfo = new ScreenStyleInfo(); // 屏幕类型
            mScreenStyleInfo.screenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
            mScreenStyleInfo.screenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
            mScreenStyleInfo.screenGroupInfo.middleCompositionInfo.styleType = 1; // 展示组件

            Log.d("TAG", "Activity：onCreate中创建一个设备信息");
        } else {

            mScreenStyleInfo = (ScreenStyleInfo) savedInstanceState.getSerializable("mScreenStyleInfo");

            Log.d("TAG","从当前Activity：onCreate中获取设备信息");
        }

        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG","onSaveInstanceState--->保存当前Activity中的设备信息");

        outState.putSerializable("mScreenStyleInfo", mScreenStyleInfo);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        Log.d("TAG","onConfigurationChanged:" + orientation);
        // 横屏
        if (orientation == SCREEN_ORIENTATION_LANDSCAPE) {
            mScreenStyleInfo.orientation = 1; // "LANDSCAPE";
        } else if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
            // 竖屏
            mScreenStyleInfo.orientation = 2; // "PORTRAIT";
        }

//        showView(mScreenStyleInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "LANDSCAPE");
        menu.add(1, 1, 1, "PORTRAIT");
        menu.add(2, 2, 2, "全屏展示");
        menu.add(3, 3, 0, "三等分");
        menu.add(3, 4, 1, "设置比例");
        menu.add(4, 5, 0, "二等分");
        menu.add(4, 6, 1, "设置比例");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0: // LANDSCAPE
                mScreenStyleInfo.orientation = 1; // "LANDSCAPE";
                break;
            case 1: // PORTRAIT
                mScreenStyleInfo.orientation = 2; // "PORTRAIT";
                break;
            case 2: // 全屏展示
                mScreenStyleInfo.styleType = 1;
                mScreenStyleInfo.leftScreenGroupInfo = null; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo = null; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo = null; // 展示组件
//
//                mScreenStyleInfo.screenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
//                mScreenStyleInfo.screenGroupInfo.compositionInfo = new ScreenCompositionInfo(); // 展示组件
//                mScreenStyleInfo.screenGroupInfo.compositionInfo.styleType = 1; //

                mScreenStyleInfo.screenGroupInfo.upCompositionInfo = new ScreenCompositionInfo();
                mScreenStyleInfo.screenGroupInfo.upCompositionInfo.weight = 1; //
                mScreenStyleInfo.screenGroupInfo.upCompositionInfo.styleType = 1; //
                mScreenStyleInfo.screenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo();
                mScreenStyleInfo.screenGroupInfo.middleCompositionInfo.weight = 8; //
                mScreenStyleInfo.screenGroupInfo.middleCompositionInfo.styleType = 2; //
                mScreenStyleInfo.screenGroupInfo.downCompositionInfo = new ScreenCompositionInfo();
                mScreenStyleInfo.screenGroupInfo.downCompositionInfo.weight = 1; //
                mScreenStyleInfo.screenGroupInfo.downCompositionInfo.styleType = 3; //

                break;
            case 3: // 三等分
                mScreenStyleInfo.styleType = 2;

                // left/up 屏幕
                mScreenStyleInfo.leftScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
                mScreenStyleInfo.leftScreenGroupInfo.weight = 2; // 分屏-布局屏幕结构
//                if (mScreenStyleInfo.orientation == 1){
//                    mScreenStyleInfo.leftScreenGroupInfo.weight = 4; // 分屏-布局屏幕结构
//                }

                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo.styleType = 1; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo.weight = 1; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo.styleType = 2; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo.weight = 7; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo.styleType = 3; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo.weight = 1; // 展示组件

                // middle 屏幕
                mScreenStyleInfo.middleScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
                mScreenStyleInfo.middleScreenGroupInfo.weight = 6; // 分屏-布局屏幕结构
//                if (mScreenStyleInfo.orientation == 1){
//                    mScreenStyleInfo.leftScreenGroupInfo.weight = 4; // 分屏-布局屏幕结构
//                }

                mScreenStyleInfo.middleScreenGroupInfo.upCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.upCompositionInfo.styleType = 1; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.upCompositionInfo.weight = 1.5f; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.middleCompositionInfo.styleType = 2; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.middleCompositionInfo.weight = 7; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.downCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.downCompositionInfo.styleType = 3; // 展示组件
                mScreenStyleInfo.middleScreenGroupInfo.downCompositionInfo.weight = 1.5f; // 展示组件

                // down/right 屏幕
                mScreenStyleInfo.rightScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
                mScreenStyleInfo.rightScreenGroupInfo.weight = 2; // 分屏-布局屏幕结构
//                if (mScreenStyleInfo.orientation == 1){
//                    mScreenStyleInfo.leftScreenGroupInfo.weight = 2; // 分屏-布局屏幕结构
//                }
                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo.styleType = 1; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo.weight = 1.5f; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo.styleType = 2; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo.weight = 7; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo.styleType = 3; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo.weight = 1.5f; // 展示组件

                break;
            case 4: // 三等分 设置比例

                break;
            case 5: // 二等分
                mScreenStyleInfo.styleType = 3;

                mScreenStyleInfo.middleScreenGroupInfo = null;

                // left/up 屏幕
                mScreenStyleInfo.leftScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
                mScreenStyleInfo.leftScreenGroupInfo.weight = 3; // 分屏-布局屏幕结构

                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo.styleType = 1; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.upCompositionInfo.weight = 1; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo.styleType = 2; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.middleCompositionInfo.weight = 1; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo.styleType = 3; // 展示组件
                mScreenStyleInfo.leftScreenGroupInfo.downCompositionInfo.weight = 1; // 展示组件

                // down/right 屏幕
                mScreenStyleInfo.rightScreenGroupInfo = new ScreenGroupInfo(); // 分屏-布局屏幕结构
                mScreenStyleInfo.rightScreenGroupInfo.weight = 7; // 分屏-布局屏幕结构

                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo.styleType = 1; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.upCompositionInfo.weight = 2; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo.styleType = 2; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.middleCompositionInfo.weight = 6; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo = new ScreenCompositionInfo(); // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo.styleType = 3; // 展示组件
                mScreenStyleInfo.rightScreenGroupInfo.downCompositionInfo.weight = 2; // 展示组件

                break;
            case 6: // 三等分 设置比例

                break;
        }

        // 更新UI
        showView(mScreenStyleInfo);
        return super.onOptionsItemSelected(item);
    }


    // 初始化UI
    private void initView() {

        contentView = (FrameLayout) findViewById(R.id.layout_container);

        if (mScreenStyleInfo == null) {
            // 显示一个空的界面

        } else {
            showView(mScreenStyleInfo);
        }

    }


    /*****
     * 显示布局显示信息
     * @param styleInfo
     */
    private void showView(ScreenStyleInfo styleInfo) {
        /*****
         *
         * 第一步：判断屏幕方向 orientation : LANDSCAPE、PORTRAIT
         * 第二步：判断布局结构类型：1、全屏展示; 2、横屏三等分; 3、竖屏三等分; 4、横屏2:8比例; 5、竖屏2:8比例   (比例可自行设置调整)
         * 第三步：根据布局结构类型：例如: 布局结构类型"2"，如下：其中主要包括：left、middle、right;
         *
         *        1.#########################################3
         *
         *        (Screen) left: top、middle、down
         *        (Screen) middle: top、middle、down
         *        (Screen) right: top、middle、down
         *
         *        2.#########################################3
         *          例如：Screen left 左半屏展示
         *        top：数据结构->展示内容名称：图片、视频、文本
         *
         *        3.#########################################3
         *        if top == null  隐藏 top
         *        else middle == null 隐藏 middle
         *        else right == null 隐藏 right
         *        @TODO 注：但会变成相应的二分屏或者全屏展示
         *
         * 第四步：数据+展示位置绑定   每个展示区域块：显示数据+区域名称
         *
         */


        if (styleInfo == null) return;

        if (screenLayout != null) {
            contentView.removeView(screenLayout);
            screenLayout = null;
        }

        switch (styleInfo.styleType) {
            case 1:  // 1、全屏展示
                screenLayout = new ScreenLayout(this);
                break;
            case 2: // 2、横屏/竖屏3等分
                screenLayout = new Screen3Layout(this);
                break;
            case 3: // 3、横屏/竖屏2比例
                screenLayout = new Screen2Layout(this);
                break;
            default:  // 默认全屏展示
                screenLayout = new ScreenLayout(this);
                break;
        }

        contentView.addView(screenLayout);
        screenLayout.updateContentView(styleInfo);
    }

}
