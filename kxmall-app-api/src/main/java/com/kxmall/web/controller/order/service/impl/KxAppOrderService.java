package com.kxmall.web.controller.order.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.kxmall.address.domain.KxAddress;
import com.kxmall.address.mapper.KxAddressMapper;
import com.kxmall.common.core.domain.model.LoginUser;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.*;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.helper.LoginHelper;
import com.kxmall.common.utils.IpUtil;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.group.mapper.KxGroupShopMapper;
import com.kxmall.order.biz.BillBizService;
import com.kxmall.order.biz.OrderBizService;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.order.domain.bo.DeliveryRequestBo;
import com.kxmall.order.domain.bo.OrderRequestBo;
import com.kxmall.order.domain.bo.OrderRequestProductBo;
import com.kxmall.order.domain.vo.KxStoreOrderProductVo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.order.mapper.KxStoreOrderProductMapper;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.system.domain.SysConfig;
import com.kxmall.system.service.ISysConfigService;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.mapper.KxUserBillMapper;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.order.service.IKxAppOrderService;
import com.kxmall.web.controller.storage.service.impl.KxAppStorageService;
import com.kxmall.wechat.WxPayConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/6
 */
@RequiredArgsConstructor
@Service
public class KxAppOrderService implements IKxAppOrderService {

    private static final Logger logger = LoggerFactory.getLogger(KxAppOrderService.class);

    private final KxStoreOrderMapper baseMapper;

    private final KxStoreOrderProductMapper orderProductMapper;

    private final KxAddressMapper addressMapper;

    private final KxStorageMapper storageMapper;

    private final OrderBizService orderBizService;

    private final KxGroupShopMapper groupShopMapper;

    private final KxUserMapper userMapper;

    private final KxUserBillMapper userBillMapper;

    private final BillBizService billBizService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object wxPrepay(String orderId, Long userId, Integer loginType, String openId) {
        Date now = new Date();
        KxStoreOrder updateOrderDO = orderBizService.checkOrderExist(orderId, userId);
        // 检测订单状态
        Integer status = updateOrderDO.getStatus();
        if (status != OrderStatusType.UNPAY.getCode()) {
            throw new ServiceException("订单状态不支持支付");
        }

        String tradeType;
        WxPayService wxPayService;
        if (UserLoginType.MP_WEIXIN.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.MINI);
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else if (UserLoginType.APP_WEIXIN.getCode() == loginType || UserLoginType.REGISTER.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.APP);
            tradeType = WxPayConstants.TradeType.APP;
        } else if (UserLoginType.H5_WEIXIN.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.H5);
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else {
            throw new ServiceException("当前平台不支持微信支付");
        }

        Object result;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(orderId);
            orderRequest.setOpenid(openId);
            String nonceStr = "ibuaiVcKdpRxkhJA";
            String body = "订单说明" + orderId;
            orderRequest.setBody(body);
            orderRequest.setAppid(wxPayService.getConfig().getAppId());
            orderRequest.setTotalFee(updateOrderDO.getPayPrice().multiply(new BigDecimal(100)).intValue());
            orderRequest.setSpbillCreateIp(IpUtil.getLocalIP());
            orderRequest.setTradeType(tradeType);
            orderRequest.setNonceStr(nonceStr);
            orderRequest.setNotifyUrl(wxPayService.getConfig().getNotifyUrl());
            result = wxPayService.createOrder(orderRequest);
        } catch (WxPayException e) {
            logger.error("[微信支付] 异常", e);
            throw new ServiceException(e.getErrCodeDes());
        } catch (Exception e) {
            logger.error("[预付款异常]", e);
            throw new ServiceException("订单系统未知异常");
        }
        return result;
    }

    @Override
    public List<KxStoreOrder> selectListByWrapper(QueryWrapper<KxStoreOrder> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<KxStoreOrderVo> selectListVoByWrapper(QueryWrapper<KxStoreOrder> wrapper) {
        return baseMapper.selectVoList(wrapper);
    }

    @Override
    public TableDataInfo<KxStoreOrderVo> getOrderPage(Integer pageNo, Integer pageSize, String state, Long userId) {
        List<Integer> status = new ArrayList<>();
        if (StringUtils.isNoneBlank(state)) {
            String[] states = state.split(",");
            for (int i = 0; i < states.length; i++) {
                status.add(Integer.parseInt(states[i]));
            }
        }
        List<KxStoreOrderVo> orderVos = baseMapper.selectOrderPages(status, (pageNo - 1) * pageSize, pageSize, userId);
        Long count = baseMapper.countOrders(status, (pageNo - 1) * pageSize, pageSize, userId);
        //封装SKU
        orderVos.forEach(item -> {
            item.setProductList(orderProductMapper.selectVoList(new QueryWrapper<KxStoreOrderProduct>().eq("order_id", item.getId())));
        });
        return new TableDataInfo<KxStoreOrderVo>(orderVos, count);
    }


    @Override
    public String cancel(String orderId, Long userId) {
        KxStoreOrder storeOrder = orderBizService.checkOrderExist(orderId, userId);
        if (storeOrder.getStatus() != OrderStatusType.UNPAY.getCode()) {
            throw new ServiceException("订单状态不支持取消");
        }
        KxStoreOrder updateOrderDO = KxStoreOrder.builder().build();
        updateOrderDO.setStatus(OrderStatusType.CANCELED.getCode());
        updateOrderDO.setUpdateTime(new Date());
        orderBizService.changeOrderStatus(orderId, OrderStatusType.UNPAY.getCode(), updateOrderDO);
        return "ok";
    }

    @Override
    public String confirm(String orderId, Long userId) {
        KxStoreOrder orderDO = orderBizService.checkOrderExist(orderId, userId);
        if (orderDO.getStatus() != OrderStatusType.WAIT_CONFIRM.getCode()) {
            throw new ServiceException("订单状态不支持确认收货");
        }
        KxStoreOrder updateOrderDO = KxStoreOrder.builder().build();
        updateOrderDO.setStatus(OrderStatusType.WAIT_APPRAISE.getCode());
        updateOrderDO.setUpdateTime(new Date());
        orderBizService.changeOrderStatus(orderId, OrderStatusType.WAIT_CONFIRM.getCode(), updateOrderDO);

        //积分发放
        this.pointRelease(orderDO);

        return "ok";
    }

    private void pointRelease(KxStoreOrder orderDO) {
        if (orderDO.getGainIntegral().compareTo(BigDecimal.ZERO) > 0) {
            KxUser user = userMapper.selectById(orderDO.getUid());

            //用户积分添加
            BigDecimal newIntegral = NumberUtil.add(user.getIntegral(), orderDO.getGainIntegral());
            user.setIntegral(newIntegral);
            user.setUid(orderDO.getUid());
            userMapper.updateById(user);

            //增加流水
            billBizService.income(user.getUid(), "购买商品赠送积分", BillDetailEnum.CATEGORY_2.getValue(), BillDetailEnum.TYPE_9.getValue(), orderDO.getGainIntegral().doubleValue(), newIntegral.doubleValue(), "购买商品赠送" + orderDO.getGainIntegral() + "积分", orderDO.getId().toString());
        }
    }

    @Override
    public KxStoreOrderVo getOrderDetail(Long orderId, Long userId) {
        QueryWrapper<KxStoreOrder> wrapper = new QueryWrapper<KxStoreOrder>().eq("id", orderId);
        if (userId != null) {
            wrapper.eq("uid", userId);
        }
        List<KxStoreOrderVo> storeOrderVos = baseMapper.selectVoList(wrapper);
        if (CollectionUtils.isEmpty(storeOrderVos)) {
            throw new ServiceException("订单并不存在");
        }
        KxStoreOrderVo orderDTO = storeOrderVos.get(0);
        List<KxStoreOrderProductVo> storeOrderProductVoList = orderProductMapper.selectVoList(new QueryWrapper<KxStoreOrderProduct>().eq("order_id", orderId));
        orderDTO.setProductList(storeOrderProductVoList);
        if (orderDTO.getCombinationId() != null && orderDTO.getCombinationId() != 0L) {
            orderDTO.setGroupShopVo(groupShopMapper.selectVoById(orderDTO.getCombinationId()));
        }
        return orderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refund(String orderId, Long userId) {
        KxStoreOrder orderDO = orderBizService.checkOrderExist(orderId, userId);
        if (PayChannelType.OFFLINE.getCode().equals(orderDO.getPayChannel())) {
            throw new ServiceException("订单支付方式不支持退款");
        }
        if (OrderStatusType.refundable(orderDO.getStatus())) {
            KxStoreOrder updateKxStoreOrder = KxStoreOrder.builder().build();
            updateKxStoreOrder.setStatus(OrderStatusType.REFUNDED.getCode());
            orderBizService.changeOrderStatus(orderId, orderDO.getStatus(), updateKxStoreOrder);
            // GlobalExecutor.execute(() -> {
            KxStoreOrderVo orderDTO = new KxStoreOrderVo();
            BeanUtils.copyProperties(orderDO, orderDTO);
            List<KxStoreOrderProductVo> orderProductVos = orderProductMapper.selectVoList(new QueryWrapper<KxStoreOrderProduct>().eq("order_no", orderDO.getOrderId()));
            orderDTO.setProductList(orderProductVos);
            // adminNotifyBizService.refundOrder(orderDTO);
            //});
            if (PayChannelType.BALANCE.getCode().equals(orderDO.getPayChannel())) {
                //余额退款
                balanceRefund(orderDTO);
                return "ok";
            } else if (refundDirect(orderDTO)) {
                return "ok";
            }


        }
        throw new ServiceException("订单状态不支持退款");
    }

    /**
     * 余额退款
     *
     * @param orderDTO
     */
    private void balanceRefund(KxStoreOrderVo orderDTO) {
        String orderNo = orderDTO.getOrderId();
        //1.校验订单状态是否处于团购状态中
        KxStoreOrder orderDO = orderBizService.checkOrderExist(orderNo, null);
        if (OrderStatusType.refundable(orderDO.getStatus())) {
            throw new ServiceException("订单状态不支持退款");
        }

        KxUser kxUser = userMapper.selectById(orderDO.getUid());
        //扣款
        KxUser userUpdate = new KxUser();
        userUpdate.setNowMoney(kxUser.getNowMoney().add(orderDO.getPayPrice()));
        userMapper.update(userUpdate, new LambdaQueryWrapper<KxUser>().eq(KxUser::getNowMoney, kxUser.getNowMoney()));

        //扣款记录
        String mark = "系统增加了" + orderDO.getPayPrice().toString() + "余额";
        Double newMoney = NumberUtil.sub(kxUser.getNowMoney(), orderDO.getPayPrice()).doubleValue();
        if (newMoney < 0) {
            newMoney = 0d;
        }

        KxUserBill userBill = KxUserBill.builder().uid(kxUser.getUid()).title("商品退款余额").category(BillDetailEnum.CATEGORY_1.getValue()).type(BillDetailEnum.TYPE_5.getValue()).number(orderDO.getPayPrice()).balance(BigDecimal.valueOf(newMoney)).mark(mark).pm(BillEnum.PM_1.getValue()).build();
        userBillMapper.insert(userBill);

        //库存回退，从锁定量扣回可销售量
        orderBizService.callbackStock(orderNo);
    }

    private Boolean refundDirect(KxStoreOrderVo orderDTO) throws ServiceException {
        String orderNo = orderDTO.getOrderId();
        Lock lock = RedisUtils.lock(OrderBizService.ORDER_REFUND_LOCK);
        try {
            boolean tryLock = lock.tryLock(30L, TimeUnit.SECONDS);
            if (tryLock) {
                //1.校验订单状态是否处于团购状态中
                KxStoreOrder orderDO = orderBizService.checkOrderExist(orderNo, null);
                if (OrderStatusType.refundable(orderDO.getStatus())) {
                    throw new ServiceException("订单状态不支持退款");
                }
                //2.退款处理
                //2.1.1 先流转状态
                Long userId = orderDO.getUid();
                LoginUser loginUser = LoginHelper.getLoginUser();
                if (!loginUser.getUserId().equals(userId)) {
                    throw new ServiceException("退款用户与当前登录用户不一致！无法退款");
                }
                Integer loginType = loginUser.getLoginType();

                //todo
                //2.1.2 向微信支付平台发送退款请求
                WxPayService wxPayService = WxPayConfiguration.getPayService(loginType == UserLoginType.MP_WEIXIN.getCode() ? PayMethodEnum.MINI : PayMethodEnum.APP);
                WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                wxPayRefundRequest.setOutTradeNo(orderNo);
                wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);
                wxPayRefundRequest.setRefundDesc("用户退款");
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
                return true;
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

    //通过key获取对象
    private SysConfig selectConfig(List<SysConfig> list, String key) {
        for (SysConfig configDO : list) {
            if (key.equals(configDO.getConfigKey())) {
                return configDO;
            }
        }
        return new SysConfig();
    }
}
