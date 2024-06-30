package com.kxmall.web.controller.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.enums.GroupShopAutomaticRefundType;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.enums.StatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.group.domain.KxGroupShop;
import com.kxmall.group.mapper.KxGroupShopMapper;
import com.kxmall.order.biz.OrderBizService;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.product.mapper.KxStoreProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * 定时任务
 */
@Component
@EnableScheduling
public class CheckQuartz {

    private static final Logger logger = LoggerFactory.getLogger(CheckQuartz.class);
    private static final String ORDER_STATUS_LOCK = "ORDER_STATUS_QUARTZ_LOCK";


    @Autowired
    private KxStoreOrderMapper orderMapper;

    @Autowired
    private OrderBizService orderBizService;


    /**
     * 订单状态定时轮训
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkOrderStatus() {
        Lock lock = RedisUtils.lock(ORDER_STATUS_LOCK);
        boolean isLocked;
        try {
            isLocked = lock.tryLock(15, TimeUnit.SECONDS);
            if (isLocked) {
                Date now = new Date();
                List<String> nos = orderMapper.selectExpireOrderNos(OrderStatusType.UNPAY.getCode(), new Date(now.getTime() - 1000l * 60 * 15));
                if (!CollectionUtils.isEmpty(nos)) {
                    nos.forEach(no -> {
                        try {
                            KxStoreOrder updateOrderDO = KxStoreOrder.builder().build();
                            updateOrderDO.setStatus(OrderStatusType.CANCELED_SYS.getCode());
                            updateOrderDO.setUpdateTime(now);
                            orderBizService.changeOrderStatus(no, OrderStatusType.UNPAY.getCode(), updateOrderDO);
                        } catch (Exception e) {
                            logger.error("[未付款检测] 异常", e);
                        }
                    });
                }
                //15分钟执行一次
                long minutes = (now.getTime() / (1000 * 60));
                if (minutes % 15 == 0) {
                    List<String> waitConfirmNos = orderMapper.selectExpireOrderNos(OrderStatusType.WAIT_CONFIRM.getCode(), new Date(now.getTime() - 1000l * 60 * 60 * 24 * 7));
                    waitConfirmNos.forEach(item -> {
                        try {
                            KxStoreOrder updateOrderDO = KxStoreOrder.builder().build();
                            updateOrderDO.setStatus(OrderStatusType.WAIT_APPRAISE.getCode());
                            updateOrderDO.setUpdateTime(now);
                            orderBizService.changeOrderStatus(item, OrderStatusType.WAIT_CONFIRM.getCode(), updateOrderDO);
                        } catch (Exception e) {
                            logger.error("[未确认检测] 异常", e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[订单状态检测定时任务] 异常", e);
        } finally {
            lock.unlock();
        }

    }


}
