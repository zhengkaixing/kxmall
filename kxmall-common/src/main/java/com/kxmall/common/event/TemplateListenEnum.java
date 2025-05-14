package com.kxmall.common.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;


/**
 * 模板监听枚举
 */
@Getter
@AllArgsConstructor
public enum TemplateListenEnum {
    TYPE_1("1","商家微信通知"),
    TYPE_2("2","骑手微信通知");
    private String value;
    private String desc;

    public static TemplateListenEnum toType(String value) {
        return Stream.of(TemplateListenEnum.values())
                .filter(p -> p.value.equals(value))
                .findAny()
                .orElse(null);
    }
}
