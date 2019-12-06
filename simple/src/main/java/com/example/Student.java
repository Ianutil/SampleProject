/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  Student
 * Created by  ianchang on 2018-07-02 10:40:06
 * Last modify date   2018-07-02 10:40:06
 */

package com.example;

/**
 * Created by ianchang on 2018/7/2.
 */

public class Student extends People {

    public Student(){
        print("我是学生");
    }


    @Override
    public void print(String msg) {
        super.print(msg);

        System.out.print("我是学生");
    }
}
