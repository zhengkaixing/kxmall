package com.kxmall.common.core.service;

/**
 * 通用 参数配置服务
 *
 * @author 郅兴开源团队-小黑
 */
public interface ConfigService {

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    String getConfigValue(String configKey);

}
