package com.kxmall.order.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by admin on 2019/7/6.
 */
@Data
public class DeliveryRequestBo {


    /**
     * 配送费
     */
    private BigDecimal totalPrice;

    /**
     * 配送信息
     */
    private String msg;

}
