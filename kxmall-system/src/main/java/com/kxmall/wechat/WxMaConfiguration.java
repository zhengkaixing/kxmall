package com.kxmall.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Maps;
import com.kxmall.system.service.ISysConfigService;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 小程序配置
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Configuration(proxyBeanMethods = false)
public class WxMaConfiguration {
    private static Map<String, WxMaService> maServices = Maps.newHashMap();
    private static Map<String, WxMaMessageRouter> routers = Maps.newHashMap();

    private static WxMaMessageHandler wxMaMessageHandler;

    private static ISysConfigService configService;

    private final static String KXMALL_WEIXIN_MA_SERVICE = "kxmall_weixin_ma_service";
    private final static String BINDSTATECHANGE = "bindstatechange";


    public static WxMaMessageRouter getRouter(String appid) {
        return routers.get(appid);
    }
    @Autowired
    public WxMaConfiguration(ISysConfigService configService) {
        WxMaConfiguration.configService = configService;
    }

    public static WxMaService getWxMaService() {
        WxMaService wxMaService = maServices.get(KXMALL_WEIXIN_MA_SERVICE);
        //增加一个redis标识
        if(wxMaService == null){
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setMsgDataFormat("JSON");
            config.setAppid(configService.selectConfigByKey("mini_appid"));
            config.setSecret(configService.selectConfigByKey("mini_appSecret"));
            config.setToken(configService.selectConfigByKey("mini_token"));
            config.setAesKey(configService.selectConfigByKey("mini_aesKey"));
            wxMaService = new WxMaServiceImpl();
            wxMaService.setWxMaConfig(config);
            maServices.put(KXMALL_WEIXIN_MA_SERVICE, wxMaService);
            routers.put(KXMALL_WEIXIN_MA_SERVICE, newRouter(wxMaService));
        }
        return wxMaService;
    }
    /**
     * 移除WxMpService
     */
    public static void removeWxMaService(){
        maServices.remove(KXMALL_WEIXIN_MA_SERVICE);
        routers.remove(KXMALL_WEIXIN_MA_SERVICE);
    }
    private static WxMaMessageRouter newRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router
                .rule().handler(wxMaMessageHandler).next()
                .rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(BINDSTATECHANGE).handler(BINDSTATECHANGE_HANDLER).end();
        return router;
    }
    private static final WxMaMessageHandler BINDSTATECHANGE_HANDLER = (wxMessage, context, service, sessionManager) -> {
        wxMessage.getFromUser();
        wxMessage.getContent();
        return null;
    };
}

