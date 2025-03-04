package com.kxmall.order.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: kxmall
 * @date: 2020/03/03 16:18
 **/
@Data
public class RiderProductBO implements Serializable {

    // 商品名称
    private String spuName;

    // 数量
    private String amount;

    // 商品URL
    private String url;

    // 单价
    private BigDecimal unitPrice;

    // 优惠单价格
    private BigDecimal preferentialPrice;

}
