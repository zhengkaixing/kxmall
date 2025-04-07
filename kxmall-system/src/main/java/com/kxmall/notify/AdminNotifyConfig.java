package com.kxmall.notify;

import com.kxmall.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * User: admin
 * Date: 2022/12/27
 * Time: 20:15
 */
@Configuration
public class AdminNotifyConfig {

    @Autowired
    private ISysConfigService configService;

    @Bean
    public AdminNotifyBizService adminNotifyBizService() {
        String enable = configService.selectConfigByKey("uninotify");
        if ("mock".equalsIgnoreCase(enable)) {
            return new MockAdminNotifyBizServiceImpl();
        } else if ("uninotify".equalsIgnoreCase(enable)) {
            return new UniNotifyAdminNotifyBizServiceImpl();
        } else {
            return new MockAdminNotifyBizServiceImpl();
        }
    }

}
