
package com.kxmall.wechat.handler;

import com.kxmall.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SubscribeHandler extends AbstractHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

          String content = "非常高兴为您服务！若您需要查询工资信息，请直接点击工资条进行查询如果不了解使用方式，只需简单回复“工资条”，我们会为您提供使用介绍，帮助您快速查询到所需信息。\n" +
              "感谢您对我们的信任和支持！";
          return new TextBuilder().build(content, wxMessage, weixinService);
    }


}
