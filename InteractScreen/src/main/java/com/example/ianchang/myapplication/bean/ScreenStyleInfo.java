package com.example.ianchang.myapplication.bean;

import java.io.Serializable;

/**
 * Created by ianchang on 2017/10/24.
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
public class ScreenStyleInfo implements Serializable {
    public static final int LANDSCAPE = 1;
    public static final int PORTRAIT = 2;

    /**
     * 其值主要用：1:landscape; 2:portrait (不分大小写)
     * */
    public int orientation; // 屏幕方向

    /**
     * 1、全屏展示; 2、横屏/竖屏三等分; 3、横屏/竖屏2:8比例   (比例可自行设置调整)
     *
     * 1、全屏使用 screenGroupInfo
     * 2、横/竖屏三等分  leftScreenGroupInfo、middleScreenGroupInfo、rightScreenGroupInfo
     * 3、横/竖二分屏   leftScreenGroupInfo、rightScreenGroupInfo
     * **/
    public int styleType; // 样式类型
    public ScreenGroupInfo screenGroupInfo; // 单屏 屏幕结构信息

    // 分屏 屏幕结构
    public ScreenGroupInfo leftScreenGroupInfo; // 屏幕结构信息
    public ScreenGroupInfo middleScreenGroupInfo; // 屏幕结构信息
    public ScreenGroupInfo rightScreenGroupInfo; // 屏幕结构信息


    public String styleName; // 样式名称  绑定数据需要使用
    public int styleCode; // 样式编号  绑定数据需要使用

}
