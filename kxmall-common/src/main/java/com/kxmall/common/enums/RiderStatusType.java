package com.kxmall.common.enums;

public enum  RiderStatusType {
    NOMRAL(1, "正常"),
    ABORT(0, "禁用");


    private int code;

    private String msg;

    RiderStatusType(int code, String msg) {
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
