package com.kxmall.wechat;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Maps;
import com.kxmall.common.enums.PayMethodEnum;
import com.kxmall.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 支付配置
 *
 * @author kxmall
 * @date 2020/03/01
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class WxPayConfiguration {

    private static Map<String, WxPayService> payServices = Maps.newHashMap();

    private static ISysConfigService configService;

    private final static String KXMALL_WEIXIN_PAY_SERVICE = "kxmall_weixin_pay_service";

    @Autowired
    public WxPayConfiguration(ISysConfigService configService) {
        WxPayConfiguration.configService = configService;
    }

    /**
     * 获取WxPayService
     *
     * @return
     */
    public static WxPayService getPayService(PayMethodEnum payMethodEnum) {
        WxPayService wxPayService = payServices.get(KXMALL_WEIXIN_PAY_SERVICE + payMethodEnum.getValue());
        if (wxPayService == null) {
            WxPayConfig payConfig = new WxPayConfig();
            switch (payMethodEnum) {
                case H5:
                    payConfig.setAppId(configService.selectConfigByKey("h5_appid"));
                    break;
                case MINI:
                    payConfig.setAppId(configService.selectConfigByKey("mini_appId"));
                    break;
                case APP:
                    payConfig.setAppId(configService.selectConfigByKey("app_appId"));
                    break;
                default:
            }
            payConfig.setMchId(configService.selectConfigByKey("wxpay_mchId"));
            payConfig.setMchKey(configService.selectConfigByKey("wxpay_mchKey"));
            payConfig.setKeyPath(configService.selectConfigByKey("wxpay_keyPath"));
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(payConfig);
            payServices.put(KXMALL_WEIXIN_PAY_SERVICE + payMethodEnum.getValue(), wxPayService);
        }
        return wxPayService;
    }

    /**
     * 移除WxPayService
     */
    public static void removeWxPayService() {
        payServices.remove(KXMALL_WEIXIN_PAY_SERVICE);
    }

}
