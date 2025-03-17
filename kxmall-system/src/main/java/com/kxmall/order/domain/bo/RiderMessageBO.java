package com.kxmall.order.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: fy
 * @date: 2020/03/03 19:50
 **/
@Data
public class RiderMessageBO implements Serializable {

    // 订单编号
    private String orderNo;

    // 配送状态
    private Integer orderRiderStatus;

    // 配送员主键ID
    private Long riderId;

    // 配送异常【如果有】
    private String errorMsg;
}
