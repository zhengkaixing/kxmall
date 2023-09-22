package com.kxmall.web.controller.callback;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.web.controller.order.service.IKxAppOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @date 2023/3/10
 */
@RestController
@RequestMapping("/cb")
public class CallbackController {


    @Autowired
    private IKxAppOrderService appOrderService;

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @RequestMapping("/wxpay")
    @Transactional(rollbackFor = Exception.class)
    public Object wxpay(@RequestBody String body) throws Exception {
//        ============微信支付回调代码 prod 开始============
//        WxPayOrderNotifyResult result = null;
//        try {
//            result = wxPayService.parseOrderNotifyResult(body);
//        } catch (WxPayException e) {
//            logger.error("[微信解析回调请求] 异常", e);
//            return WxPayNotifyResponse.fail(e.getMessage());
//        }
//        logger.info("处理腾讯支付平台的订单支付");
//        logger.info(JSONObject.toJSONString(result));
//        ============微信支付回调代码 prod 结束============

//        ============微信支付回调代码 dev 开始============
        WxPayOrderNotifyResult result = JSONObject.parseObject(body, WxPayOrderNotifyResult.class);
//        ============微信支付回调代码 dev 结束============
        /* 之前传过去的我们系统的订单ID */
        String orderNo = result.getOutTradeNo();
        String payId = result.getTransactionId();

        List<KxStoreOrder> KxStoreOrderList = appOrderService.selectListByWrapper(
            new QueryWrapper<KxStoreOrder>()
                .eq("order_id", orderNo));

        if (CollectionUtils.isEmpty(KxStoreOrderList)) {
            return WxPayNotifyResponse.fail("订单不存在 orderNo=" + orderNo);
        }

        KxStoreOrder order = KxStoreOrderList.get(0);

        // 检查这个订单是否已经处理过
        if (order.getStatus() != OrderStatusType.UNPAY.getCode()) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        Integer totalFee = result.getTotalFee();

        // 检查支付订单金额
        if (!totalFee.equals(order.getPayPrice().intValue())) {
            return WxPayNotifyResponse.fail(order.getOrderId() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        //**************** 在此之前都没有 数据库修改 操作 所以前面是直接返回错误的 **********************//

        KxStoreOrder updateOrderDO = KxStoreOrder.builder().build();
        updateOrderDO.setPayId(payId);
        updateOrderDO.setPayChannel("WX");
        updateOrderDO.setPayPrice(BigDecimal.valueOf(totalFee));
        updateOrderDO.setPayTime(new Date());
        updateOrderDO.setUpdateTime(order.getPayTime());
        updateOrderDO.setStatus(OrderStatusType.WAIT_PREPARE_GOODS.getCode());
        appOrderService.changeOrderStatus(orderNo, OrderStatusType.UNPAY.getCode(), updateOrderDO);

        return WxPayNotifyResponse.success("支付成功");
    }

}
