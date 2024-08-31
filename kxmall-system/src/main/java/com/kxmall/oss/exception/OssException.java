package com.kxmall.oss.exception;

/**
 * OSS异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
