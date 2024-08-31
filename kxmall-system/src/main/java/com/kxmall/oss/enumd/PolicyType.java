package com.kxmall.oss.enumd;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * minio策略配置
 *
 * @author 郅兴开源团队-小黑
 */
@Getter
@AllArgsConstructor
public enum PolicyType {

    /**
     * 只读
     */
    READ("read-only"),

    /**
     * 只写
     */
    WRITE("write-only"),

    /**
     * 读写
     */
    READ_WRITE("read-write");

    /**
     * 类型
     */
    private final String type;

}
