package com.kxmall.common.enums;

/**
 * @description: 用户默认地址枚举类
 * @author: kxmall
 * @date: 2020/03/07 21:14
 **/
public enum DefaultAddressStatusType {

    COMMON_ADDRESS(0, "非默认地址"),
    DEFAULT_ADDRESS(1, "默认地址");

    DefaultAddressStatusType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
