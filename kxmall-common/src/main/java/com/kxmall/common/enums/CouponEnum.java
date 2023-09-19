
package com.kxmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author kxmall
 * 优惠券相关枚举
 */
@Getter
@AllArgsConstructor
public enum CouponEnum {

	FALI_0(0L,"有效"),
	FALI_1(1L,"无效"),

	USE_0(0L,"不可用"),
	USE_1(1L,"可用"),

	STATUS_0(0L,"未使用"),
	STATUS_1(1L,"已使用"),
	STATUS_2(2L,"已过期"),

	TYPE_0(0L,"通用券"),
	TYPE_1(1L,"商品券"),
	TYPE_2(2L,"内部券"),

	PERMANENT_0(0L,"限量"),
	PERMANENT_1(1L,"不限量");




	private Long value;
	private String desc;

	public static CouponEnum toType(int value) {
		return Stream.of(CouponEnum.values())
				.filter(p -> p.value == value)
				.findAny()
				.orElse(null);
	}


}
