package com.kxmall.common.enums;

/**
 * @@description: 仓库营业状态
 * @author: kxmall
 * @date: 2020/02/21 11:54
 **/
public enum StorageBusinessStatusType {

    REST(0, "休息中"),
    BUSINESS(1, "营业中");

    private int code;

    private String msg;

    StorageBusinessStatusType(int code, String msg) {
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
