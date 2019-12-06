/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  Triangle
 * Created by  ianchang on 2018-08-14 13:42:42
 * Last modify date   2018-08-14 13:42:42
 */

package com.function.ianchang.simplegreendao;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by ianchang on 2018/8/14.
 *
 * 三角形
 */

public class Triangle {

    private FloatBuffer vertexBuffer;

    // 设置每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    // 设置三角形顶点数组
    static float triangleCoords[] = { // 默认按逆时针方向顺序绘制
            0.0f, 0.622008459f, 0.0f,   // 顶
            -0.5f, -0.311004243f, 0.0f,   // 左底
            0.5f, -0.311004243f, 0.0f    // 右底
    };

    // 设置图形的RGB值和透明度
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    /**
     * 初始化
     * 流程：创建一个顶点的缓冲区空间，然后将其作为一个浮点的缓冲区，
     * 然后将坐标加到这个缓冲区中，然后将读指针指向第一个位置
     */
    public Triangle() {
        // 初始化顶点字节缓冲区，用于存放形状的坐标，
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (每个浮点数占用4个字节)
                triangleCoords.length * 4);
        // 设置使用设备硬件的原生字节序
        bb.order(ByteOrder.nativeOrder());

        // 将ByteBuffer作为一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        // 把坐标都添加到FloatBuffer中
        vertexBuffer.put(triangleCoords);
        // 设置buffer从第一个坐标开始读
        vertexBuffer.position(0);
    }

    /**
     * 绘图
     */
    public void draw(){
        new Util().draw(COORDS_PER_VERTEX,vertexBuffer,color);
    }
}

