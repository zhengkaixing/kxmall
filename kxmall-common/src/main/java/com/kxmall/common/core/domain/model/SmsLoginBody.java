package com.kxmall.common.core.domain.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 短信登录对象
 *
 * @author 郅兴开源团队-小黑
 */

@Data
public class SmsLoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String phonenumber;

    /**
     * 用户密码
     */
    @NotBlank(message = "{sms.code.not.blank}")
    private String smsCode;

}
