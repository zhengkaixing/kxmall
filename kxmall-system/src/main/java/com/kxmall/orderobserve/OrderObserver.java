package com.kxmall.orderobserve;

/**
 * 监听器
 *
 * @author kaixin
 */
public interface OrderObserver {

    /**
     * 处理
     *
     * @param obj 默认传参对象
     */
    void doSomeThing(OrderUpdater obj);

}
