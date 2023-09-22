package com.kxmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 产品相关规格类型枚举
 */
@Getter
@AllArgsConstructor
public enum SpecTypeEnum {
    TYPE_0(0,"单规格"),
    TYPE_1(1,"多规格");

    private Integer value;
    private String desc;
}
