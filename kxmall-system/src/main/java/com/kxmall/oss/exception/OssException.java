package com.kxmall.oss.exception;

/**
 * OSS异常类
 *
 * @author kxmall
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
