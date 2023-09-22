package com.kxmall.orderobserve;

import com.kxmall.order.domain.KxStoreOrder;

/**
 * 观察者对象
 *
 * @author kaixin
 */
public class OrderUpdater {

    /**
     * 订单对象
     */
    private KxStoreOrder orderDo;


    /**
     * 默认构造参数
     *
     * @param orderDo 订单对象
     */
    public OrderUpdater(KxStoreOrder orderDo) {
        this.orderDo = orderDo;
    }

    public KxStoreOrder getOrderDo() {
        return orderDo;
    }

    public void setOrderDo(KxStoreOrder orderDo) {
        this.orderDo = orderDo;
    }
}
