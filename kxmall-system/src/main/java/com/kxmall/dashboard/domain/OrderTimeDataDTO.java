package com.kxmall.dashboard.domain;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class OrderTimeDataDTO implements Serializable {
    private Double todayPrice;  //今日成交额
    private Integer todayCount; //今日订单数
    private Double proPrice;  //昨日成交额
    private Integer proCount;//昨日订单数
    private Double monthPrice;//本月成交额
    private Integer monthCount;//本月订单数

    private Integer lastWeekCount;//上周
    private Double lastWeekPrice; //上周

    private Long userCount;
    private Long orderCount;
    private Double priceCount;
    private Long goodsCount;
}
