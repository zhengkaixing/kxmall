package com.kxmall.common.enums;

/**
 * @@description:
 * @author: kxmall
 * @date: 2020/03/08 19:23
 **/
public enum RiderOrderAbnormalType {

    TIMEOUT(2, "超时"),
    ABNORMAL(3, "配送异常");
    private Integer code;

    private String msg;

    RiderOrderAbnormalType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
