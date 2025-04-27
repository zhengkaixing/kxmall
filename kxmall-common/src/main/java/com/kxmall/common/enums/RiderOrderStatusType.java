package com.kxmall.common.enums;

/**
 * @description: 配送状态
 * @author: kxmall
 * @date: 2020/03/02 17:08
 **/
public enum RiderOrderStatusType {

    WAITING(0, "待取货"),
    DISPENSE(1, "配送中"),
    TIMEOUT(2, "超时"),
    ABNORMAL(3, "配送异常"),
    COMPLETED(4, "已完成"),
    RIDERERROR(5, "被更改分配");
    private Integer code;

    private String msg;

    RiderOrderStatusType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static RiderOrderStatusType getBycode(Integer code) {
        for (RiderOrderStatusType orderCode : values()) {
            if (orderCode.getCode().equals(code)) {
                return orderCode;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
