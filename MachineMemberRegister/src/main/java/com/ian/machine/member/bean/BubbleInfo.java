/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BubbleInfo
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:42
 */

package com.ian.machine.member.bean;

/**
 * Created by ianchang on 2018/8/14.
 */

public class BubbleInfo {
    /** 气泡半径 */
    private int radius;
    /** 上升速度 */
    private float speedY;
    /** 平移速度 */
    private float speedX;
    /** 气泡x坐标 */
    private float x;
    /** 气泡y坐标 */
    private float y;

    public int color;

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius
     *            the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the speedY
     */
    public float getSpeedY() {
        return speedY;
    }

    /**
     * @param speedY
     *            the speedY to set
     */
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    /**
     * @return the speedX
     */
    public float getSpeedX() {
        return speedX;
    }

    /**
     * @param speedX
     *            the speedX to set
     */
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

}