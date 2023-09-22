
package com.kxmall.wechat.listener;


import com.kxmall.common.event.TemplateEvent;
import com.kxmall.wechat.service.WeixinTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author kxmall
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
    }
}
