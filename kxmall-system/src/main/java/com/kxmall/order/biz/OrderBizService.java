package com.kxmall.order.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.enums.PayMethodEnum;
import com.kxmall.common.enums.UserLoginType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.wechat.WxPayConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by admin on 2019/7/10.
 */
@Service
public class OrderBizService {

    private static final String ORDER_STATUS_LOCK = "ORDER_STATUS_LOCK_";

    //订单退款乐观锁
    public static final String ORDER_REFUND_LOCK = "ORDER_REFUND_LOCK_";

    private static final Logger logger = LoggerFactory.getLogger(OrderBizService.class);

    @Autowired
    private KxStoreOrderMapper orderMapper;

    @Autowired
    private KxUserMapper userMapper;

    public Boolean changeOrderStatus(String orderId, Integer nowStatus, KxStoreOrder updateOrderDO) {
        try {
            // 防止传入值为空,导致其余订单被改变
            if (orderId == null || updateOrderDO == null) {
                throw new ServiceException("订单状态流转失败！");
            }

            if (orderMapper.update(updateOrderDO,
                    new QueryWrapper<KxStoreOrder>()
                            .eq("order_id", orderId)
                            .eq("status", nowStatus)) > 0) {
                return true;
            }
            throw new ServiceException("订单状态流转失败！");

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[订单状态流转] 异常", e);
            throw new ServiceException("订单系统未知异常");
        }
    }


    public Boolean updateOrderStatus(String orderNo, Integer status) throws ServiceException {
        KxStoreOrder updateKxStoreOrder = KxStoreOrder.builder().build();
        updateKxStoreOrder.setOrderId(orderNo);
        updateKxStoreOrder.setStatus(status);
        return updateOrder(orderNo, updateKxStoreOrder);
    }

    public Boolean updateOrder(String orderNo, KxStoreOrder kxStoreOrder) {
        Lock lock = RedisUtils.lock(ORDER_STATUS_LOCK + orderNo);
        try {
            // 防止传入值为空,导致其余订单被改变
            if (orderNo == null || kxStoreOrder == null) {
                throw new ServiceException("订单状态流转失败！");
            }
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                // kxStoreOrder 是要被更新的数据，orderNo是条件
                if (orderMapper.update(kxStoreOrder, new QueryWrapper<KxStoreOrder>().eq("order_id", orderNo)) > 0) {
                    return true;
                }
                throw new ServiceException("订单状态流转失败！");
            } else {
                throw new ServiceException("订单系统繁忙~");
            }

        } catch (Exception e) {
            logger.error("[订单状态扭转] 异常", e);
            throw new ServiceException("订单系统未知异常");
        } finally {
            lock.unlock();
        }
    }

    public KxStoreOrder checkOrderExist(String orderId, Long userId) {
        QueryWrapper<KxStoreOrder> wrapper = new QueryWrapper<KxStoreOrder>().eq("order_id", orderId);
        if (userId != null) {
            wrapper.eq("uid", userId);
        }
        List<KxStoreOrder> storeOrderList = orderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(storeOrderList)) {
            throw new ServiceException("订单不存在");
        }
        return storeOrderList.get(0);
    }


    public String groupShopStatusRefund(String orderNo) throws ServiceException {
        Lock lock = RedisUtils.lock(ORDER_REFUND_LOCK + orderNo);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                //1.校验订单状态是否处于团购状态中
                KxStoreOrder orderDO = checkOrderExist(orderNo, null);
                if (orderDO.getStatus() != OrderStatusType.GROUP_SHOP_WAIT.getCode()) {
                    throw new ServiceException("订单状态不是团购状态");
                }
                //2.退款处理
                //2.1.1 先流转状态
                KxStoreOrder updateKxStoreOrder = KxStoreOrder.builder().build();
                updateKxStoreOrder.setStatus(OrderStatusType.REFUNDED.getCode());
                updateKxStoreOrder.setUpdateTime(new Date());
                changeOrderStatus(orderNo, OrderStatusType.GROUP_SHOP_WAIT.getCode(), updateKxStoreOrder);
                Long userId = orderDO.getUid();
                KxUser userDO = userMapper.selectById(userId);
                Integer loginType = userDO.getLoginType();

                //2.1.2 向微信支付平台发送退款请求
                WxPayService wxPayService = WxPayConfiguration.getPayService(loginType == UserLoginType.MP_WEIXIN.getCode() ? PayMethodEnum.MINI : PayMethodEnum.APP);
                WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                wxPayRefundRequest.setOutTradeNo(orderNo);
                wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);
                wxPayRefundRequest.setRefundDesc("团购失败退款");
                BigDecimal bigDecimal = new BigDecimal("100");
                wxPayRefundRequest.setTotalFee(orderDO.getPayPrice().subtract(orderDO.getFreightPrice()).multiply(bigDecimal).intValue());
                wxPayRefundRequest.setRefundFee(orderDO.getPayPrice().subtract(orderDO.getFreightPrice()).multiply(bigDecimal).intValue());
                WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
                if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new ServiceException(wxPayRefundResult.getReturnMsg(), 500);
                }
                if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new ServiceException(wxPayRefundResult.getReturnMsg(), 500);
                }
                return "ok";
            } else {
                throw new ServiceException("系统繁忙~");
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[微信退款] 异常", e);
            throw new ServiceException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }


}
