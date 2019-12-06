/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BillInfo
 * Created by  ianchang on 2018-08-08 09:20:18
 * Last modify date   2018-08-08 09:20:18
 */

package com.ian.printer.bean;

import java.util.List;

/**
 * Created by ianchang on 2018/8/8.
 */

public class BillInfo {

    public String storeName; // 门店名称
    public String billName; // 小票名称
    public String deviceName; // 设备名称

    public String transactNum; // 交易号

    public List<GoodInfo> goodsInfo; // 商品

    public int count; // 件数

    public float amount; // 金额
    public String paymentMethod; // 支付方式

    public float payOut; // 支付金额

    public String memberNum; // 会员号

    public float scoreUp; // 累积积分
    public float consumePoint; // 消费积分

    public String billNum; // 开票号

    public String qrcode = "http://www.baidu.com"; // 二维码信息
    public String qrcodeLabel = "加入社群优惠福利"; // 二维码标题
    public String wishSlogan = "感谢您的惠顾，欢迎下次光临"; // 祝福语
    public String addressOfStore = "青浦区盈满港路555号"; // 门店地址
    public String telphoneOfStore = "（021）59855985"; // 门店地址
    public String fallowOfWeChat = "欢迎关注微信[世纪联华]公众号";

}
