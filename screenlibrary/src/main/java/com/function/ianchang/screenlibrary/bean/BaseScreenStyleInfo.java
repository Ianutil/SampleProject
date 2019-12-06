package com.function.ianchang.screenlibrary.bean;

import java.io.Serializable;

/*****
 * Created by ianchang on 2017/10/24.
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
public class BaseScreenStyleInfo implements Serializable {
    public static final int LANDSCAPE = 1;
    public static final int PORTRAIT = 2;

    /**
     * 其值主要用：1:landscape; 2:portrait (不分大小写)
     * */
    public int orientation = LANDSCAPE; // 屏幕方向


    /****
     * 1、图片样式
     * 2、视频样式
     * 3、文本样式
     * 4、图片、视频、文本
     */
    public int styleType; // 样式

    public String styleName; // 样式名称  绑定数据需要使用
    public int styleCode; // 样式编号  绑定数据需要使用

    public Object datas; // 需要展示的数据
}
