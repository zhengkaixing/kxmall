package com.kxmall.common.enums;

public enum GoodsOutStockType {
    TO_BE_FOR_STOCK(0, "待出库"),
    OUT_FOR_STOCK(1, "已出库"),
    ;

    GoodsOutStockType(int code, String msg) {
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
