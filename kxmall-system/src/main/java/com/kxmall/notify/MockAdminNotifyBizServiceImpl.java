package com.kxmall.notify;

import com.alibaba.fastjson.JSONObject;
import com.kxmall.order.domain.bo.OrderMessageBO;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 为了能够正常启动demo的mock实现
 * User: admin
 * Date: 2022/12/27
 * Time: 16:22
 */
public class MockAdminNotifyBizServiceImpl implements AdminNotifyBizService {

    private static final Logger logger = LoggerFactory.getLogger(MockAdminNotifyBizServiceImpl.class);

    @Override
    public void newOrder(KxStoreOrderVo orderDTO) {
        logger.info("[mock通知 有新订单] 请及时发货：" + JSONObject.toJSONString(orderDTO));
    }

    @Override
    public void refundOrder(KxStoreOrderVo orderDTO) {
        logger.info("[mock通知 有新退款] 请及时处理：" + JSONObject.toJSONString(orderDTO));
    }

    @Override
    public void newRiderOrder(OrderMessageBO orderMessageBO) {
        logger.info("[mock通知 有新骑手订单] 请及时处理：" + JSONObject.toJSONString(orderMessageBO));
    }
}
