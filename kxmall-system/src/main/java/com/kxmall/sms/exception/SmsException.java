package com.kxmall.sms.exception;

/**
 * Sms异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class SmsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SmsException(String msg) {
        super(msg);
    }

}
