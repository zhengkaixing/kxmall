package com.kxmall.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match");
    }
}
