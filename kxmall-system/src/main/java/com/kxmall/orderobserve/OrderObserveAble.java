package com.kxmall.orderobserve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Vector;

/**
 * 活动类观察者
 *
 * @author kaixin
 */
@Component
public class OrderObserveAble implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OrderObserveAble.class);

    //InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候会执行该方法。

    /**
     * 上下文
     */
    @Resource
    private ApplicationContext context;
    /**
     * 被观察者集合
     */
    private Vector<OrderObserver> observers;


    public OrderObserveAble() {
        this.observers = new Vector<>();
    }

    private synchronized void addObserver(OrderObserver monitor) {
        if (monitor == null) {
            return;
        }
        if (!observers.contains(monitor)) {
            observers.add(monitor);
        }
    }

    public synchronized void deleteObserver(OrderObserver monitor) {
        if (monitor == null) {
            return;
        }
        if (observers.isEmpty()) {
            return;
        }
        observers.remove(monitor);
    }

    //异步执行
    @Async
    public synchronized void notifyObservers(OrderUpdater updater) {
        if (observers.isEmpty()) {
            return;
        }
        for (OrderObserver observer : observers) {
            try {
                observer.doSomeThing(updater);
            } catch (Exception e) {
                logger.error("通知失败", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, OrderObserver> beans = context.getBeansOfType(OrderObserver.class);
        if (beans.isEmpty()) {
            return;
        }
        for (String name : beans.keySet()) {
            addObserver(beans.get(name));
        }
    }
}
