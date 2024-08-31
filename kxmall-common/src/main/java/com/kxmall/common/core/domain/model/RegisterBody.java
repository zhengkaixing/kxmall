package com.kxmall.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户注册对象
 *
 * @author 郅兴开源团队-小黑
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBody extends LoginBody {

    private String userType;

}
