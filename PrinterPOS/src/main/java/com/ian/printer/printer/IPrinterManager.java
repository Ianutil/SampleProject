/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  IPrinterCode
 * Created by  ianchang on 2018-08-03 11:19:36
 * Last modify date   2018-08-03 11:19:36
 */

package com.ian.printer.printer;

import com.ian.printer.bean.BillInfo;

/**
 * Created by ianchang on 2018/8/3.
 */

public interface IPrinterManager {

    /*******
     *
     * @param device  打开指定串口设备
     * @param baudRate 波特率
     * @return 获取一个连接对象
     */
    SerialPort openPrinter(String device, int baudRate);

    /********
     * 关闭设备连接
     */
    void closePrinter();

    /*****
     * 打印小票
     * @param info
     */
    void printBill(BillInfo info);
}
