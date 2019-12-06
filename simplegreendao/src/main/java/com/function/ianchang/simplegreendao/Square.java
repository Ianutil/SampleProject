/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  Square
 * Created by  ianchang on 2018-08-14 13:43:34
 * Last modify date   2018-08-14 13:43:33
 */

package com.function.ianchang.simplegreendao;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by ianchang on 2018/8/14.
 *
 * 正方形
 */

public class Square {

    //顶点缓冲区
    private FloatBuffer vertexBuffer;
    //绘图顺序顶点缓冲区
    private ShortBuffer drawListBuffer;

    // 每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    //正方形四个顶点的坐标
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f     // top right

//            1, 1, 0,   // top right
//            -1, 1, 0,  // top left
//            -1, -1, 0, // bottom left
//            1, -1, 0,  // bottom right

    };

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // 顶点的绘制顺序
    // 设置图形的RGB值和透明度
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (坐标数 * 4)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
//        vertexBuffer.put(new float[] {
//                120.0f, 200.0f,
//                360.0f, 200.0f,
//                360.0f, 600.0f,
//                120.0f, 200.0f,
//                360.0f, 600.0f,
//                120.0f, 600.0f
//        });
        vertexBuffer.position(0);

        // 为绘制列表初始化字节缓冲
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (对应顺序的坐标数 * 2)short是2字节
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    /**
     * 绘图
     */
    public void draw() {
        new Util().draw(COORDS_PER_VERTEX, vertexBuffer, color, drawOrder, drawListBuffer);
    }
}

