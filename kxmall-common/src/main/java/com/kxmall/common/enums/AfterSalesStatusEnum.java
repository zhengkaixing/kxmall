/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package com.kxmall.common.enums;

import lombok.Getter;

/**
 * 售后状态枚举
 *
 * @author 郅兴开源团队-小黑
 * @date 2021/12/21
 */
@Getter
public enum AfterSalesStatusEnum {

	STATUS_0(0,"已提交等待平台审核"),
	STATUS_1(1,"平台已审核,等待用户发货/退款"),
	STATUS_2(2,"用户已发货"),
	STATUS_3(3,"已完成");

	AfterSalesStatusEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code;

	private String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}


}
