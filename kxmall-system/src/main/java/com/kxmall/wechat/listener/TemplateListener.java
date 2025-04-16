
package com.kxmall.wechat.listener;


import com.kxmall.common.event.TemplateBean;
import com.kxmall.common.event.TemplateEvent;
import com.kxmall.common.event.TemplateListenEnum;
import com.kxmall.wechat.service.WeixinTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 郅兴开源团队-小黑
 * 异步监听模板通知事件
 */
@Slf4j
@Component
public class TemplateListener implements SmartApplicationListener {

    @Autowired
    private WeixinTemplateService weixinTemplateService;



    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return aClass == TemplateEvent.class;
    }

    @Async
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //转换事件类型
        TemplateEvent templateEvent = (TemplateEvent) applicationEvent;
        //获取注册用户对象信息
        TemplateBean templateBean = templateEvent.getTemplateBean();
        log.info("模板事件类型：{}", templateBean.getTemplateType());
        //todo
        if (TemplateListenEnum.toType(templateBean.getTemplateType()) == TemplateListenEnum.TYPE_1) {
            //商家消息推送
            weixinTemplateService.sendPlayOrder(templateBean.getName()
                    , templateBean.getTime(), templateBean.getPrice(), templateBean.getOrderId(), templateBean.getStoreId());
        }
        if (TemplateListenEnum.toType(templateBean.getTemplateType()) == TemplateListenEnum.TYPE_2) {
            //骑手消息推送
            weixinTemplateService.sendPlayRiderOrder(templateBean.getTime(), templateBean.getPrice(), templateBean.getOrderId(), templateBean.getAddress(), templateBean.getFreightPrice(), templateBean.getRiderId());
        }
    }
}
