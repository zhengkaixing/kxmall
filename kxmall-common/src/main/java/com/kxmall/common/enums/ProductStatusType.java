package com.kxmall.common.enums;

/**
 * Created by admin on 2023/7/2
 */
public enum ProductStatusType {
    STOCK(0, "库存中"),
    SELLING(1, "售卖中"),
            ;

    ProductStatusType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
