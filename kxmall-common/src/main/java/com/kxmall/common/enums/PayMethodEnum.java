package com.kxmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author kxmall
 * 支付相关枚举
 */
@Getter
@AllArgsConstructor
public enum PayMethodEnum {

    H5("h5", "公众号支付"),
    MINI("mini", "小程序支付"),
    APP("app", "app支付");


    private String value;
    private String desc;

    public static PayMethodEnum toType(String value) {
        return Stream.of(PayMethodEnum.values())
            .filter(p -> p.value.equals(value))
            .findAny()
            .orElse(null);
    }


}
