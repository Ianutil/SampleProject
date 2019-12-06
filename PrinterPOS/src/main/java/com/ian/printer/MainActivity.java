/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-08-01 18:21:45
 * Last modify date   2018-08-01 18:21:44
 */

package com.ian.printer;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.printer.bean.BillInfo;
import com.ian.printer.bean.GoodInfo;
import com.ian.printer.printer.OnDataReceiveListener;
import com.ian.printer.printer.PrinterImageUtils;
import com.ian.printer.printer.PrinterManager;
import com.ian.printer.printer.PrinterTextUtils;
import com.ian.printer.printer.SerialPort;

import java.io.FileDescriptor;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnDataReceiveListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private PrinterManager printer;
    /***
     * Fixme /dev/ttyS0  /dev/ttyS1  /dev/ttyS2  /dev/ttyS3   /dev/ttyS4
     * Fixme      4800        9600         19200    115200
     */
    private String[] devices = {
            "/dev/ttyS0",
            "/dev/ttyS1",
            "/dev/ttyS2",
            "/dev/ttyS3",
            "/dev/ttyS4"
    };

    private String[] baudRates = {
            "4800",
            "9600",
            "19200",
            "115200",
    };

    private Handler handler = new Handler();
    private String device, baudRate;

    private TextView printerStatusTV;

    private SerialPort serialPort;

    private BillInfo billInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_open).setOnClickListener(this);
        findViewById(R.id.button_close).setOnClickListener(this);

        printerStatusTV = (TextView) findViewById(R.id.printer_status);

        RadioGroup group = (RadioGroup) findViewById(R.id.tty_group);
        group.setOnCheckedChangeListener(ttyListener);

        group = (RadioGroup) findViewById(R.id.rb_group);
        group.setOnCheckedChangeListener(rbListener);

        // FIXME: 2018/9/6 创建打印机对象
        printer = new PrinterManager(this);

        device = devices[3];
        baudRate = baudRates[1];

        initData();
    }


    @Override
    protected void onStop() {
        if (printer != null){
            printer.closePrinter();
            printer = null;
        }

        super.onStop();
    }

    private RadioGroup.OnCheckedChangeListener ttyListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            switch (checkedId) {
                case R.id.tty_s0:
                    device = devices[0];
                    break;
                case R.id.tty_s1:
                    device = devices[1];
                    break;
                case R.id.tty_s2:
                    device = devices[2];
                    break;
                case R.id.tty_s3:
                    device = devices[3];
                    break;
                case R.id.tty_s4:
                    device = devices[4];
                    break;
            }

            Log.i(TAG, "选择设备:" + device);

        }
    };

    private RadioGroup.OnCheckedChangeListener rbListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_4800:
                    baudRate = baudRates[0];
                    break;
                case R.id.rb_9600:
                    baudRate = baudRates[1];
                    break;
                case R.id.rb_19200:
                    baudRate = baudRates[2];
                    break;
                case R.id.rb_115200:
                    baudRate = baudRates[3];
                    break;
            }

            Log.i(TAG, "选择频率:" + baudRate);
        }
    };

    @Override
    public void onClick(View v) {
        String state = "";
        switch (v.getId()) {
            case R.id.button_open:
                serialPort = printer.openPrinter(device, Integer.valueOf(baudRate));
                state = "打开串口";
                break;
            case R.id.button_close:
                printer.closePrinter();
                state = "关闭串口";
                break;
        }

        if (serialPort == null) {
            printerStatusTV.setText(state + ":设备连接失败");
        } else {
            FileDescriptor fileDescriptor = serialPort.mFd;
            String result = fileDescriptor.toString();
            Log.e(TAG, "--------result:" + result);
            printerStatusTV.setText(state + " " + result);
        }

        printerStatusTV.postInvalidate();
    }

    // 打印小票
    public void printBill(View view) {

        PrinterTextUtils.sentBigMessage(printer, "星期一餐厅", 1);
        String line = "--------------------------------";
        PrinterTextUtils.sentSmallMessage(printer, line, 0);

        String str = "订 单 号：1005199";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "用 户 名：15712937281";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "桌    号：3号桌";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "订单状态：订单已确认";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "订单日期：2018/8/3 12:34:53";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "付 款 人：线下支付（支付宝）";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "服 务 员：1001";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);

        str = "订单备注：不要辣，少盐";
        PrinterTextUtils.sentSmallMessage(printer, str, 0);


        // 移动打印位置到下一个水平定位点的位置
//        printer.sendSerialPort(new byte[]{10});

        PrinterTextUtils.sentMessage(printer, "品项", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer);

        PrinterTextUtils.sentMessage(printer, "单价", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer);

        PrinterTextUtils.setBlankSpace(printer);
        PrinterTextUtils.sentMessage(printer, "数量", PrinterTextUtils.ALIGNMENT_LEFT);

        PrinterTextUtils.setBlankSpace(printer);
        PrinterTextUtils.sentSmallMessage(printer, "小计", PrinterTextUtils.ALIGNMENT_LEFT);

        line = "--------------------------------";
        PrinterTextUtils.sentSmallMessage(printer, line, 0);

        float sum = 0;
        for (int i = 0; i < 5; i++) {
            PrinterTextUtils.sentMessage(printer, "豆腐" + i, PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer);

            PrinterTextUtils.sentMessage(printer, "12.03", PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer);

            PrinterTextUtils.sentMessage(printer, "1" + i, PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer);

            sum += 12.03 * (10 + i);
            PrinterTextUtils.sentSmallMessage(printer, String.valueOf(12 * (10 + i)), PrinterTextUtils.ALIGNMENT_LEFT);
        }

        line = "--------------------------------";
        PrinterTextUtils.sentSmallMessage(printer, line, 0);

        PrinterTextUtils.sentSmallMessage(printer, "总计: " + sum + " 元", PrinterTextUtils.ALIGNMENT_RIGHT);

        PrinterTextUtils.setSound(printer, false);
        // 发送切纸命令
//        PrinterTextUtils.sendCutPage(printer);

    }

    // 打印表格
    public void testPrintForm(View view) {

        // 移动打印位置到下一个水平定位点的位置
//        printer.sendSerialPort(new byte[]{9});
        // 选择/取消下划线模式 0:取消下划线模式;1:选择下划线模式（1 点宽）2:选择下划线模式（2 点宽）
//        printer.sendSerialPort(new byte[]{27,45, 2});
        // 右边距
//        printer.sendSerialPort(new byte[]{27, 32, 50});
        PrinterTextUtils.sentMessage(printer, "品项", PrinterTextUtils.ALIGNMENT_LEFT);

        byte[] command = new byte[]{27, 92, 0, 0};
        byte nl = (byte) (40 % 256);
        byte nh = (byte) (40 / 256);
        command[2] = nl;
        command[3] = nh;
        printer.sendSerialPort(command);
        printer.sendSerialPort(command);

        // 右边距
//        printer.sendSerialPort(new byte[]{27, 32, 50});
        PrinterTextUtils.sentMessage(printer, "单价", PrinterTextUtils.ALIGNMENT_LEFT);
//        printer.sendSerialPort(new byte[]{27, 68, 50, 0});

        command = new byte[]{27, 92, 0, 0};
        nl = (byte) (40 % 256);
        nh = (byte) (40 / 256);
        command[2] = nl;
        command[3] = nh;
        printer.sendSerialPort(command);

        PrinterTextUtils.sentMessage(printer, "数量", PrinterTextUtils.ALIGNMENT_LEFT);

        command = new byte[]{27, 92, 0, 0};
        nl = (byte) (40 % 256);
        nh = (byte) (40 / 256);
        command[2] = nl;
        command[3] = nh;
        printer.sendSerialPort(command);
        PrinterTextUtils.sentSmallMessage(printer, "小计", PrinterTextUtils.ALIGNMENT_LEFT);

        // 提示音 9, 9播放声音，1，1关闭声音
        printer.sendSerialPort(new byte[]{27, 66, 1, 1});

    }

    // Fixme 测试打印文字
    public void testPrinter(View view) {

        if (serialPort == null) {
            Toast.makeText(this, "请先打开串口", Toast.LENGTH_SHORT).show();
            return;
        }

        // "测试", 1
        printer.sendSerialPort(new byte[]{27, 33, 0});
        PrinterTextUtils.sentBigMessage(printer, "测试", 1);
        PrinterTextUtils.sentBigMessage(printer, "1234567890ABCDE", 0);

        PrinterTextUtils.sendSplitLine(printer);

        printer.sendSerialPort(new byte[]{29, 80, 3, 0});
        printer.sendSerialPort(PrinterTextUtils.setAbsolutePrintPosition(6));

        PrinterTextUtils.sentSmallMessage(printer, "2018-08-07 13:26:00", PrinterTextUtils.ALIGNMENT_CENTER);
        PrinterTextUtils.sentSmallMessage(printer, "测试123456789", 0);

        PrinterTextUtils.sendSplitLine(printer, System.currentTimeMillis());

        PrinterTextUtils.sentSmallMessage(printer, "开发测试12342343", 1);
        PrinterTextUtils.sentSmallMessage(printer, "打印测试1234234", 2);


        // 发送切纸命令
        PrinterTextUtils.sendCutPage(printer);

    }

    // 打印图片
    public void testPrinterImg(View view) {

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
//        byte[] buf = PrinterImageUtils.draw2PxPoint(PrinterImageUtils.compressPic(bitmap));
//        byte[] buf = PrinterImageUtils.draw2PxPoint(bitmap);
//
//        if ((bitmap != null) && (bitmap.isRecycled())) {
//            bitmap.recycle();
//            bitmap = null;
//        }

        // 发送图片
//        printer.sendSerialPort(buf);

        String result = PrinterImageUtils.getQrCodeCmd("www.baidu.com", 6);

        byte[] data = new byte[]{27, 36, 0, 0};
        byte nl = (byte) (3 % 256);
        byte nh = -(byte) (3 / 256);
        data[2] = nl;
        data[3] = nh;
        printer.sendSerialPort(data);
        printer.sendSerialPort(PrinterTextUtils.setAlignment(PrinterTextUtils.ALIGNMENT_LEFT));
        printer.sendSerialPort(result.getBytes());


        nl = (byte) (3 % 256);
        nh = -(byte) (10 / 256);
        printer.sendSerialPort(new byte[]{27, 92, nl, nh});

        printer.sendSerialPort(result.getBytes());

        // 发送切纸命令
//        PrinterTextUtils.sendCutPage(printer);
    }


    // 测试Bill
    public void testPrintBill(View view) {
        // FIXME: 2018/9/6 打印小票
        printer.printBill(billInfo);

//        printBill();
    }


    private void printBill() {
        // 所在店门
        PrinterTextUtils.sentSmallMessage(printer, billInfo.storeName, PrinterTextUtils.ALIGNMENT_CENTER);

        // 标题栏
        PrinterTextUtils.sentSmallMessage(printer, billInfo.billName, PrinterTextUtils.ALIGNMENT_LEFT);

        // Pos机名称
        PrinterTextUtils.sentMessage(printer, "终端：" + billInfo.deviceName, PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer, 100);

        PrinterTextUtils.sentSmallMessage(printer, "交易：" + billInfo.transactNum, PrinterTextUtils.ALIGNMENT_LEFT);

        // 分割线
        PrinterTextUtils.sendSplitLine(printer);

        // 品名/条码
        PrinterTextUtils.sentMessage(printer, "品名/条码", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer, 80);
//        printer.sendSerialPort(PrinterTextUtils.setAbsolutePrintPosition(120));

        // 单价
        PrinterTextUtils.sentMessage(printer, "单价", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer, 30);
//        printer.sendSerialPort(PrinterTextUtils.setAbsolutePrintPosition(120));

        // 数量
        PrinterTextUtils.sentMessage(printer, "数量", PrinterTextUtils.ALIGNMENT_LEFT);
        PrinterTextUtils.setBlankSpace(printer, 20);

        // 金额
        PrinterTextUtils.sentSmallMessage(printer, "金额", PrinterTextUtils.ALIGNMENT_LEFT);

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

//            PrinterTextUtils.sentMessage(printer, (i+1)+".椰牌\n软椰子汁695435434454534", PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.sentMessage(printer, info.name, PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer, 80);


            PrinterTextUtils.sentMessage(printer, format.format(info.price), PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer, 30);


            PrinterTextUtils.sentMessage(printer, String.valueOf(info.count), PrinterTextUtils.ALIGNMENT_LEFT);
            PrinterTextUtils.setBlankSpace(printer, 30);

            PrinterTextUtils.sentSmallMessage(printer, format.format(info.price), PrinterTextUtils.ALIGNMENT_LEFT);

        }

        // 下分割线
        PrinterTextUtils.sendSplitLine(printer, System.currentTimeMillis());

        // 件数
        PrinterTextUtils.sentSmallMessage(printer, "件数：" + billInfo.count, PrinterTextUtils.ALIGNMENT_LEFT);

        // 应付合计
        PrinterTextUtils.sentSmallMessage(printer, "应付合计：" + format.format(billInfo.amount), PrinterTextUtils.ALIGNMENT_LEFT);

        // 微信支付
        PrinterTextUtils.sentSmallMessage(printer, String.format("%s：%s", billInfo.paymentMethod, format.format(billInfo.amount)), PrinterTextUtils.ALIGNMENT_LEFT);


        // 分割线3
        PrinterTextUtils.sendSplitLine(printer);

        // 会员卡号
        PrinterTextUtils.sentSmallMessage(printer, String.format("会员卡号：%s", billInfo.memberNum), PrinterTextUtils.ALIGNMENT_LEFT);

        // 消费前积分
        PrinterTextUtils.sentSmallMessage(printer, String.format("消费前积分：%s", format.format(billInfo.scoreUp)), PrinterTextUtils.ALIGNMENT_LEFT);

        // 本次积分
        PrinterTextUtils.sentSmallMessage(printer, String.format("本次积分：%s", format.format(billInfo.consumePoint)), PrinterTextUtils.ALIGNMENT_LEFT);

        // 开票号
        PrinterTextUtils.sentSmallMessage(printer, String.format("开票码：%s", billInfo.billNum), PrinterTextUtils.ALIGNMENT_LEFT);

        PrinterTextUtils.setNextLine(printer);


        if (!TextUtils.isEmpty(billInfo.qrcode)) {
            // 二维码
            String result = PrinterImageUtils.getQrCodeCmd(billInfo.qrcode, 8);
            printer.sendSerialPort(PrinterTextUtils.setAlignment(PrinterTextUtils.ALIGNMENT_CENTER));
            printer.sendSerialPort(result.getBytes());

            PrinterTextUtils.setNextLine(printer);
            PrinterTextUtils.sentSmallMessage(printer, billInfo.qrcodeLabel, PrinterTextUtils.ALIGNMENT_CENTER);

            PrinterTextUtils.setNextLine(printer);

        }

        // 结尾
        PrinterTextUtils.sentSmallMessage(printer, billInfo.wishSlogan, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(printer, billInfo.addressOfStore, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(printer, billInfo.telphoneOfStore, PrinterTextUtils.ALIGNMENT_CENTER);

        PrinterTextUtils.sentSmallMessage(printer, billInfo.fallowOfWeChat, PrinterTextUtils.ALIGNMENT_CENTER);

        // 发出警告声音，表示已经打印完成
        PrinterTextUtils.setSound(printer, false);
        // 发送切纸命令
        PrinterTextUtils.sendCutPage(printer);

    }


    private void initData() {
        billInfo = new BillInfo();
        billInfo.storeName = "世纪联华-青浦店";
        billInfo.billName = "销售小票";
        billInfo.deviceName = "021";
        billInfo.transactNum = "13050008";

        ArrayList<GoodInfo> data = new ArrayList<>();
        billInfo.goodsInfo = data;

        GoodInfo info;
        for (int i = 0; i < 4; i++) {
            info = new GoodInfo();
            info.name = "测试菜品" + i;
            info.price = 20 + i;
            info.count = 3 + i;
            info.sum = info.price * info.count;
            data.add(info);

            // 总金额
            billInfo.amount += info.sum;
            // 总件数
            billInfo.count += info.count;
        }


        // 实付金额
        billInfo.payOut = billInfo.amount;
        // 支付方式
        billInfo.paymentMethod = "微信支付";

        // 会员号
        billInfo.memberNum = "1000000000000000";
        // 累积积分
        billInfo.scoreUp = 729;
        // 本次消费积分
        billInfo.consumePoint = 13;
        // 开票码
        billInfo.billNum = "1423424343242433232430";

        billInfo.qrcode = "http://www.baidu.com";
        billInfo.qrcodeLabel = "加入社群优惠福利";
    }

    @Override
    public void onDataReceive(final String data) {

        Log.d(TAG, "进入数据监听事件中---->onDataReceive:" + data);
        //
        //在线程中直接操作UI会报异常：ViewRootImpl$CalledFromWrongThreadException
        //解决方法：handler
        handler.post(new Runnable() {
            @Override
            public void run() {
                printerStatusTV.setText(data);
            }
        });
    }


    public void testOpenLock(View view) {

        startActivity(new Intent(this, LockActivity.class));
    }


}
