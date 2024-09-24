package com.kxmall.order.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class OrderTimeDataVo implements Serializable {

    /**今日成交额*/
    private Double todayPrice;

    /**今日订单数*/
    private Long todayCount;

    /**昨日成交额*/
    private Double proPrice;

    /**昨日订单数*/
    private Long proCount;

    /**本月成交额*/
    private Double monthPrice;

    /**本月订单数*/
    private Long monthCount;

    /**上周订单数*/
    private Long lastWeekCount;

    /**上周成交额*/
    private Double lastWeekPrice;
}
