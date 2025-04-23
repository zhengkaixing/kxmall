
package com.kxmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author kxmall
 * 优惠券状态枚举
 */
@Getter
@AllArgsConstructor
public enum CouponStatusEnum {



	STATUS_0(0,"未使用"),
	STATUS_1(1,"已使用"),
	STATUS_2(2,"已过期");


	private Integer value;
	private String desc;

	public static CouponStatusEnum toType(int value) {
		return Stream.of(CouponStatusEnum.values())
				.filter(p -> p.value == value)
				.findAny()
				.orElse(null);
	}


}
