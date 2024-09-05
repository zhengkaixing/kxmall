package com.kxmall.orderobserve;

/**
 * 监听器
 *
 * @author 郅兴开源团队-小黑
 */
public interface OrderObserver {

    /**
     * 处理
     *
     * @param obj 默认传参对象
     */
    void doSomeThing(OrderUpdater obj);

}
