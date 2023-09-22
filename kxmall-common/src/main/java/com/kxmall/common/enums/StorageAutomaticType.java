package com.kxmall.common.enums;

/**
 * @@description: 仓库是否自动分配订单
 * @author: kxmall
 * @date: 2020/03/09 21:40
 **/
public enum StorageAutomaticType {

    NO_AUTOMATIC(0, "非自动"),
    AUTOMATIC(1, "自动");

    private int code;

    private String msg;

    StorageAutomaticType(int code, String msg) {
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
