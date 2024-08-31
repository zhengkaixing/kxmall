package com.kxmall.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 小程序登录用户身份权限
 *
 * @author 郅兴开源团队-小黑
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class XcxLoginUser extends LoginUser {

    private static final long serialVersionUID = 1L;

    /**
     * openid
     */
    private String openid;

}
