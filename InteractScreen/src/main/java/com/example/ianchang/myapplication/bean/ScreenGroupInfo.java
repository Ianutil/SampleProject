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
public class ScreenGroupInfo implements Serializable {

    public ScreenCompositionInfo upCompositionInfo; // 上
    public ScreenCompositionInfo middleCompositionInfo; // 中
    public ScreenCompositionInfo downCompositionInfo; // 下

    public int styleType; // 样式

    public float weight = 1; // 比例 默认1
    public String styleName; // 样式名称  绑定数据需要使用
    public int styleCode; // 样式编号  绑定数据需要使用
}
