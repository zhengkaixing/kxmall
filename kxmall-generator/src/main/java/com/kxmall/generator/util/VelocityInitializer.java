package com.kxmall.generator.util;

import com.kxmall.common.constant.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * VelocityEngine工厂
 *
 * @author 郅兴开源团队-小黑
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VelocityInitializer {

    /**
     * 初始化vm方法
     */
    public static void initVelocity() {
        Properties p = new Properties();
        try {
            // 加载classpath目录下的vm文件
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
