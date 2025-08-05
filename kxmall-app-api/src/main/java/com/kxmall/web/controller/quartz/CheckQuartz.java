package com.kxmall.web.controller.quartz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.kxmall.web.controller.order.service.IKxAppOrderService;
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
import org.springframework.util.ObjectUtils;

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
    private static final String GROUP_SHOP_START_LOCK = "GROUP_SHOP_START_LOCK";
    private static final String GROUP_SHOP_END_LOCK = "GROUP_SHOP_END_LOCK";
    private static final String GROUP_SHOP_LOCK_LOCK = "GROUP_SHOP_LOCK_LOCK";


    @Autowired
    private KxStoreOrderMapper orderMapper;
    @Autowired
    private OrderBizService orderBizService;
    @Autowired
    private KxGroupShopMapper groupShopMapper;
    @Autowired
    private KxStoreProductMapper storeProductMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Autowired
    private IKxAppOrderService appOrderService;

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
                            //将冻结库存还回去
                            orderBizService.callbackStock(no);
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

                            KxStoreOrder kxStoreOrder = orderMapper.selectOne(new LambdaQueryWrapper<KxStoreOrder>().eq(KxStoreOrder::getOrderId, item));
                            if (!ObjectUtils.isEmpty(kxStoreOrder)) {
                                appOrderService.confirm(kxStoreOrder.getOrderId(), kxStoreOrder.getUid());
                            }
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

    /**
     * 设定60s跑一次,团购商品到期自动退款,改变状态
     */
    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void groupShopStart() throws Exception {
        Lock lock = RedisUtils.lock(GROUP_SHOP_START_LOCK);
        boolean isLocked;

        try {
            isLocked = lock.tryLock(30, TimeUnit.SECONDS);
            if (isLocked) {
                Date now = new Date();
                /**
                 * 1. 激活 团购时间开始的活动商品
                 */
                // 1.1 查询在活动期间,且冻结状态的团购商品
                List<KxGroupShop> groupShops = groupShopMapper.selectList(new QueryWrapper<KxGroupShop>()
                        .le("start_time", now)
                        .gt("end_time", now)
                        .eq("status", StatusType.LOCK.getCode()));
                if (groupShops != null) {
                    for (KxGroupShop groupShopDO : groupShops) {
                        groupShopDO.setUpdateTime(now);
                        groupShopDO.setStatus(StatusType.ACTIVE.getCode());
                        KxStoreProduct storeProduct = storeProductMapper.selectById(groupShopDO.getProductId());
                        if (storeProduct == null) {
                            throw new ServiceException("团购商品不存在");
                        }

                        // 1.2 检查商品是不是已经下架
                        if (storeProduct.getIsShow().equals(StatusType.ACTIVE.getCode())) {
                            if (groupShopMapper.updateById(groupShopDO) <= 0) {
                                throw new ServiceException("团购商品更新失败");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[团购开始 定时任务] 异常", e);
            throw e;
        } finally {
            lock.unlock();
        }

    }

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void groupShopEnd() throws Exception {
        Lock lock = RedisUtils.lock(GROUP_SHOP_END_LOCK);
        boolean isLocked;

        try {
            isLocked = lock.tryLock(30, TimeUnit.SECONDS);
            if (isLocked) {
                Date now = new Date();
                /**
                 * 2. 冻结 团购时间结束的活动商品,并根据对应情况处理订单
                 */
                QueryWrapper<KxGroupShop> wrapper = new QueryWrapper<KxGroupShop>()
                        .eq("status", StatusType.ACTIVE.getCode())
                        .and(w->w.gt("start_time", now).or().le("end_time", now));
                List<KxGroupShop> lockGroupShopDOList = groupShopMapper.selectList(wrapper);
                // 2.2 将团购订单的状态转为对应的退款或待出库状态,对未达人数且自动退款的商品订单进行退款,对达到人数或不自动退款的商品订单转换状态
                if (!CollectionUtils.isEmpty(lockGroupShopDOList)) {
                    // 2.1 对过期团购商品冻结.
                    KxGroupShop lockGroupShopDO = new KxGroupShop();
                    lockGroupShopDO.setStatus(StatusType.LOCK.getCode());
                    lockGroupShopDO.setUpdateTime(now);
                    groupShopMapper.update(lockGroupShopDO, wrapper);
                    for (KxGroupShop groupShopDO : lockGroupShopDOList) {
                        // 2.2.1查询团购订单中数据
                        List<KxStoreOrder> lockOrderList = orderMapper.selectList(
                                new QueryWrapper<KxStoreOrder>()
                                        .eq("combination_id", groupShopDO.getId())
                                        .eq("status", OrderStatusType.GROUP_SHOP_WAIT.getCode()));

                        if (CollectionUtils.isEmpty(lockOrderList)) {
                            continue;
                        }

                        if (groupShopDO.getAutomaticRefund() == GroupShopAutomaticRefundType.YES.getCode() && groupShopDO.getAlreadyBuyNumber().compareTo(groupShopDO.getMinimumNumber()) < 0) {
                            // 2.2.2.1.退款
                            logger.info("[团购结束] 退款逻辑 groupShopId:" + groupShopDO.getId());
                            for (KxStoreOrder orderDO : lockOrderList) {
                                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                                    @Override
                                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                                        try {
                                            //对订单退款保证原子性，仅退款成功的单子，变更状态
                                            orderBizService.groupShopStatusRefund(orderDO.getOrderId());
                                            logger.info("[团购订单退款] 完成 orderNo:" + orderDO.getOrderId());
                                        } catch (Exception e) {
                                            logger.error("[团购订单退款] 异常 orderNo:" + orderDO.getOrderId() + "; errmsg:" + e.getMessage());
                                            transactionStatus.setRollbackOnly();
                                        }
                                    }
                                });
                            }
                        } else {
                            logger.info("[团购结束] 发货逻辑 groupShopId:" + groupShopDO.getId());
                            // 2.2.2.2 转换订单为待出货状态 (非自动退款场景)
                            List<Long> collect = lockOrderList.stream().map(s -> s.getId()).collect(Collectors.toList());
                            KxStoreOrder orderDO = KxStoreOrder.builder().build();
                            orderDO.setStatus(OrderStatusType.WAIT_STOCK.getCode());
                            orderMapper.update(orderDO, (
                                    new QueryWrapper<KxStoreOrder>()
                                            .in("id", collect)
                                            .eq("status", OrderStatusType.GROUP_SHOP_WAIT.getCode())));
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error("[团购结束 定时任务] 异常", e);
            throw e;
        } finally {
            lock.unlock();
        }


    }

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void groupShopLock() throws Exception {
        Lock lock = RedisUtils.lock(GROUP_SHOP_LOCK_LOCK);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                Date now = new Date();
                /**
                 * 3 冻结 团购活动期间却被下架的商品
                 */
                QueryWrapper<KxGroupShop> groupShopDOEntityWrapper = new QueryWrapper<>();

                // 3.1 从团购中查询活动期间的商品
                groupShopDOEntityWrapper.eq("status", StatusType.ACTIVE.getCode())
                        .le("start_time", now)
                        .gt("end_time", now);
                List<KxGroupShop> groupShopDOS = groupShopMapper.selectList(groupShopDOEntityWrapper);
                if (!CollectionUtils.isEmpty(groupShopDOS)) {
                    List<Long> spuIdList = groupShopDOS.stream().map(t -> t.getProductId()).collect(Collectors.toList());

                    // 3.2 在团购中查询给出spuID,是否有被下架的商品
                    List<KxStoreProduct> spuDOS = storeProductMapper.selectList(new QueryWrapper<KxStoreProduct>().in("id", spuIdList).eq("is_show", StatusType.LOCK.getCode()));
                    if (!CollectionUtils.isEmpty(spuDOS)) {
                        List<Long> collect = spuDOS.stream().map(t -> t.getId()).collect(Collectors.toList());
                        KxGroupShop groupShopDO = new KxGroupShop();
                        groupShopDO.setStatus(StatusType.LOCK.getCode());
                        groupShopDO.setUpdateTime(now);
                        groupShopMapper.update(groupShopDO, (new QueryWrapper<KxGroupShop>().in("spu_id", collect)));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[团购锁定 定时任务] 异常", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

}
