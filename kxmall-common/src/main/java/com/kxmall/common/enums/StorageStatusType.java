package com.kxmall.common.enums;

/**
 * 仓库状态，枚举
 */
public enum StorageStatusType {

    NOMRAL(1, "正常"),
    ABORT(0, "禁用");


    private int code;

    private String msg;

    StorageStatusType(int code, String msg) {
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
