package com.kxmall.order.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: kxmall
 * @date: 2020/03/08 14:44
 **/
@Data
public class KxOrderStatisticalVo implements Serializable {

    /**
     * 待配货
     */
    private Integer waitingCount;
    /**
     * 配货中
     */
    private Integer beingDistributed;
    /**
     * 待配送
     */
    private Integer waitingToBeDelivered;
    /**
     * 配送中
     */
    private Integer inDelivery;

    /**
     * 异常订单
     */
    private Integer abnormalOrder;

    /**
     * 完成订单
     */
    private Integer completedCount;



}
