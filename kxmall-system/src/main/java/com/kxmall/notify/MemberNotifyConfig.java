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
public class MemberNotifyConfig {

    @Autowired
    private ISysConfigService configService;

    @Bean
    public MemberNotifyBizService memberNotifyBizService() {
        String enable = configService.selectConfigByKey("uninotify");
        if ("mock".equalsIgnoreCase(enable)) {
            return new MockMemberNotifyBizServiceImpl();
        } else if ("uninotify".equalsIgnoreCase(enable)) {
            return new UniNotifyMemberNotifyBizServiceImpl();
        } else {
            return new MockMemberNotifyBizServiceImpl();
        }
    }

}
