package com.kxmall.web.controller.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.vo.KxStoreOrderVo;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppOrderService {


    /**
     * 微信预付订单
     * @param orderId
     * @param userId
     * @param loginType
     * @param openId
     * @return
     */
    Object wxPrepay(String orderId, Long userId, Integer loginType, String openId);

    /**
     * 查询订单
     * @param wrapper
     * @return
     */
    List<KxStoreOrder> selectListByWrapper(QueryWrapper<KxStoreOrder> wrapper);

    /**
     * 更新状态
     * @param orderId
     * @param nowStatus
     * @param updateOrderDO
     */
    Boolean changeOrderStatus(String orderId, Integer nowStatus, KxStoreOrder updateOrderDO);

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param state
     * @param userId
     * @return
     */
    TableDataInfo<KxStoreOrderVo> getOrderPage(Integer pageNo, Integer pageSize, String state, Long userId);

    /**
     * 取消订单
     * @param orderId
     * @param userId
     * @return
     */
    String cancel(String orderId, Long userId);

    /**
     * 确认订单
     * @param orderId
     * @param userId
     * @return
     */
    String confirm(String orderId, Long userId);

    /**
     * 获取订单详情
     * @param orderId
     * @param userId
     * @return
     */
    KxStoreOrderVo getOrderDetail(Long orderId, Long userId);
}
