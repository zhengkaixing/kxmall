package com.kxmall.notify;

import cn.hutool.core.date.DateUtil;
import com.kxmall.common.event.TemplateBean;
import com.kxmall.common.event.TemplateEvent;
import com.kxmall.common.event.TemplateListenEnum;
import com.kxmall.order.domain.bo.OrderMessageBO;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description: 微信公众号通知
 * User: admin
 * Date: 2022/12/27
 * Time: 16:19
 */
public class UniNotifyAdminNotifyBizServiceImpl implements AdminNotifyBizService {


    private static final Logger logger = LoggerFactory.getLogger(UniNotifyAdminNotifyBizServiceImpl.class);

    @Resource
    private ApplicationEventPublisher publisher;

    @Override
    public void newOrder(KxStoreOrderVo storeOrder) {
        try {
            //公众号通知
            TemplateBean templateBean = TemplateBean.builder()
                    .orderId(storeOrder.getOrderId())
                    .name(storeOrder.getUserPhone())
                    .templateType(TemplateListenEnum.TYPE_1.getValue())
                    .time(DateUtil.formatDateTime(new Date()))
                    .price(storeOrder.getPayChannel() + "")
                    .storeId(storeOrder.getStoreId())
                    .build();
            publisher.publishEvent(new TemplateEvent(this, templateBean));
        } catch (Exception e) {
            logger.error("[通知管理员] 异常", e);
        }
    }

    @Override
    public void refundOrder(KxStoreOrderVo orderDTO) {

    }

    @Override
    public void newRiderOrder(OrderMessageBO orderMessageBO) {
        //公众号通知
        TemplateBean templateBean = TemplateBean.builder()
                .orderId(orderMessageBO.getOrderNo())
                .templateType(TemplateListenEnum.TYPE_2.getValue())
                .time(DateUtil.formatDateTime(new Date()))
                .price(orderMessageBO.getTotalPrice().toString())
                .address(orderMessageBO.getAddress())
                .freightPrice(orderMessageBO.getFreightPrice().toString())
                .riderId(orderMessageBO.getRiderId())
                .build();
        publisher.publishEvent(new TemplateEvent(this, templateBean));
    }


}
