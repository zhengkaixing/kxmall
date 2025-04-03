package com.kxmall.notify;

import com.kxmall.common.constant.MessageTemplateConstants;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.utils.DateUtils;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.wechat.domain.vo.SocialWxaSubscribeMessageSendReqVo;
import com.kxmall.wechat.service.MiniTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

import static com.kxmall.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

/**
 * Description: 微信公众号通知
 * User: admin
 * Date: 2022/12/27
 * Time: 16:19
 */
public class UniNotifyMemberNotifyBizServiceImpl implements MemberNotifyBizService {


    private static final Logger logger = LoggerFactory.getLogger(UniNotifyMemberNotifyBizServiceImpl.class);


    @Resource
    private MiniTemplateService miniTemplateService;

    @Override
    @Async
    public void newOrder(KxStoreOrderVo storeOrder) {
        try {
            // 2. 构建并发送模版消息
            miniTemplateService.sendWxaSubscribeMessage(new SocialWxaSubscribeMessageSendReqVo()
                    .setUserId(storeOrder.getUid())
                    .setTemplateTitle(MessageTemplateConstants.ORDER_SUCCESSFUL)
                    .setPage("pages/order/list") // 订单列表
                    .addMessage("character_string6", storeOrder.getOrderId()) // 支付单编号
                    .addMessage("date5", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS,storeOrder.getCreateTime())) // 充值时间
                    .addMessage("amount9", storeOrder.getPayPrice().toString()) // 充值金额
                    .addMessage("phrase8", OrderStatusType.valueOf(String.valueOf(storeOrder.getStatus())).getMsg())
                    .addMessage("thing10", "店家开始配货，商品正在路上"));
        } catch (Exception e) {
            logger.error("[通知会员] 异常", e);
        }
    }

    @Override
    public void refundOrder(KxStoreOrderVo orderDTO) {

    }


    @Override
    @Async
    public void completeOrder(KxStoreOrderVo storeOrder) {
        try {
            // 2. 构建并发送模版消息
            miniTemplateService.sendWxaSubscribeMessage(new SocialWxaSubscribeMessageSendReqVo()
                    .setUserId(storeOrder.getUid())
                    .setTemplateTitle(MessageTemplateConstants.ORDER_FULFILLMENT)
                    .setPage("pages/order/list") // 订单列表
                    .addMessage("character_string1", storeOrder.getOrderId()) // 支付单编号
                    .addMessage("thing8", storeOrder.getRealName()) // 收货人
                    .addMessage("thing9", storeOrder.getUserAddress()) // 收货地址
                    .addMessage("date7", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS,storeOrder.getUpdateTime())) // 完成时间
                    .addMessage("thing5", "订单已完成"));
        } catch (Exception e) {
            logger.error("[通知会员] 异常", e);
        }
    }
}
