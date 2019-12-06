/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  PrinterImageUtils
 * Created by  ianchang on 2018-08-03 13:04:41
 * Last modify date   2018-08-03 13:04:40
 */

package com.ian.printer.printer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by ianchang on 2018/8/3.
 */

public class PrinterImageUtils {


    public static Bitmap compressPic(Bitmap bitmap) {
        int i = bitmap.getWidth();
        int j = bitmap.getHeight();
        Bitmap localBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(localBitmap);
        canvas.drawColor(-1);
        canvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 150, 150), null);
        return localBitmap;
    }

    public static byte[] draw2PxPoint(Bitmap bitmap) {
        byte[] arrayOfByte = new byte[bitmap.getWidth() * bitmap.getHeight() / 8 + 1000];
        int j = 0 + 1;
        arrayOfByte[0] = 27;
        int i = j + 1;
        arrayOfByte[j] = 51;
        j = i + 1;
        arrayOfByte[i] = 0;
        i = j + 1;
        arrayOfByte[j] = 27;
        int k = i + 1;
        arrayOfByte[i] = 97;
        j = k + 1;
        arrayOfByte[k] = 1;
        i = 0;
        while (i < bitmap.getHeight() / 24.0F) {
            k = j + 1;
            arrayOfByte[j] = 27;
            j = k + 1;
            arrayOfByte[k] = 42;
            k = j + 1;
            arrayOfByte[j] = 33;
            int m = k + 1;
            arrayOfByte[k] = ((byte) (bitmap.getWidth() % 256));
            arrayOfByte[m] = ((byte) (bitmap.getWidth() / 256));
            j = 0;
            k = m + 1;
            while (j < bitmap.getWidth()) {
                m = 0;
                while (m < 3) {
                    int n = 0;
                    while (n < 8) {
                        int i1 = px2Byte(j, i * 24 + m * 8 + n, bitmap);
                        arrayOfByte[k] = ((byte) (arrayOfByte[k] + (arrayOfByte[k] + i1)));
                        n += 1;
                    }
                    k += 1;
                    m += 1;
                }
                j += 1;
            }
            arrayOfByte[k] = 10;
            i += 1;
            j = k + 1;
        }
        i = j + 1;
        arrayOfByte[j] = 27;
        j = i + 1;
        arrayOfByte[i] = 50;
        byte[] data = new byte[j];
        System.arraycopy(arrayOfByte, 0, data, 0, j);
        return data;
    }

    public static byte px2Byte(int paramInt1, int paramInt2, Bitmap paramBitmap) {
        if ((paramInt1 < paramBitmap.getWidth()) && (paramInt2 < paramBitmap.getHeight())) {
            paramInt1 = paramBitmap.getPixel(paramInt1, paramInt2);
            if (rgb2Gray((0xFF0000 & paramInt1) >> 16, (0xFF00 & paramInt1) >> 8, paramInt1 & 0xFF) < 128) {
                return 1;
            }
            return 0;
        }
        return 0;
    }

    public static int rgb2Gray(int paramInt1, int paramInt2, int paramInt3) {
        return (int) (0.299D * paramInt1 + 0.587D * paramInt2 + 0.114D * paramInt3);
    }


    /**
     * 打印二维码
     * @param qrCode
     * @return
     */
    public static String getQrCodeCmd(String qrCode, int size) {
        byte[] data;
        int store_len = qrCode.length() + 3;
        byte store_pL = (byte) (store_len % 256);
        byte store_pH = (byte) (store_len / 256);

        // QR Code: Select the model
        // Hex      1D      28      6B      04      00      31      41      n1(x32)     n2(x00) - size of model
        // set n1 [49 x31, model 1] [50 x32, model 2] [51 x33, micro qr code]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=140
        byte[] modelQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, (byte)0x04, (byte)0x00, (byte)0x31, (byte)0x41, (byte)0x32, (byte)0x00};

        // QR Code: Set the size of module
        // Hex      1D      28      6B      03      00      31      43      n
        // n depends on the printer 设置二维码大小
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=141
//        byte[] sizeQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, (byte)0x03, (byte)0x00, (byte)0x31, (byte)0x43, (byte)0x08};
        byte[] sizeQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, (byte)0x03, (byte)0x00, (byte)0x31, (byte)0x43, (byte)size};

        // Hex      1D      28      6B      03      00      31      45      n
        // Set n for error correction [48 x30 -> 7%] [49 x31-> 15%] [50 x32 -> 25%] [51 x33 -> 30%]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=142
        byte[] errorQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, (byte)0x03, (byte)0x00, (byte)0x31, (byte)0x45, (byte)0x31};

        // QR Code: Store the data in the symbol storage area
        // Hex      1D      28      6B      pL      pH      31      50      30      d1...dk
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=143
        //                        1D          28          6B         pL          pH  cn(49->x31) fn(80->x50) m(48->x30) d1…dk
        byte[] storeQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, store_pL, store_pH, (byte)0x31, (byte)0x50, (byte)0x30};

        // QR Code: Print the symbol data in the symbol storage area
        // Hex      1D      28      6B      03      00      31      51      m
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=144
        byte[] printQR = {(byte)0x1d, (byte)0x28, (byte)0x6b, (byte)0x03, (byte)0x00, (byte)0x31, (byte)0x51, (byte)0x30};

        data = byteMerger(modelQR, sizeQR);
        data = byteMerger(data, errorQR);
        data = byteMerger(data, storeQR);
        data = byteMerger(data, qrCode.getBytes());
        data = byteMerger(data, printQR);

        return new String(data);
    }

    /**
     * 字节数组合并
     * @param bytesA
     * @param bytesB
     * @return
     */
    public static byte[] byteMerger(byte[] bytesA, byte[] bytesB) {
        byte[] bytes = new byte[bytesA.length + bytesB.length];
        System.arraycopy(bytesA, 0, bytes, 0, bytesA.length);
        System.arraycopy(bytesB, 0, bytes, bytesA.length, bytesB.length);
        return bytes;
    }


    /**
     * 打印条码
     * @param barcode
     * @return
     */
    public static String getBarcodeCmd(String barcode) {
        // 打印 Code-128 条码时需要使用字符集前缀
        // "{A" 表示大写字母
        // "{B" 表示所有字母，数字，符号
        // "{C" 表示数字，可以表示 00 - 99 的范围


        byte[] data;

        String btEncode;

        if (barcode.length() < 18) {
            // 字符长度小于15的时候直接输出字符串
            btEncode = "{B" + barcode;
        } else {
            // 否则做一点优化

            int startPos = 0;
            btEncode = "{B";

            for (int i = 0; i < barcode.length(); i++) {
                char curChar = barcode.charAt(i);

                if (curChar < 48 || curChar > 57 || i == (barcode.length() - 1)) {
                    // 如果是非数字或者是最后一个字符

                    if (i - startPos >= 10) {
                        if (startPos == 0) {
                            btEncode = "";
                        }

                        btEncode += "{C";

                        boolean isFirst = true;
                        int numCode = 0;

                        for (int j = startPos; j < i; j++) {

                            if (isFirst) { // 处理第一位
                                numCode = (barcode.charAt(j) - 48) * 10;
                                isFirst = false;
                            } else { // 处理第二位
                                numCode += (barcode.charAt(j) - 48);
                                btEncode += (char) numCode;
                                isFirst = true;
                            }

                        }

                        btEncode += "{B";

                        if (!isFirst) {
                            startPos = i - 1;
                        } else {
                            startPos = i;
                        }
                    }

                    for (int k = startPos; k <= i; k++) {
                        btEncode += barcode.charAt(k);
                    }
                    startPos = i + 1;
                }

            }
        }


        // 设置 HRI 的位置，02 表示下方
        byte[] hriPosition = {(byte) 0x1d, (byte) 0x48, (byte) 0x02};
        // 最后一个参数表示宽度 取值范围 1-6 如果条码超长则无法打印
        byte[] width = {(byte) 0x1d, (byte) 0x77, (byte) 0x02};
        byte[] height = {(byte) 0x1d, (byte) 0x68, (byte) 0xfe};
        // 最后两个参数 73 ： CODE 128 || 编码的长度
        byte[] barcodeType = {(byte) 0x1d, (byte) 0x6b, (byte) 73, (byte) btEncode.length()};
        byte[] print = {(byte) 10, (byte) 0};

        data = byteMerger(hriPosition, width);
        data = byteMerger(data, height);
        data = byteMerger(data, barcodeType);
        data = byteMerger(data, btEncode.getBytes());
        data = byteMerger(data, print);

        return new String(data);
    }

}
