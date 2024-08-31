package com.kxmall.common.exception.user;

import com.kxmall.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object... args) {
        super("user", code, args, null);
    }
}
