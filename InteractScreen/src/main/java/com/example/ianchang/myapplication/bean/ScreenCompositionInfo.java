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
public class ScreenCompositionInfo implements Serializable {

    public float weight; // 比例

    public String styleName; // 样式名称  绑定数据需要使用
    public int styleCode; // 样式编号  绑定数据需要使用

    // 展示内容名称：图片、视频、文本等组件名称或者代号
    public int styleType; // 样式类型

    public Object datas; // 需要展示的数据

}
