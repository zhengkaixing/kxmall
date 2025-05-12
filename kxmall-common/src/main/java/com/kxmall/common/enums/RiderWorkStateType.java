package com.kxmall.common.enums;

/**
 * @description: 配送员休息开工枚举
 * @author: kxmall
 * @date: 2020/02/28 19:58
 **/
public enum  RiderWorkStateType {

    REST(0,"休息"),

    WORKING(1,"开工");

    private int code;

    private String msg;

    RiderWorkStateType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }
}
