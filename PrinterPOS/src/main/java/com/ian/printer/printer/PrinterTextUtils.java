/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  PrinterUtils
 * Created by  ianchang on 2018-08-03 10:00:37
 * Last modify date   2018-08-02 18:52:52
 */

package com.ian.printer.printer;

import android.support.annotation.IntDef;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ianchang on 2018/8/2.
 */

public class PrinterTextUtils {

    public static final String ENCODE = "GB2312";
    // 切纸
    public static final int CUT_PAGE = 12;
    /**分割线*/
    public static final String SPLIT_LINE_01 = "--------------------------------";
    public static final String SPLIT_LINE_02 = "================================";

    /*****行间距***/
    public static final int LINE_SPACE = 60;

    /*****左对齐***/
    public static final int ALIGNMENT_LEFT = Alignment.LEFT;
    /*****居中***/
    public static final int ALIGNMENT_CENTER = Alignment.CENTER;
    /*****右对齐***/
    public static final int ALIGNMENT_RIGHT = Alignment.RIGHT;

    @IntDef({Alignment.LEFT, Alignment.CENTER, Alignment.RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Alignment{
        /*****左对齐***/
        int LEFT = 0;
        /*****居中***/
        int CENTER = 1;
        /*****右对齐***/
        int RIGHT = 2;
    }

    @IntDef({FontSize.SIZE_1, FontSize.SIZE_2, FontSize.SIZE_3, FontSize.SIZE_4,
            FontSize.SIZE_5, FontSize.SIZE_6, FontSize.SIZE_7, FontSize.SIZE_8})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FontSize{
        /*****左对齐***/
        int SIZE_1 = 0;
        int SIZE_2 = 16;
        int SIZE_3 = 32;
        int SIZE_4 = 48;
        int SIZE_5 = 64;
        int SIZE_6 = 80;
        int SIZE_7 = 96;
        int SIZE_8 = 112;
    }


    /********
     * 对齐方式
     * @param code 0：左对齐；1：居中；2：右对齐
     * @return
     */
    public static byte[] setAlignment(int code) {
        byte[] data = new byte[3];
        data[0] = 27;
        data[1] = 97;
        if (code > 2) {
            data[2] = 2;
            return data;
        }
        data[2] = ((byte) code);
        return data;
    }

    public static byte[] setLineSpace(int code) {
        byte[] data = new byte[3];
        data[0] = 27;
        data[1] = 51;
        if (code > 127) {
            data[2] = Byte.MAX_VALUE;
            return data;
        }
        data[2] = ((byte) code);
        return data;
    }

    /*******
     *
     * @param code 0:正常;1:加粗
     * @return
     */
    public static byte[] setTextBold(int code) {
        byte[] data = new byte[3];
        data[0] = 27;
        data[1] = 69;
        if (code > 0) {
            data[2] = 1;
            return data;
        }
        data[2] = ((byte) code);
        return data;
    }

    public static byte[] setSizeChar(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt1;
        if (paramInt1 > 1) {
            i = 1;
        }
        paramInt1 = paramInt2;
        if (paramInt2 > 1) {
            paramInt1 = 1;
        }
        paramInt2 = paramInt3;
        if (paramInt3 > 1) {
            paramInt2 = 1;
        }
        paramInt3 = paramInt4;
        if (paramInt4 > 1) {
            paramInt3 = 1;
        }
        return new byte[]{27, 33, (byte) ((i << 4) + (paramInt1 << 5) + (paramInt2 << 7) + paramInt3)};
    }

    public static byte[] setSizeChinese(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt1;
        if (paramInt1 > 1) {
            i = 1;
        }
        paramInt1 = paramInt2;
        if (paramInt2 > 1) {
            paramInt1 = 1;
        }
        paramInt2 = paramInt3;
        if (paramInt3 > 1) {
            paramInt2 = 1;
        }
        return new byte[]{28, 33, (byte) ((i << 3) + (paramInt1 << 2) + (paramInt2 << 7) + paramInt4)};
    }

    /*********
     *
     * @param msg
     * @param code code=0表示打印且换行
     * @return
     */
    public static byte[] printString(String msg, int code) {

        try {
            byte[] data = msg.getBytes(ENCODE);

            int length = data.length;

            // code = 0表示：打印并换行
            if (code == 0) {
                length += 1;
                byte[] buf = new byte[length];

                System.arraycopy(data, 0, buf, 0, data.length);

                buf[(length - 1)] = 10;

                return buf;
            }

            return data;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static byte[] printFeedLine(int code) {
        return new byte[]{27, 100, (byte) code};
    }

    public static byte[] printCutPaper(int code) {
        byte[] data = new byte[3];

        if (code != 1) {
            data[0] = 27;
            data[1] = 105;
        } else {
            data[0] = 27;
            data[1] = 109;
        }

        data[2] = ((byte) code);

        return data;
    }

    public static byte[] setClean() {
        return new byte[]{27, 64};
    }


    /**********
     * 发送切纸命令
     * @param printer
     */
    public static void sendCutPage(PrinterManager printer) {
        printer.sendSerialPort(PrinterTextUtils.printFeedLine(CUT_PAGE));
        printer.sendSerialPort(PrinterTextUtils.printCutPaper(1));
        printer.sendSerialPort(PrinterTextUtils.setClean());
    }


    /*******
     * 发送一个大的消息
     * @param printer
     * @param msg
     * @param code
     */
    public static void sentBigMessage(PrinterManager printer, String msg, int code) {

        printer.sendSerialPort(setAlignment(code));
        printer.sendSerialPort(setLineSpace(LINE_SPACE));
        printer.sendSerialPort(setSizeChar(1, 1, 0, 0));
        printer.sendSerialPort(setSizeChinese(1, 1, 0, 0));
        printer.sendSerialPort(printString(msg, 0));

    }

    /*******
     * 发送一个内容的消息
     * @param printer
     * @param msg
     * @param code
     */
    public static void sentSmallMessage(PrinterManager printer, String msg, int code) {

        printer.sendSerialPort(setSizeChar(0, 0, 0, 0));
        printer.sendSerialPort(setSizeChinese(0, 0, 0, 0));
        printer.sendSerialPort(setLineSpace(LINE_SPACE));
        printer.sendSerialPort(setAlignment(code));
        printer.sendSerialPort(setTextFont(0));

        // 设置字符大小
//        printer.sendSerialPort(new byte[]{29, 33, 0});

        printer.sendSerialPort(printString(msg, 0));

    }

    public static void sendSplitLine(PrinterManager printer){
        printer.sendSerialPort(setLineSpace(10));
        printer.sendSerialPort(setAlignment(ALIGNMENT_LEFT));
        printer.sendSerialPort(setSizeChar(0, 0, 0, 0));
        printer.sendSerialPort(setSizeChinese(0, 0, 0, 0));
        setNextLine(printer);
        printer.sendSerialPort(SPLIT_LINE_02.getBytes());
        setNextLine(printer);
        setNextLine(printer);
    }

    /*******
     * 格式化日期
     * @param printer
     * @param date
     */
    public static void sendSplitLine(PrinterManager printer, long date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String text = format.format(new Date(date));

        StringBuilder builder = new StringBuilder();
        builder.append("  ");
        builder.append(text);
        builder.append("  ");
        int count = builder.length();
        int total  = SPLIT_LINE_01.length();

        while (count < total){
            builder.append("=");
            builder.insert(0, "=");

            count += 2;
        }

        // 删除最后一个
        if (builder.length() > total){
            builder.delete(total - 1, total);
        }

        printer.sendSerialPort(setLineSpace(10));
        printer.sendSerialPort(setAlignment(ALIGNMENT_LEFT));
        printer.sendSerialPort(setSizeChar(0, 0, 0, 0));
        printer.sendSerialPort(setSizeChinese(0, 0, 0, 0));
        setNextLine(printer);
        printer.sendSerialPort(builder.toString().getBytes());
        setNextLine(printer);
        setNextLine(printer);

    }

    /******
     * 挑行
     * @param printer
     */
    public static void setNextLine(PrinterManager printer){

        printer.sendSerialPort(new byte[]{10});
    }

    /******
     * 设置字体
     * @param code
     * @return
     */
    public static byte[] setTextFont(int code){

        byte[] data = new byte[3];

        data[0] = 27;
        data[1] = 77;

        // 0、48；1、49
        data[2] = (byte) code;

        return data;
    }

    /*******
     * 设置绝对
     * @param n
     * @return
     */
    public static byte[] setAbsolutePrintPosition(int n) {
        byte[] data = new byte[]{27, 36, 0, 0};
        byte nl = (byte)(n % 256);
        byte nh = (byte)(n / 256);
        data[2] = nl;
        data[3] = nh;
        return data;
    }


    /*******
     * 发送一个内容的消息
     * @param printer
     * @param msg
     * @param code
     */
    public static void sentMessage(PrinterManager printer, String msg, int code) {

        printer.sendSerialPort(setSizeChar(0, 0, 0, 0));
        printer.sendSerialPort(setSizeChinese(0, 0, 0, 0));
        printer.sendSerialPort(setLineSpace(LINE_SPACE));
        printer.sendSerialPort(setAlignment(code));
        printer.sendSerialPort(setTextFont(0));
        printer.sendSerialPort(printString(msg, 1));

    }

    /**********
     *
     * 设置打印机声音
     * @param printer
     * @param flag  true:打开声音;false:关闭声音
     */
    public static void setSound(PrinterManager printer, boolean flag) {
        if (flag) {
            // 提示音 9, 9播放声音，1，1关闭声音
            printer.sendSerialPort(new byte[]{27, 66, 9, 9});
        } else {
            // 提示音 9, 9播放声音，1，1关闭声音
            printer.sendSerialPort(new byte[]{27, 66, 1, 1});

        }
    }


    /*********
     * 空格（相对位置）
     * @param printer
     */
    public static void setBlankSpace(PrinterManager printer) {
        byte[] data = new byte[]{27, 92, 0, 0};
        byte nl = (byte) (40 % 256);
        byte nh = (byte) (40 / 256);
        data[2] = nl;
        data[3] = nh;
        printer.sendSerialPort(data);
    }

    /*********
     * 空格（相对位置）
     * @param printer
     * @param size 大小
     */
    public static void setBlankSpace(PrinterManager printer, int size) {
        byte[] data = new byte[]{27, 92, 0, 0};
        byte nl = (byte) (size % 256);
        byte nh = (byte) (size / 256);
        data[2] = nl;
        data[3] = nh;
        printer.sendSerialPort(data);
    }
}
