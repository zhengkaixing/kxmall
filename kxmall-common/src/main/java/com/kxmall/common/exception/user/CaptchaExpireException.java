package com.kxmall.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire");
    }
}
