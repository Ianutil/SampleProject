/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  People
 * Created by  ianchang on 2018-07-02 10:39:23
 * Last modify date   2018-07-02 10:39:23
 */

package com.example;

/**
 * Created by ianchang on 2018/7/2.
 */

public class People {

    public People(){
        print("我是父类");
    }

    public void print(String msg){
        System.out.println(msg);

    }

    private void printPeople(){
        print("我是父类");
    }
}
