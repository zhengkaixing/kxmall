package com.kxmall.web.controller.order.builder;

import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.bo.OrderPriceBo;
import com.kxmall.order.domain.bo.OrderRequestBo;

/**
 * @description: 指挥者
 * @author: fy
 * @date: 2020/03/13 13:05
 **/
public class OrderDirector {

    private OrderBuilder builder;

    public OrderDirector(OrderBuilder builder) {
        this.builder = builder;
    }

    //构建与组装方法
    public void constructOrder(KxStoreOrder orderDO, final OrderRequestBo orderRequest, String channel, Long userId) {
        builder.buildOrderCheckHandlePart(orderRequest, userId);
        OrderPriceBo orderPriceBo = builder.buildOrderPriceHandlePart(orderDO, orderRequest, userId);
        builder.buildOrderHandlePart(orderDO, orderPriceBo, orderRequest, channel, userId);
        builder.buildCoupontHandlePart(orderDO);
        builder.buildOrderSkuHandlePart(orderDO, orderPriceBo, orderRequest);
        builder.buildCartHandlePart(orderRequest, userId);
        builder.buildNotifyHandlePart(orderDO);
    }

}
