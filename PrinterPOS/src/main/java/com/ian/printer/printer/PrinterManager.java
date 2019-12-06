/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  SerialPortUtils
 * Created by  ianchang on 2018-08-03 10:08:39
 * Last modify date   2018-08-02 19:04:34
 */

package com.ian.printer.printer;

import android.text.TextUtils;
import android.util.Log;

import com.ian.printer.bean.BillInfo;
import com.ian.printer.bean.GoodInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  PrinterPOS
 * Class name  PrinterManager
 * Created by  ianchang on 2018-08-03 10:00:37
 * Last modify date   2018-08-02 18:52:52
 *
 */

public class PrinterManager implements IPrinterManager {

    private final static String TAG = PrinterManager.class.getSimpleName();

    /***
     * Fixme /dev/ttyS0  /dev/ttyS1  /dev/ttyS2  /dev/ttyS3   /dev/ttyS4
     * Fixme      4800        9600         19200    19200
     */
    private String mDevice = "/dev/ttyS3";
    private int mBaudRate = 9600; // Fixme 4800/9600/19200/115200
    public boolean isOpen; //是否打开串口标志
    public boolean isStop; //线程状态，为了安全终止线程

    // FIXME: 2018/9/6 打印串口对象
    public SerialPort mSerialPort;
    public FileInputStream inputStream;
    public FileOutputStream outputStream;
    private ReceiverThread mReceiveThread;

    public OnDataReceiveListener onDataReceiveListener;

    /*******
     * @see #openPrinter(String, int)
     * @see #closePrinter()
     * @see #printBill(BillInfo)
     * @param listener
     */
    public PrinterManager(OnDataReceiveListener listener){
        onDataReceiveListener = listener;
        isOpen = false;
        isStop = true;
    }


    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }


    public boolean isOpen(){
        return isOpen;
    }

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    @Override
    public SerialPort openPrinter(String device, int baudRate) {

        try {

            Log.i(TAG, "device:"+device + " rate:"+baudRate);

            if (mSerialPort != null){
                // 已经连接的，就不在重复连接了
                if (mSerialPort != null && mDevice.equals(device) && mBaudRate == baudRate){
                    return mSerialPort;
                }else {
                    closePrinter();
                }
            }

            // 重新连接相对应的设备
            mDevice = device;
            mBaudRate = baudRate;

            mSerialPort = new SerialPort(new File(mDevice), mBaudRate, 0);
            
            isOpen = true;
            isStop = false; //线程状态

            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = (FileInputStream) mSerialPort.getInputStream();
            outputStream = (FileOutputStream) mSerialPort.getOutputStream();

            // 开始线程监控是否有数据要接收
            mReceiveThread = new ReceiverThread();
            mReceiveThread.start(); 
        } catch (IOException e) {
            e.printStackTrace();
            closePrinter();
            Log.e(TAG, "openPrinter: 打开串口异常：" + e.toString());
        }
        
        Log.i(TAG, "openPrinter: 打开串口");
        return mSerialPort;
    }

    /**
     * 关闭串口
     */
    @Override
    public void closePrinter() {
        try {

            // 线程状态：停止接收消息
            isStop = true; 
            
            if (mReceiveThread != null){
                mReceiveThread.interrupt();
            }
            
            mReceiveThread = null;
            
            if (inputStream != null){
                inputStream.close();
            }

            inputStream = null;

            if (outputStream != null){
                outputStream.close();
            }
            outputStream = null;

            if (mSerialPort != null){
                mSerialPort.close();
            }

            isOpen = false;

            mSerialPort = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "closePrinter: 关闭串口异常：" + e.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        
        Log.d(TAG, "closePrinter: 关闭串口成功");
    }

    /**
     * 发送串口指令（字符串）
     *
     * @param data String数据指令
     */
    public void sendSerialPort(byte[] data) {

        try {

            if (data == null || data.length == 0) return;

            if (outputStream == null) return;

            outputStream.write(data);

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 单开一线程，来读数据
     */
    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            super.run();

            //判断进程是否在运行，更安全的结束进程
            while (!isStop) {

                try {

                    // 64   1024
                    byte[] buffer = new byte[64];
                    int length; //读取数据的大小

                    length = inputStream.read(buffer);

                    Log.e(TAG, "run: 数据读取："+length);

                    if (length > 0) {
                        String data = new String(buffer, 0, length);
                        Log.d(TAG, "接收到了数据大小：" + String.valueOf(length)+" data:"+data);

                        if (onDataReceiveListener != null){
                            onDataReceiveListener.onDataReceive(data);
                        }

                    }

                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：");
                    e.printStackTrace();
                }
            }

        }
    }

    /**********
     * 测试Bill
     * @param billInfo
     */
    @Override
    public void printBill(BillInfo billInfo) {
        // 所在店门
        PrinterTextUtils.sentSmallMessage(this, billInfo.storeName, PrinterTextUtils.ALIGNMENT_CENTER);

        // 标题栏
        PrinterTextUtils.sentSmallMessage(this, billInfo.billName, PrinterTextUtils.ALIGNMENT_LEFT);

        // Pos机名称
        PrinterTextUtils.sentMessage(this, "终端："+billInfo.deviceName, PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(this, 100);

        PrinterTextUtils.sentSmallMessage(this, "交易："+billInfo.transactNum, PrinterTextUtils.ALIGNMENT_LEFT);

        // 分割线
        PrinterTextUtils.sendSplitLine(this);

        // 品名/条码
        PrinterTextUtils.sentMessage(this, "品名/条码", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(this, 80);
//        printer.sendSerialPort(PrinterTextUtils.setAbsolutePrintPosition(120));

        // 单价
        PrinterTextUtils.sentMessage(this, "单价", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(this, 30);
//        printer.sendSerialPort(PrinterTextUtils.setAbsolutePrintPosition(120));

        // 数量
        PrinterTextUtils.sentMessage(this, "数量", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(this, 20);

        // 金额
        PrinterTextUtils.sentSmallMessage(this, "金额", PrinterTextUtils.ALIGNMENT_LEFT);

//        StringBuilder builder = new StringBuilder();
//        int len, splitCount = 5;

        DecimalFormat format = new DecimalFormat("#.00");
        int count = billInfo.goodsInfo.size();
        GoodInfo info;
        // 购买项
        for (int i = 0; i < count; i++) {
            info = billInfo.goodsInfo.get(i);
//            len = builder.length();
//            builder.delete(0, len);
//
//            builder.append(i + 1);
//            builder.append(".");
//            builder.append("椰牌软椰子汁695435434454534");
//
//            if (len > splitCount) {
//                for (i = 0; i <len; i+=5){
//                    if (i == 0) continue;
//
//                    builder.insert(i, "\n");
//                }
//            }

//            PrinterTextUtils.sentMessage(this, (i+1)+".椰牌\n软椰子汁695435434454534", PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.sentMessage(this, info.name, PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(this, 80);


            PrinterTextUtils.sentMessage(this, format.format(info.price), PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(this, 30);


            PrinterTextUtils.sentMessage(this, String.valueOf(info.count), PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(this, 30);

            PrinterTextUtils.sentSmallMessage(this, format.format(info.price), PrinterTextUtils.ALIGNMENT_LEFT);

        }

        // 下分割线
        PrinterTextUtils.sendSplitLine(this, System.currentTimeMillis());

        // 件数
        PrinterTextUtils.sentSmallMessage(this, "件数："+billInfo.count, PrinterTextUtils.ALIGNMENT_LEFT);

        // 应付合计
        PrinterTextUtils.sentSmallMessage(this, "应付合计："+format.format(billInfo.amount), PrinterTextUtils.ALIGNMENT_LEFT);

        // 微信支付
        PrinterTextUtils.sentSmallMessage(this, String.format("%s：%s", billInfo.paymentMethod, format.format(billInfo.amount)), PrinterTextUtils.ALIGNMENT_LEFT);


        // 分割线3
        PrinterTextUtils.sendSplitLine(this);

        // 会员卡号
        PrinterTextUtils.sentSmallMessage(this, String.format("会员卡号：%s", billInfo.memberNum), PrinterTextUtils.ALIGNMENT_LEFT);

        // 消费前积分
        PrinterTextUtils.sentSmallMessage(this, String.format("消费前积分：%s", format.format(billInfo.scoreUp)), PrinterTextUtils.ALIGNMENT_LEFT);

        // 本次积分
        PrinterTextUtils.sentSmallMessage(this, String.format("本次积分：%s", format.format(billInfo.consumePoint)), PrinterTextUtils.ALIGNMENT_LEFT);

        // 开票号
        PrinterTextUtils.sentSmallMessage(this, String.format("开票码：%s", billInfo.billNum), PrinterTextUtils.ALIGNMENT_LEFT);

        PrinterTextUtils.setNextLine(this);


        if (!TextUtils.isEmpty(billInfo.qrcode)){
            // 二维码
            String result = PrinterImageUtils.getQrCodeCmd(billInfo.qrcode, 8);
            sendSerialPort(PrinterTextUtils.setAlignment(PrinterTextUtils.ALIGNMENT_CENTER));
            sendSerialPort(result.getBytes());

            PrinterTextUtils.setNextLine(this);
            PrinterTextUtils.sentSmallMessage(this, billInfo.qrcodeLabel, PrinterTextUtils.ALIGNMENT_CENTER);

            PrinterTextUtils.setNextLine(this);

        }

        // 结尾
        PrinterTextUtils.sentSmallMessage(this, billInfo.wishSlogan, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(this, billInfo.addressOfStore, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(this, billInfo.telphoneOfStore, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(this, billInfo.fallowOfWeChat, PrinterTextUtils.ALIGNMENT_CENTER);

        // 发出警告声音，表示已经打印完成
        PrinterTextUtils.setSound(this, false);
        // 发送切纸命令
        PrinterTextUtils.sendCutPage(this);

    }

}
