package com.kxmall.web.controller.order.builder;


import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.bo.OrderPriceBo;
import com.kxmall.order.domain.bo.OrderRequestBo;

/**
 * @description: 抽象建造者
 * @author: fy
 * @date: 2020/03/13 13:04
 **/
public abstract class OrderBuilder {

    /**
     * 1.订单初始创建校验部分
     */
    public abstract void buildOrderCheckHandlePart(OrderRequestBo orderRequest, Long userId);

    /**
     * 2.订单价格处理部分
     */
    public abstract OrderPriceBo buildOrderPriceHandlePart(KxStoreOrder orderDO, OrderRequestBo orderRequest, Long userId);

    /**
     * 3.构建订单部分
     */
    public abstract void buildOrderHandlePart(KxStoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest, String channel, Long userId);

    /**
     * 4.更新优惠券部分
     */
    public abstract void buildCoupontHandlePart(KxStoreOrder orderDO);

    /**
     * 5.订单商品SKU部分
     */
    public abstract void buildOrderSkuHandlePart(KxStoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest);

    /**
     * 6.购物车部分
     */
    public abstract void buildCartHandlePart(OrderRequestBo orderRequest, Long userId);

    /**
     * 7.触发订单创建完成后通知事件部分
     */
    public abstract void buildNotifyHandlePart(KxStoreOrder orderDO);
}
